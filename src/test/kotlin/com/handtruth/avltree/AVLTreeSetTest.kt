package com.handtruth.avltree

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertTrue

class AVLTreeSetTest {
    @Test
    fun `Add and get`() {
        val a = setOf(4, -6532, 89, 3, -5670, 1, 9043, 6,-24, 46, 35, 0)
        val m = avlTreeSetOf(4, -6532, 89, 3, -5670, 1, 9043, 6,-24, 46, 35, 0)
        for (item in a)
            assertTrue(item in m)
        assertEquals(a, m)
        m.remove(1)
        assertNotEquals(a, m)
        assertEquals(setOf(4, -6532, 89, 3, -5670, 9043, 6,-24, 46, 35, 0), m)
    }
    @Test
    fun `Check string representation`() {
        assertEquals(setOf("Kek").toString(), avlTreeSetOf("Kek").toString())
    }
}