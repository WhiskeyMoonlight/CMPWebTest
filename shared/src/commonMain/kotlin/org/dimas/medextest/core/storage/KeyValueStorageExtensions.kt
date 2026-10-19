package org.dimas.medextest.core.storage

import kotlinx.datetime.LocalDate

fun KeyValueStorage.getBoolean(key: String): Boolean = getString(key) == true.toString()

fun KeyValueStorage.putBoolean(key: String, value: Boolean) = putString(key, value.toString())

fun KeyValueStorage.getLocalDate(key: String): LocalDate? =
    getString(key)?.let { runCatching { LocalDate.parse(it) }.getOrNull() }

fun KeyValueStorage.putLocalDate(key: String, value: LocalDate) = putString(key, value.toString())
