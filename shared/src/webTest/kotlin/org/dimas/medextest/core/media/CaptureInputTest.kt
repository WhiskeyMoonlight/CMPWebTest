package org.dimas.medextest.core.media

import org.dimas.medextest.testutil.captureInputAttribute
import org.dimas.medextest.testutil.captureInputCount
import org.dimas.medextest.testutil.dispatchOnCaptureInput
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

class CaptureInputTest {
    @Test
    fun camera_input_asks_for_rear_camera_image() {
        openCaptureInput { }

        assertEquals(1, captureInputCount())
        assertEquals("image/*", captureInputAttribute("accept"))
        assertEquals("environment", captureInputAttribute("capture"))
        assertEquals("", captureInputAttribute("hidden"))
        dispatchOnCaptureInput("cancel")
    }

    @Test
    fun cancel_settles_once_with_null_and_removes_input() {
        val results = mutableListOf<Any?>()
        openCaptureInput { results += it }

        dispatchOnCaptureInput("cancel")

        assertEquals(listOf<Any?>(null), results)
        assertEquals(0, captureInputCount())
    }

    @Test
    fun change_without_files_settles_with_null() {
        var settled = false
        var file: Any? = "not settled"
        openCaptureInput { picked ->
            settled = true
            file = picked
        }

        dispatchOnCaptureInput("change")

        assertTrue(settled)
        assertNull(file)
        assertEquals(0, captureInputCount())
    }
}
