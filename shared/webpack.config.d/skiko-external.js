if (__dirname.includes('/build/js/')) {
    config.externals = Object.assign({}, config.externals, {
        './skiko.mjs': 'globalThis.__medexSkikoModule',
    });
    config.externalsType = 'var';
}
