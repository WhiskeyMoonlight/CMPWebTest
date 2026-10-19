import { connectBrowser, connectPage, launchChrome } from './cdp.mjs';

const [, , command, ...args] = process.argv;
const port = Number(process.env.CDP_PORT ?? 9333);

async function main() {
    if (command === 'launch') {
        const pid = await launchChrome({ port, profileDir: args[0], headless: args[1] !== 'headful' });
        console.log(`chrome pid ${pid} on port ${port}`);
        return;
    }
    const page = await connectPage(port);
    try {
        switch (command) {
            case 'open':
                await page.send('Page.enable');
                await page.send('Page.navigate', { url: args[0] });
                await page.once('Page.loadEventFired');
                break;
            case 'eval':
                console.log(JSON.stringify(await page.evaluate(args.join(' ')), null, 2));
                break;
            case 'click':
                await page.click(Number(args[0]), Number(args[1]));
                break;
            case 'wheel':
                await page.wheel(Number(args[0]), Number(args[1]), Number(args[2]));
                break;
            case 'shot':
                console.log(await page.screenshot(args[0]));
                break;
            case 'grant':
            case 'deny-geolocation': {
                const origin = await page.evaluate('location.origin');
                const browser = await connectBrowser(port);
                try {
                    const names = command === 'grant' ? args : ['geolocation'];
                    const setting = command === 'grant' ? 'granted' : 'denied';
                    for (const name of names) {
                        await browser.send('Browser.setPermission', { origin, permission: { name }, setting });
                    }
                } finally {
                    browser.close();
                }
                break;
            }
            case 'geo':
                await page.send('Emulation.setGeolocationOverride', {
                    latitude: Number(args[0]),
                    longitude: Number(args[1]),
                    accuracy: Number(args[2] ?? 30),
                });
                break;
            case 'click-upload': {
                await page.send('Page.enable');
                await page.send('DOM.enable');
                await page.send('Page.setInterceptFileChooserDialog', { enabled: true });
                const chooser = page.once('Page.fileChooserOpened', 5_000);
                await page.click(Number(args[0]), Number(args[1]));
                const { backendNodeId, mode } = await chooser;
                await page.send('DOM.setFileInputFiles', { files: [args[2]], backendNodeId });
                console.log(`file chooser (${mode}) received ${args[2]}`);
                break;
            }
            case 'cpu':
                await page.send('Emulation.setCPUThrottlingRate', { rate: Number(args[0]) });
                break;
            default:
                throw new Error(`Unknown command ${command}`);
        }
    } finally {
        page.close();
    }
}

main().catch((error) => {
    console.error(error.message);
    process.exit(1);
});
