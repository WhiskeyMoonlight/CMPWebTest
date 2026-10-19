(function () {
    var loaded = null;
    var forwardingApi = new Proxy({}, {
        get: function (target, name) {
            return function () {
                return loaded.skikoApi[name].apply(loaded.skikoApi, arguments);
            };
        }
    });
    globalThis.__medexSkikoModule = { skikoApi: forwardingApi };

    var skikoUrl = Object.keys(window.__karma__.files).find(function (path) { return path.endsWith('/skiko.mjs'); });
    var ready = import(skikoUrl).then(function (module) {
        return module.awaitSkiko.then(function (instance) {
            loaded = module;
            Object.keys(instance.wasmExports).forEach(function (name) {
                globalThis[name] = instance.wasmExports[name];
            });
        });
    });

    before(function () {
        this.timeout(60000);
        return ready;
    });
})();
