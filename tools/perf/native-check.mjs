import { mkdirSync } from 'node:fs';
import { tmpdir } from 'node:os';
import { join } from 'node:path';
import { setTimeout as sleep } from 'node:timers/promises';
import { connectBrowser, connectPage } from './cdp.mjs';

const port = Number(process.env.CDP_PORT ?? 9333);
const base = process.env.BASE_URL ?? 'http://localhost:8081/';
const outDir = process.env.OUT_DIR ?? join(tmpdir(), 'medex-native-check');
const sampleFile = process.env.SAMPLE_FILE;
const capabilities = {
    pickFile: [265, 229],
    locate: [685, 435],
    map: [285, 552],
    call: [300, 611],
    promo: [360, 670],
};
mkdirSync(outDir, { recursive: true });

const browser = await connectBrowser(port);
const page = await connectPage(port);
const report = [];
const log = (step, value) => {
    report.push({ step, value });
    console.log(`${step}: ${typeof value === 'string' ? value : JSON.stringify(value)}`);
};

async function openCapabilities() {
    await page.send('Page.enable');
    const loaded = page.once('Page.loadEventFired', 30_000);
    await page.send('Page.navigate', { url: `${base}?perf-bridge=1&t=${Date.now()}#/more/capabilities` });
    await loaded;
    await sleep(2500);
}

async function setPermission(name, setting) {
    const origin = await page.evaluate('location.origin');
    await browser.send('Browser.setPermission', { origin, permission: { name }, setting });
}

async function shot(name) {
    return page.screenshot(join(outDir, `${name}.png`));
}

try {
    await openCapabilities();

    await setPermission('geolocation', 'granted');
    await page.send('Emulation.setGeolocationOverride', { latitude: 55.7337, longitude: 37.5886, accuracy: 18 });
    await page.click(...capabilities.locate);
    await sleep(1500);
    log('geolocation granted, screenshot', await shot('geo-granted'));

    await setPermission('geolocation', 'denied');
    await page.click(...capabilities.locate);
    await sleep(1500);
    log('geolocation denied, screenshot', await shot('geo-denied'));

    await setPermission('geolocation', 'granted');
    await page.click(...capabilities.locate);
    await sleep(1500);
    log('geolocation re-granted in settings, screenshot', await shot('geo-regranted'));

    await setPermission('clipboard-read', 'granted');
    await setPermission('clipboard-write', 'granted');
    await page.click(...capabilities.promo);
    await sleep(500);
    log('clipboard after promo click', await page.evaluate('navigator.clipboard.readText()').catch((error) => `error ${error.message}`));
    log('promo snackbar, screenshot', await shot('promo'));

    if (sampleFile) {
        await page.send('Page.bringToFront');
        log('visibility before picking', await page.evaluate('document.visibilityState'));
        await page.send('DOM.enable');
        await page.send('Page.setInterceptFileChooserDialog', { enabled: true });
        const blockingTask = await page.evaluate("globalThis.medexPerfBridge.run('AttachmentHash:Default:X10')");
        const clickedAt = Date.now();
        const chooser = page.once('Page.fileChooserOpened', 20_000);
        await page.click(...capabilities.pickFile);
        const { backendNodeId } = await chooser;
        log('file chooser opened while main thread was blocked, ms after click', Date.now() - clickedAt);
        await page.send('DOM.setFileInputFiles', { files: [sampleFile], backendNodeId });
        await sleep(3000);
        const tasks = JSON.parse(await page.evaluate('globalThis.medexPerfBridge.tasks()'));
        log('blocking task', tasks.find((task) => task.id === blockingTask));
        log('picked during load, screenshot', await shot('picked-under-load'));
    }
    const before = (await browser.send('Target.getTargets')).targetInfos.filter((target) => target.type === 'page').length;
    await page.click(...capabilities.map);
    await sleep(1000);
    await page.click(...capabilities.call);
    await sleep(1000);
    const after = (await browser.send('Target.getTargets')).targetInfos.filter((target) => target.type === 'page');
    log('pages opened by map and tel:', after.length - before);
    log('visibility after system actions', await page.evaluate('document.visibilityState'));
    log('page url after system actions', await page.evaluate('location.href'));

} finally {
    browser.close();
    page.close();
}
