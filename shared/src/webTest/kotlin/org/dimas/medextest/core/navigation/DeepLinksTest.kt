package org.dimas.medextest.core.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

@Serializable
private data object CatalogKey : NavKey

@Serializable
private data class ItemKey(val itemId: String) : NavKey

@Serializable
private data object UnlinkedKey : NavKey

class DeepLinksTest {
    private val deepLinks = DeepLinks(
        listOf(
            deepLink<CatalogKey>("catalog"),
            deepLink<ItemKey>(
                path = "catalog/item/{itemId}",
                backStack = { listOf(CatalogKey, it) },
                pathOf = { "catalog/item/${it.itemId}" },
            ),
        ),
    )

    @Test
    fun static_path_parses_to_its_key() {
        assertEquals(listOf(CatalogKey), deepLinks.parse("catalog"))
    }

    @Test
    fun placeholder_is_decoded_into_the_key_and_back_stack_is_built() {
        assertEquals(listOf(CatalogKey, ItemKey("lab-tests")), deepLinks.parse("catalog/item/lab-tests"))
    }

    @Test
    fun surrounding_slashes_are_tolerated() {
        assertEquals(listOf(CatalogKey), deepLinks.parse("/catalog/"))
    }

    @Test
    fun unknown_paths_are_ignored() {
        listOf("", "/", "unknown", "catalog/unknown", "catalog/item").forEach { path ->
            assertNull(deepLinks.parse(path), "expected null for '$path'")
        }
    }

    @Test
    fun path_of_a_key_uses_its_own_link() {
        assertEquals("catalog", deepLinks.pathOf(CatalogKey))
        assertEquals("catalog/item/abc", deepLinks.pathOf(ItemKey("abc")))
        assertNull(deepLinks.pathOf(UnlinkedKey))
    }
}
