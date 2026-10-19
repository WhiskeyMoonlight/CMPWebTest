package org.dimas.medextest.core.designsystem.adaptive

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class WindowWidthClassTest {
    @Test
    fun width_maps_to_material_breakpoints() {
        assertEquals(WindowWidthClass.Compact, WindowWidthClass.fromWidth(599.dp))
        assertEquals(WindowWidthClass.Medium, WindowWidthClass.fromWidth(600.dp))
        assertEquals(WindowWidthClass.Medium, WindowWidthClass.fromWidth(839.dp))
        assertEquals(WindowWidthClass.Expanded, WindowWidthClass.fromWidth(840.dp))
    }

    @Test
    fun only_compact_is_narrow() {
        assertFalse(WindowWidthClass.Compact.isWide)
        assertTrue(WindowWidthClass.Medium.isWide)
        assertTrue(WindowWidthClass.Expanded.isWide)
    }

    @Test
    fun content_is_limited_only_on_wide_screens() {
        assertEquals(Dp.Infinity, WindowWidthClass.Compact.contentMaxWidth)
        assertEquals(720.dp, WindowWidthClass.Medium.contentMaxWidth)
        assertEquals(1080.dp, WindowWidthClass.Expanded.contentMaxWidth)
    }
}
