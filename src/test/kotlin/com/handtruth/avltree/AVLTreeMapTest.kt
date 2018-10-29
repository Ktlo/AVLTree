package com.handtruth.avltree

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class AVLTreeMapTest {

    private fun sample() = avlTreeMapOf(
            6 to "Lol",
            4 to "Kek",
            2590 to "Cheburek",
            -39 to "ZOG",
            7631702 to "Mizen",
            424 to "Cat",
            -39101619 to "TaReLkA",
            -3424 to "The Value...")

    @Test fun `Test iterating over elements`() {
        val m = sample()
        val k = listOf(6, 2590, -39, 7631702, 424, 4, -39101619, -3424)
        val v = listOf("Lol", "Cheburek", "ZOG", "Mizen", "Cat", "Kek", "TaReLkA", "The Value...")
        for ((i, pair) in m.iterator().withIndex()) {
            assertEquals(pair.key, k[i])
            assertEquals(pair.value, v[i])
        }
        for ((i, key) in m.keys.withIndex()) {
            assertEquals(key, k[i])
        }
        for ((i, value) in m.values.withIndex()) {
            assertEquals(value, v[i])
        }
    }

    @Test
    fun `Remove an element`() {
        val m = sample()
        m.remove(-39)
        assertEquals(m, avlTreeMapOf(
                6 to "Lol",
                4 to "Kek",
                2590 to "Cheburek",
                7631702 to "Mizen",
                424 to "Cat",
                -39101619 to "TaReLkA",
                -3424 to "The Value..."))
    }

}