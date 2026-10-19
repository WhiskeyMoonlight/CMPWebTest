package org.dimas.medextest.testutil

import web.file.File
import kotlin.js.js

internal fun textFile(content: String, name: String, type: String): File = js("new File([content], name, { type: type })")

internal fun captureInputCount(): Int = js("document.querySelectorAll('input[type=file][capture]').length")

internal fun captureInputAttribute(name: String): String? = js(
    "(function () { var input = document.querySelector('input[type=file][capture]'); return input ? input.getAttribute(name) : null; })()",
)

internal fun dispatchOnCaptureInput(type: String): Unit = js(
    "document.querySelector('input[type=file][capture]').dispatchEvent(new Event(type))",
)
