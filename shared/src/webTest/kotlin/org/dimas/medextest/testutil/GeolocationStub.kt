package org.dimas.medextest.testutil

import kotlin.js.js

internal fun installGeolocationStub(mode: String, latitude: Double, longitude: Double, accuracy: Double, delayMs: Int): Unit = js(
    """(function () {
        var errors = { denied: [1, 'User denied Geolocation'], unavailable: [2, 'Position unavailable'], timeout: [3, 'Timeout expired'] };
        globalThis.__medexGeolocationCalls = 0;
        Object.defineProperty(navigator, 'geolocation', {
            configurable: true,
            value: {
                getCurrentPosition: function (success, error) {
                    globalThis.__medexGeolocationCalls++;
                    if (mode === 'silent') return;
                    setTimeout(function () {
                        if (mode === 'success') {
                            success({ coords: { latitude: latitude, longitude: longitude, accuracy: accuracy }, timestamp: Date.now() });
                        } else {
                            error({ code: errors[mode][0], message: errors[mode][1] });
                        }
                    }, delayMs);
                }
            }
        });
    })()""",
)

internal fun geolocationCalls(): Int = js("globalThis.__medexGeolocationCalls")

internal fun removeGeolocationStub(): Unit = js("delete navigator.geolocation")

internal fun installPermissionsStub(state: String): Unit = js(
    """Object.defineProperty(navigator, 'permissions', {
        configurable: true,
        value: state === 'missing' ? undefined : { query: function () { return Promise.resolve({ name: 'geolocation', state: state }); } }
    })""",
)

internal fun removePermissionsStub(): Unit = js("delete navigator.permissions")
