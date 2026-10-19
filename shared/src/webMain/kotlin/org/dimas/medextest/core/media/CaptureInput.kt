package org.dimas.medextest.core.media

import web.dom.document
import web.events.Event
import web.events.EventHandler
import web.events.EventType
import web.events.addEventListener
import web.file.File
import web.html.HtmlTagName
import web.html.InputType
import web.html.file

internal fun openCaptureInput(onFile: (File?) -> Unit) {
    val input = document.createElement(HtmlTagName.input)
    input.type = InputType.file
    input.accept = "image/*"
    input.setAttribute("capture", "environment")
    input.setAttribute("hidden", "")

    var settled = false
    fun settle(file: File?) {
        if (settled) return
        settled = true
        input.remove()
        onFile(file)
    }

    input.addEventListener(EventType<Event>("change"), EventHandler { settle(input.files?.item(0)) })
    input.addEventListener(EventType<Event>("cancel"), EventHandler { settle(null) })
    document.body.appendChild(input)
    input.click()
}
