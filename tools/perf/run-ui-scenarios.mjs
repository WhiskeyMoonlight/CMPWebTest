import { createReadStream, existsSync, readFileSync, statSync, writeFileSync } from 'node:fs';
import { createServer } from 'node:http';
import { tmpdir } from 'node:os';
import { extname, join, normalize, resolve } from 'node:path';
import { setTimeout as sleep } from 'node:timers/promises';
import { connectPage, launchChrome } from './cdp.mjs';

const distDir = resolve(process.env.DIST_DIR ?? 'webApp/build/dist/wasmJs/productionExecutable');
const label = process.env.LABEL ?? distDir;
const httpPort = Number(process.env.HTTP_PORT ?? 8093);
const cdpPort = Number(process.env.CDP_PORT ?? 9336);
const cpuThrottling = Number(process.env.CPU_THROTTLING ?? 4);
const runs = Number(process.env.RUNS ?? 3);
const scenarioNames = (process.env.SCENARIOS ?? 'idle,idleHud,checkIn,tabs,marketplaceScroll,carouselSwipe').split(',');
const swipePairs = (process.env.SWIPES ?? '0:-320,-86:-320,-185:-320,-268:-320,-320:-185,-320:0')
    .split(',')
    .map((pair) => pair.split(':').map(Number));
const chromeArgs = (process.env.CHROME_ARGS ?? '').split(' ').filter(Boolean);
const output = process.env.OUTPUT ?? join(tmpdir(), `medex-ui-scenarios-${Date.now()}.json`);
const profileTop = Number(process.env.PROFILE_TOP ?? 0);
const dprCap = Number(process.env.DPR_CAP ?? 0);
const base = `http://127.0.0.1:${httpPort}/`;

const DEVICE = { width: 412, height: 915, deviceScaleFactor: 2.625, mobile: true };
const USER_AGENT = 'Mozilla/5.0 (Linux; Android 14; Pixel 7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/141.0.0.0 Mobile Safari/537.36';
const TRACE_CATEGORIES = ['devtools.timeline', 'disabled-by-default-devtools.timeline', 'v8'];
const SETTLE_MS = 3500;
const RENDER_MIN_MS = 0.3;
const LONG_TASK_MS = 50;
const MOVED_PX = 20;
const MIME = {
    '.html': 'text/html; charset=utf-8',
    '.js': 'text/javascript',
    '.mjs': 'text/javascript',
    '.wasm': 'application/wasm',
    '.css': 'text/css',
    '.json': 'application/json',
    '.png': 'image/png',
    '.svg': 'image/svg+xml',
};

const probeSource = readFileSync(new URL('./ui-probe.js', import.meta.url), 'utf8');
const traceSink = { events: null };

function serve(dir, port) {
    const server = createServer((request, response) => {
        const path = decodeURIComponent(new URL(request.url, base).pathname);
        let file = normalize(join(dir, path));
        if (!file.startsWith(dir)) return response.writeHead(403).end();
        if (existsSync(file) && statSync(file).isDirectory()) file = join(file, 'index.html');
        if (!existsSync(file)) return response.writeHead(404).end();
        response.writeHead(200, { 'Content-Type': MIME[extname(file)] ?? 'application/octet-stream', 'Cache-Control': 'max-age=3600' });
        createReadStream(file).pipe(response);
    });
    return new Promise((resolveServer) => server.listen(port, '127.0.0.1', () => resolveServer(server)));
}

function localDate() {
    const now = new Date();
    return `${now.getFullYear()}-${String(now.getMonth() + 1).padStart(2, '0')}-${String(now.getDate()).padStart(2, '0')}`;
}

function storagePreset({ hud = false } = {}) {
    return {
        'medex.onboarding.completed': 'true',
        'medex.checkin.lastPrompted': localDate(),
        'medex.dev.perfHud': String(hud),
    };
}

async function navigate(page, url) {
    const loaded = page.once('Page.loadEventFired', 60_000);
    await page.send('Page.navigate', { url });
    await loaded;
}

async function waitFor(page, expression, timeoutMs = 30_000) {
    const started = Date.now();
    while (Date.now() - started < timeoutMs) {
        if (await page.evaluate(expression).catch(() => false)) return;
        await sleep(100);
    }
    throw new Error(`Timeout: ${expression}`);
}

async function openApp(page, hash, storage) {
    const assignments = Object.entries(storage)
        .map(([key, value]) => `localStorage.setItem(${JSON.stringify(key)}, ${JSON.stringify(value)});`)
        .join('');
    const source = `if (location.origin === ${JSON.stringify(base.slice(0, -1))}) { localStorage.clear(); ${assignments} }`;
    const { identifier } = await page.send('Page.addScriptToEvaluateOnNewDocument', { source });
    await page.send('Emulation.setCPUThrottlingRate', { rate: 1 });
    await navigate(page, 'about:blank');
    await navigate(page, `${base}${hash}`);
    await page.send('Page.removeScriptToEvaluateOnNewDocument', { identifier });
    await waitFor(page, `globalThis.__medexUiProbe?.text().includes('Главная')`);
    await sleep(SETTLE_MS);
}

async function find(page, kind, value, timeoutMs = 6000) {
    const started = Date.now();
    do {
        const point = await page.evaluate(`globalThis.__medexUiProbe.${kind}(${JSON.stringify(value)})`);
        if (point) return point;
        await sleep(150);
    } while (Date.now() - started < timeoutMs);
    return null;
}

async function locate(page, kind, value) {
    const point = await find(page, kind, value);
    if (!point) throw new Error(`Не найден узел ${kind}: ${value}`);
    return point;
}

async function touch(page, type, x, y) {
    const touchPoints = type === 'touchEnd' ? [] : [{ x, y, id: 1, radiusX: 4, radiusY: 4, force: 1 }];
    await page.send('Input.dispatchTouchEvent', { type, touchPoints });
}

async function tap(page, point) {
    await touch(page, 'touchStart', point.x, point.y);
    await sleep(70);
    await touch(page, 'touchEnd');
}

async function drag(page, from, dx, dy, steps = 18) {
    await touch(page, 'touchStart', from.x, from.y);
    for (let step = 1; step <= steps; step++) {
        await sleep(16);
        const eased = (step / steps) ** 2;
        await touch(page, 'touchMove', from.x + dx * eased, from.y + dy * eased);
    }
    await sleep(200);
    await touch(page, 'touchMove', from.x + dx, from.y + dy);
    await touch(page, 'touchEnd');
}

async function traced(page, action) {
    traceSink.events = [];
    await page.send('Tracing.start', {
        transferMode: 'ReportEvents',
        traceConfig: { includedCategories: TRACE_CATEGORIES, recordMode: 'recordAsMuchAsPossible' },
    });
    await page.evaluate('globalThis.__medexUiProbe.reset()');
    if (profileTop > 0) await page.send('Profiler.start');
    await page.send('Emulation.setCPUThrottlingRate', { rate: cpuThrottling });
    const started = performance.now();
    const details = await action();
    const durationMs = performance.now() - started;
    await page.send('Emulation.setCPUThrottlingRate', { rate: 1 });
    const hotspots = profileTop > 0 ? topFunctions((await page.send('Profiler.stop')).profile) : undefined;
    const probe = await page.evaluate('globalThis.__medexUiProbe.snapshot()');
    const complete = page.once('Tracing.tracingComplete', 120_000);
    await page.send('Tracing.end');
    await complete;
    const events = traceSink.events;
    traceSink.events = null;
    return { ...summarize(events, durationMs, probe), ...details, hotspots };
}

function topFunctions(profile) {
    const selfMs = new Map();
    const byId = new Map(profile.nodes.map((node) => [node.id, node]));
    profile.samples.forEach((id, index) => {
        const frame = byId.get(id).callFrame;
        const name = frame.functionName || `(${frame.url.split('/').pop() || 'native'})`;
        selfMs.set(name, (selfMs.get(name) ?? 0) + (profile.timeDeltas[index] ?? 0) / 1000);
    });
    return [...selfMs.entries()]
        .filter(([name]) => name !== '(idle)' && name !== '(program)')
        .sort((a, b) => b[1] - a[1])
        .slice(0, profileTop)
        .map(([name, ms]) => `${round(ms)} мс ${name}`);
}

function percentile(values, fraction) {
    if (values.length === 0) return 0;
    const sorted = [...values].sort((a, b) => a - b);
    return sorted[Math.min(sorted.length - 1, Math.floor(fraction * sorted.length))];
}

const sum = (values) => values.reduce((total, value) => total + value, 0);
const round = (value) => Math.round(value * 10) / 10;

function mainThreadKey(events) {
    const names = new Map();
    const animationFrames = new Map();
    for (const event of events) {
        const key = `${event.pid}:${event.tid}`;
        if (event.ph === 'M' && event.name === 'thread_name') names.set(key, event.args.name);
        if (event.name === 'FireAnimationFrame') animationFrames.set(key, (animationFrames.get(key) ?? 0) + 1);
    }
    return [...names.entries()]
        .filter(([, name]) => name === 'CrRendererMain')
        .map(([key]) => key)
        .sort((a, b) => (animationFrames.get(b) ?? 0) - (animationFrames.get(a) ?? 0))[0];
}

function summarize(events, durationMs, probe) {
    const main = mainThreadKey(events);
    const slices = events.filter((event) => event.ph === 'X' && `${event.pid}:${event.tid}` === main);
    const durations = (names) => slices.filter((event) => names.includes(event.name)).map((event) => (event.dur ?? 0) / 1000);
    const renders = durations(['FireAnimationFrame']).filter((value) => value >= RENDER_MIN_MS);
    const tasks = durations(['RunTask', 'ThreadControllerImpl::RunTask']);
    const intervals = probe.frames.intervals;
    return {
        durationMs: Math.round(durationMs),
        rafFrames: intervals.length,
        intervalP95: round(percentile(intervals, 0.95)),
        intervalMax: round(Math.max(0, ...intervals)),
        dropped: intervals.filter((value) => value > 20).length,
        droppedTwice: intervals.filter((value) => value > 36).length,
        renders: renders.length,
        renderP50: round(percentile(renders, 0.5)),
        renderP95: round(percentile(renders, 0.95)),
        renderMax: round(Math.max(0, ...renders)),
        renderTotal: Math.round(sum(renders)),
        mainBusyPct: Math.round((sum(tasks) / durationMs) * 100),
        longTasks: tasks.filter((value) => value > LONG_TASK_MS).length,
        longestTask: round(Math.max(0, ...tasks)),
        styleLayoutMs: round(sum(durations(['UpdateLayoutTree', 'Layout']))),
        gcMs: round(sum(durations(['MinorGC', 'MajorGC']))),
        inputMs: round(sum(durations(['EventDispatch']))),
        loafCount: probe.frames.loafCount,
        loafBlockingMs: Math.round(probe.frames.loafBlocking),
        pointercancel: probe.input.pointercancel,
        touchmove: probe.input.touchmove,
        touchmovePrevented: probe.input.touchmovePrevented,
    };
}

const scenarios = {
    async idle(page) {
        await openApp(page, '#/home', storagePreset());
        return traced(page, async () => { await sleep(5000); return {}; });
    },
    async idleHud(page) {
        await openApp(page, '#/home', storagePreset({ hud: true }));
        return traced(page, async () => { await sleep(5000); return {}; });
    },
    async checkIn(page) {
        await openApp(page, '#/home', storagePreset());
        const mood = await locate(page, 'byLabel', 'Хорошее настроение');
        return traced(page, async () => {
            await tap(page, mood);
            await sleep(3000);
            return { sheetOpened: (await page.evaluate('globalThis.__medexUiProbe.text()')).includes('Симптомы') };
        });
    },
    async tabs(page) {
        await openApp(page, '#/home', storagePreset());
        const labels = ['Витрина', 'Медкарта', 'Ещё', 'Главная'];
        const points = [];
        for (const tabLabel of labels) points.push(await locate(page, 'byText', tabLabel));
        return traced(page, async () => {
            for (const point of points) {
                await tap(page, point);
                await sleep(1500);
            }
            return {};
        });
    },
    async marketplaceScroll(page) {
        await openApp(page, '#/marketplace', storagePreset());
        const before = await locate(page, 'byText', 'Анализы');
        return traced(page, async () => {
            for (const yDistance of [-450, -450, -450, 450, 450, 450]) {
                await page.send('Input.synthesizeScrollGesture', {
                    x: DEVICE.width / 2, y: DEVICE.height / 2, yDistance, speed: 1400, gestureSourceType: 'touch',
                });
                await sleep(500);
            }
            await sleep(700);
            const after = await page.evaluate(`globalThis.__medexUiProbe.byText('Анализы')`);
            return { returnedToStart: after != null && Math.abs(after.top - before.top) < MOVED_PX };
        });
    },
    async carouselSwipe(page) {
        const outcomes = [];
        for (const [dx, dy] of swipePairs) {
            const angle = Math.round((Math.atan2(Math.abs(dx), Math.abs(dy)) * 180) / Math.PI);
            await openApp(page, '#/marketplace', storagePreset());
            const tile = await locate(page, 'byText', 'Подарок на День Рождения');
            const section = await locate(page, 'byText', 'Записаться к врачу');
            const result = await traced(page, async () => {
                await drag(page, { x: DEVICE.width - 40, y: tile.y + 40 }, dx, dy);
                await sleep(1300);
                const sectionAfter = await find(page, 'byText', 'Записаться к врачу', 4000);
                const tileAfter = await find(page, 'byText', 'Подарок на День Рождения', 1000);
                if (sectionAfter == null) return { angle, outcome: 'unknown' };
                const vertical = Math.abs(sectionAfter.top - section.top) > MOVED_PX;
                const horizontal = !vertical && (tileAfter == null || Math.abs(tileAfter.left - tile.left) > MOVED_PX);
                const moved = { dTop: Math.round(sectionAfter.top - section.top), dLeft: tileAfter ? Math.round(tileAfter.left - tile.left) : null };
                return { angle, outcome: vertical ? 'vertical' : horizontal ? 'horizontal' : 'none', moved };
            });
            outcomes.push(result);
        }
        return { swipes: outcomes, pointercancel: sum(outcomes.map((item) => item.pointercancel)) };
    },
};

function median(values) {
    const numbers = values.filter((value) => typeof value === 'number');
    return numbers.length === 0 ? values[0] : percentile(numbers, 0.5);
}

function aggregate(results) {
    const keys = Object.keys(results[0]).filter((key) => key !== 'swipes' && key !== 'hotspots');
    return Object.fromEntries(keys.map((key) => [key, median(results.map((result) => result[key]))]));
}

const COLUMNS = ['renders', 'renderP50', 'renderP95', 'renderMax', 'renderTotal', 'rafFrames', 'dropped', 'droppedTwice', 'intervalMax', 'mainBusyPct', 'longTasks', 'styleLayoutMs', 'gcMs', 'pointercancel'];

async function main() {
    const server = await serve(distDir, httpPort);
    const profileDir = process.env.PROFILE_DIR ?? join(tmpdir(), `medex-ui-profile-${Date.now()}`);
    const pid = await launchChrome({ port: cdpPort, profileDir, width: DEVICE.width, height: DEVICE.height, headless: process.env.HEADFUL !== '1', extraArgs: chromeArgs });
    const page = await connectPage(cdpPort);
    page.on('Tracing.dataCollected', (params) => traceSink.events?.push(...params.value));
    try {
        await page.send('Page.enable');
        await page.send('Runtime.enable');
        await page.send('Emulation.setDeviceMetricsOverride', DEVICE);
        await page.send('Emulation.setTouchEmulationEnabled', { enabled: true, maxTouchPoints: 5 });
        await page.send('Emulation.setUserAgentOverride', { userAgent: USER_AGENT, platform: 'Android' });
        await page.send('Page.addScriptToEvaluateOnNewDocument', { source: probeSource });
        if (dprCap > 0) {
            const source = `Object.defineProperty(window, 'devicePixelRatio', { get: () => Math.min(${dprCap}, ${DEVICE.deviceScaleFactor}) });`;
            await page.send('Page.addScriptToEvaluateOnNewDocument', { source });
        }
        if (profileTop > 0) {
            await page.send('Profiler.enable');
            await page.send('Profiler.setSamplingInterval', { interval: 200 });
        }
        await navigate(page, base);
        const gpu = await page.evaluate(`(() => {
            const gl = document.createElement('canvas').getContext('webgl2');
            const info = gl?.getExtension('WEBGL_debug_renderer_info');
            return info ? gl.getParameter(info.UNMASKED_RENDERER_WEBGL) : 'unknown';
        })()`);
        const report = { label, distDir, gpu, cpuThrottling, device: DEVICE, runs, scenarios: {} };
        console.log(`${label} · GPU: ${gpu} · CPU ×${cpuThrottling} · ${DEVICE.width}×${DEVICE.height}@${DEVICE.deviceScaleFactor}`);
        console.log(`| Сценарий | ${COLUMNS.join(' | ')} |`);
        console.log(`| --- | ${COLUMNS.map(() => '---').join(' | ')} |`);
        for (const name of scenarioNames) {
            const results = [];
            for (let run = 0; run < runs; run++) {
                results.push(await scenarios[name].call({}, page));
            }
            const summary = aggregate(results);
            report.scenarios[name] = { summary, results };
            console.log(`| ${name} | ${COLUMNS.map((column) => summary[column] ?? '—').join(' | ')} |`);
            if (name === 'carouselSwipe') {
                const swipes = results.flatMap((result) => result.swipes);
                for (const angle of [...new Set(swipePairs.map(([dx, dy]) => Math.round((Math.atan2(Math.abs(dx), Math.abs(dy)) * 180) / Math.PI)))]) {
                    const own = swipes.filter((swipe) => swipe.angle === angle);
                    const counts = own.reduce((acc, swipe) => ({ ...acc, [swipe.outcome]: (acc[swipe.outcome] ?? 0) + 1 }), {});
                    console.log(`|   угол ${angle}° | ${JSON.stringify(counts)} · pointercancel ${sum(own.map((swipe) => swipe.pointercancel))} · ${own.map((swipe) => JSON.stringify(swipe.moved)).join(' ')} |`);
                }
            }
            results.forEach((result, run) => result.hotspots && console.log(`|   профиль, прогон ${run + 1} | ${result.hotspots.join(' · ')} |`));
            if (name === 'checkIn') console.log(`|   шторка открылась | ${results.map((result) => result.sheetOpened).join(', ')} |`);
        }
        writeFileSync(output, JSON.stringify(report, null, 2));
        console.log(`JSON: ${output}`);
    } finally {
        page.close();
        server.close();
        if (process.env.KEEP_CHROME !== '1') process.kill(pid);
    }
}

main().catch((error) => {
    console.error(error);
    process.exit(1);
});
