if (__dirname.includes('/build/js/')) {
    const skikoPath = require('path');
    const skikoProjectDir = skikoPath.resolve(__dirname, '../../../../shared');
    const skikoRuntimeDir = skikoProjectDir + '/build/processedResources/js/test';

    config.files.unshift({ pattern: skikoProjectDir + '/karma-bootstrap/skiko-globals.js', included: true, watched: false });
    config.files.push(
        { pattern: skikoRuntimeDir + '/skiko.mjs', included: false, served: true, watched: false },
        { pattern: skikoRuntimeDir + '/skiko.wasm', included: false, served: true, watched: false },
    );
}

config.client = config.client || {};
config.client.mocha = Object.assign({}, config.client.mocha, { timeout: 60000 });
