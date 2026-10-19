package org.dimas.medextest.core.storage

import web.storage.localStorage

class BrowserKeyValueStorage : KeyValueStorage {
    override fun getString(key: String): String? = runCatching { localStorage.getItem(key) }.getOrNull()

    override fun putString(key: String, value: String) {
        runCatching { localStorage.setItem(key, value) }
    }

    override fun remove(key: String) {
        runCatching { localStorage.removeItem(key) }
    }
}
