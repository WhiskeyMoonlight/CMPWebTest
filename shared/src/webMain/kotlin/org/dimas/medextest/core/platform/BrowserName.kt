package org.dimas.medextest.core.platform

import web.navigator.navigator

private val KNOWN_BROWSERS = listOf("Edg/", "Firefox/", "CriOS/", "Chrome/", "Version/")

internal fun browserName(): String {
    val userAgent = navigator.userAgent
    val (start, token) = userAgent.findAnyOf(KNOWN_BROWSERS) ?: return "Browser"
    val version = userAgent.substring(start + token.length).substringBefore(' ')
    val name = when (token) {
        "Edg/" -> "Edge"
        "CriOS/" -> "Chrome iOS"
        "Version/" -> "Safari"
        else -> token.removeSuffix("/")
    }
    return "$name $version"
}
