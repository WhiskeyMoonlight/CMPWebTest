package org.dimas.medextest.core.storage

import android.content.Context
import androidx.core.content.edit

class AndroidKeyValueStorage(context: Context) : KeyValueStorage {
    private val preferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)

    override fun getString(key: String): String? = preferences.getString(key, null)

    override fun putString(key: String, value: String) = preferences.edit { putString(key, value) }

    override fun remove(key: String) = preferences.edit { remove(key) }

    private companion object {
        const val PREFERENCES_NAME = "medex"
    }
}
