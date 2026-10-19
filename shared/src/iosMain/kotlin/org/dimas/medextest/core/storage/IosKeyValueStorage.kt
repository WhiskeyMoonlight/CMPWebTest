package org.dimas.medextest.core.storage

import platform.Foundation.NSUserDefaults

class IosKeyValueStorage(
    private val defaults: NSUserDefaults = NSUserDefaults.standardUserDefaults,
) : KeyValueStorage {
    override fun getString(key: String): String? = defaults.stringForKey(key)

    override fun putString(key: String, value: String) = defaults.setObject(value, forKey = key)

    override fun remove(key: String) = defaults.removeObjectForKey(key)
}
