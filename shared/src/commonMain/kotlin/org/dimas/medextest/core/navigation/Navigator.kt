package org.dimas.medextest.core.navigation

import androidx.navigation3.runtime.NavKey

interface Navigator {
    fun navigate(key: NavKey)
    fun back()
}
