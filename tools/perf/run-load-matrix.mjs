import { readFileSync, writeFileSync } from 'node:fs';
import { tmpdir } from 'node:os';
import { join } from 'node:path';
import { setTimeout as sleep } from 'node:timers/promises';
import { connectPage, launchChrome } from './cdp.mjs';

const base = process.env.BASE_URL ?? 'http://localhost:8081/';
const port = Number(process.env.CDP_PORT ?? 9334);
const scale = process.env.SCALE ?? 'X1';
const cpuThrottling = Number(process.env.CPU_THROTTLING ?? 1);
const scenarios = (process.env.SCENARIOS ?? 'LabImport,AttachmentHash,ScanFilter,RequestStorm').split(',');
const modes = (process.env.MODES ?? 'Main,Default,Parallel,Cooperative,Worker').split(',');
const output = process.env.OUTPUT ?? join(tmpdir(), `medex-load-matrix-${Date.now()}.json`);
const routes = ['#/home', '#/marketplace', '#/medcard', '#/more', '#/more/capabilities', '#/more/dev'];

const probeSource = readFileSync(new URL('./frame-probe.js', import.meta.url), 'utf8');

async function openApp(page) {
    await page.send('Page.enable');
    await page.send('Runtime.enable');
    await page.send('Page.addScriptToEvaluateOnNewDocument', { source: probeSource });
    await navigate(page, base);
    await page.evaluate(`(() => {
        localStorage.setItem('medex.onboarding.completed', 'true');
        localStorage.setItem('medex.dev.perfHud', 'true');
        localStorage.setItem('medex.checkin.lastPrompted', new Date().toISOString().slice(0, 10));
    })()`);
    await navigate(page, `${base}?perf-bridge=1#/home`);
    for (let attempt = 0; attempt < 100 && !(await page.evaluate('typeof globalThis.medexPerfBridge === "object"')); attempt++) {
        await sleep(100);
    }
    if (!(await page.evaluate('typeof globalThis.medexPerfBridge === "object"'))) {
        throw new Error('medexPerfBridge не найден: соберите свежий дистрибутив и откройте его с ?perf-bridge=1');
    }
    await sleep(1500);
}

async function navigate(page, url) {
    const loaded = page.once('Page.loadEventFired', 30_000);
    await page.send('Page.navigate', { url });
    await loaded;
}

async function runOne(page, scenario, mode) {
    await page.evaluate('globalThis.__medexProbe.reset()');
    const id = await page.evaluate(`globalThis.medexPerfBridge.run('${scenario}:${mode}:${scale}')`);
    const started = Date.now();
    let step = 0;
    let task;
    while (Date.now() - started < 180_000) {
        await page.evaluate(`location.hash = '${routes[step % routes.length]}'`).catch(() => {});
        await page.wheel(640, 450, step % 2 === 0 ? 400 : -400).catch(() => {});
        step++;
        await sleep(300);
        const tasks = JSON.parse(await page.evaluate('globalThis.medexPerfBridge.tasks()'));
        task = tasks.find((candidate) => candidate.id === id);
        if (task && task.status !== 'Running') break;
    }
    const probe = await page.evaluate('globalThis.__medexProbe.snapshot()');
    await page.evaluate('globalThis.medexPerfBridge.clear()');
    return { scenario, mode, scale, cpuThrottling, task, probe, navigations: step };
}

function row(result) {
    const { task, probe } = result;
    return `| ${result.scenario} | ${result.mode} | ${task?.status ?? '—'} | ${task?.elapsedMs ?? '—'} | ${probe.frames} | ${Math.round(probe.worstGap)} | ${probe.over50} | ${probe.loafCount} / ${Math.round(probe.loafBlocking)} | ${task?.worstLagMs ?? '—'} | ${task?.summary ?? ''} |`;
}

async function main() {
    const profileDir = process.env.PROFILE_DIR ?? join(tmpdir(), 'medex-perf-profile');
    const pid = await launchChrome({ port, profileDir, headless: process.env.HEADFUL !== '1' });
    const page = await connectPage(port);
    try {
        if (cpuThrottling > 1) await page.send('Emulation.setCPUThrottlingRate', { rate: cpuThrottling });
        await openApp(page);
        const userAgent = await page.evaluate('navigator.userAgent');
        const cores = await page.evaluate('navigator.hardwareConcurrency');
        const results = [];
        for (const scenario of scenarios) {
            for (const mode of modes) {
                const result = await runOne(page, scenario, mode);
                results.push(result);
                console.error(row(result));
                await sleep(500);
            }
        }
        writeFileSync(output, JSON.stringify({ base, userAgent, cores, scale, cpuThrottling, results }, null, 2));
        console.log(`UA: ${userAgent}, ядер: ${cores}, масштаб: ${scale}, CPU throttling: ${cpuThrottling}x`);
        console.log('| Сценарий | Режим | Статус | Время, мс | Кадров | Худший кадр, мс | Кадров >50 мс | LoAF шт / блок. мс | Лаг UI (app), мс | Итог |');
        console.log('| --- | --- | --- | --- | --- | --- | --- | --- | --- | --- |');
        results.forEach((result) => console.log(row(result)));
        console.log(`JSON: ${output}`);
    } finally {
        page.close();
        if (process.env.KEEP_CHROME !== '1') process.kill(pid);
    }
}

main().catch((error) => {
    console.error(error);
    process.exit(1);
});
