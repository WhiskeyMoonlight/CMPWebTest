import { spawn } from 'node:child_process';
import { existsSync, mkdirSync, readFileSync, writeFileSync } from 'node:fs';
import { setTimeout as sleep } from 'node:timers/promises';

const CHROME = process.env.CHROME_PATH ?? '/Applications/Google Chrome.app/Contents/MacOS/Google Chrome';

export async function launchChrome({ port, profileDir, width = 1280, height = 900, headless = true, extraArgs = [] }) {
    mkdirSync(profileDir, { recursive: true });
    disableBatterySaver(profileDir);
    const args = [
        `--remote-debugging-port=${port}`,
        `--user-data-dir=${profileDir}`,
        `--window-size=${width},${height}`,
        '--no-first-run',
        '--no-default-browser-check',
        '--disable-background-timer-throttling',
        '--disable-renderer-backgrounding',
        '--disable-backgrounding-occluded-windows',
        ...extraArgs,
        'about:blank',
    ];
    if (headless) args.unshift('--headless=new');
    const child = spawn(CHROME, args, { detached: true, stdio: 'ignore' });
    child.unref();
    for (let attempt = 0; attempt < 50; attempt++) {
        try {
            await fetch(`http://127.0.0.1:${port}/json/version`);
            return child.pid;
        } catch {
            await sleep(200);
        }
    }
    throw new Error(`Chrome did not open port ${port}`);
}

function disableBatterySaver(profileDir) {
    const path = `${profileDir}/Local State`;
    const state = existsSync(path) ? JSON.parse(readFileSync(path, 'utf8')) : {};
    state.performance_tuning = { ...state.performance_tuning, battery_saver_mode: { state: 0 } };
    writeFileSync(path, JSON.stringify(state));
}

export async function connectPage(port, urlPrefix = process.env.PAGE_URL_PREFIX ?? '') {
    const targets = await (await fetch(`http://127.0.0.1:${port}/json/list`)).json();
    const page = targets.find((target) => target.type === 'page' && target.url.startsWith(urlPrefix));
    if (!page) throw new Error('No page target');
    return CdpSession.open(page.webSocketDebuggerUrl);
}

export async function connectBrowser(port) {
    const version = await (await fetch(`http://127.0.0.1:${port}/json/version`)).json();
    return CdpSession.open(version.webSocketDebuggerUrl);
}

export class CdpSession {
    static async open(url) {
        const socket = new WebSocket(url);
        await new Promise((resolve, reject) => {
            socket.addEventListener('open', resolve, { once: true });
            socket.addEventListener('error', reject, { once: true });
        });
        return new CdpSession(socket);
    }

    constructor(socket) {
        this.socket = socket;
        this.nextId = 1;
        this.pending = new Map();
        this.listeners = new Map();
        socket.addEventListener('message', (event) => {
            const message = JSON.parse(event.data);
            if (message.id && this.pending.has(message.id)) {
                const { resolve, reject } = this.pending.get(message.id);
                this.pending.delete(message.id);
                if (message.error) reject(new Error(`${message.error.message} ${message.error.data ?? ''}`));
                else resolve(message.result);
            } else if (message.method) {
                (this.listeners.get(message.method) ?? []).forEach((listener) => listener(message.params));
            }
        });
    }

    send(method, params = {}) {
        const id = this.nextId++;
        this.socket.send(JSON.stringify({ id, method, params }));
        return new Promise((resolve, reject) => this.pending.set(id, { resolve, reject }));
    }

    on(method, listener) {
        const list = this.listeners.get(method) ?? [];
        list.push(listener);
        this.listeners.set(method, list);
    }

    once(method, timeoutMs = 10_000) {
        return new Promise((resolve, reject) => {
            const timer = setTimeout(() => reject(new Error(`Timeout waiting for ${method}`)), timeoutMs);
            this.on(method, (params) => {
                clearTimeout(timer);
                resolve(params);
            });
        });
    }

    async evaluate(expression) {
        const { result, exceptionDetails } = await this.send('Runtime.evaluate', {
            expression,
            awaitPromise: true,
            returnByValue: true,
        });
        if (exceptionDetails) throw new Error(exceptionDetails.exception?.description ?? exceptionDetails.text);
        return result.value;
    }

    async click(x, y) {
        for (const type of ['mouseMoved', 'mousePressed', 'mouseReleased']) {
            await this.send('Input.dispatchMouseEvent', { type, x, y, button: 'left', buttons: type === 'mousePressed' ? 1 : 0, clickCount: 1 });
        }
    }

    async wheel(x, y, deltaY) {
        await this.send('Input.dispatchMouseEvent', { type: 'mouseWheel', x, y, deltaX: 0, deltaY });
    }

    async screenshot(path) {
        const { data } = await this.send('Page.captureScreenshot', { format: 'png' });
        writeFileSync(path, Buffer.from(data, 'base64'));
        return path;
    }

    close() {
        this.socket.close();
    }
}
