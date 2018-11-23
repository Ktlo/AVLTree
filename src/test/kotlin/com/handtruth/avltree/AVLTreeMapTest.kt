package com.handtruth.avltree

import org.junit.jupiter.api.Test
import kotlin.test.*

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
    fun `Equality of different realisations`() {
        assertEquals(
                mapOf(1 to 4, 3 to 8, 184 to -5),
                avlTreeMapOf(1 to 4, 184 to -5, 3 to 8)
        )
        assertNotEquals(
                mapOf(5 to "Ohh", -86249 to "Ehh"),
                sample()
        )
        assertEquals(
                mapOf(
                        6 to "Lol",
                        4 to "Kek",
                        2590 to "Cheburek",
                        -39 to "ZOG",
                        7631702 to "Mizen",
                        424 to "Cat",
                        -39101619 to "TaReLkA",
                        -3424 to "The Value..."),
                sample()
        )
        assertEquals(sample().values, sample().apply { remove(2590); put(-8, "Cheburek") }.values)
        assertNotEquals(sample().values, sample().apply { remove(2590); put(-8, "Cheburak") }.values)
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
        m.remove(424, "Cat")
        assertEquals(m, avlTreeMapOf(
                6 to "Lol",
                4 to "Kek",
                2590 to "Cheburek",
                7631702 to "Mizen",
                -39101619 to "TaReLkA",
                -3424 to "The Value..."))
    }

    @Test
    fun `Test "size" property`() {
        val m = sample()
        assertEquals(8, m.size)
        m.remove(2590)
        assertEquals(7, m.size)
        m[9876543] = "QWERTY"
        assertEquals(8, m.size)
        m[-123456789] = "ASDF"
        assertEquals(9, m.size)
        m.clear()
        assertEquals(0, m.size)
    }

    @Test
    fun `"containsKey" test`() {
        val m = sample()

        assertTrue(m.containsKey(6))
        assertTrue(m.containsKey(4))
        assertTrue(m.containsKey(2590))
        assertTrue(m.containsKey(-39))
        assertTrue(m.containsKey(7631702))
        assertTrue(m.containsKey(424))
        assertTrue(m.containsKey(-39101619))
        assertTrue(m.containsKey(-3424))

        assertFalse(m.containsKey(453552398))
        assertFalse(m.containsKey(-342))
    }

    @Test
    fun `"containsValue" test`() {
        val m = sample()

        assertTrue(m.containsValue("Lol"))
        assertTrue(m.containsValue("Kek"))
        assertTrue(m.containsValue("Cheburek"))
        assertTrue(m.containsValue("ZOG"))
        assertTrue(m.containsValue("Mizen"))
        assertTrue(m.containsValue("Cat"))
        assertTrue(m.containsValue("TaReLkA"))
        assertTrue(m.containsValue("The Value..."))

        assertFalse(m.containsValue("Meaning of Life"))
        assertFalse(m.containsValue("Bugs"))
    }

    @Test
    fun `Get sample values`() {
        val m = sample()

        assertEquals("Lol", m[6])
        assertEquals("Cheburek", m[2590])
        assertEquals("TaReLkA", m[-39101619])
        assertEquals("The Value...", m[-3424])
        assertEquals("ZOG", m[-39])
        assertEquals("Kek", m[4])
        assertEquals("Mizen", m[7631702])

        assertNull(m[0])
        assertNull(m[-999999999])
        assertNull(m[999999999])
        assertNull(m[4260447])
    }

    @Test
    fun `Void check`() {
        assertTrue(avlTreeMapOf<String, Any>().isEmpty())
        val m = sample()

        assertFalse(m.isEmpty())
        m.remove(6)
        m.remove(4)
        m.remove(2590)
        m.remove(-39)
        m.remove(7631702)
        assertFalse(m.isEmpty())
        m.remove(424)
        m.remove(-39101619)
        m.remove(-3424)
        assertTrue(m.isEmpty())

        assertTrue(sample().apply { clear() }.isEmpty())
    }

    @Test
    fun `Put some values`() {
        val m = sample()
        m[25] = "The Value..."
        assertTrue(m.containsKey(25))
        assertTrue(m.containsValue("The Value..."))
        assertEquals(m.size, 9)
        assertEquals(mapOf(
                6 to "Lol",
                4 to "Kek",
                2590 to "Cheburek",
                -39 to "ZOG",
                7631702 to "Mizen",
                424 to "Cat",
                -39101619 to "TaReLkA",
                -3424 to "The Value...",
                25 to "The Value..."), m)
    }

    @Test
    fun `String representation`() {
        val m = avlTreeMapOf("A" to "B")
        assertEquals(mapOf("A" to "B").toString(), m.toString())
    }

    @Test
    fun `Hash equality`() {
        val m = sample()
        assertEquals(mapOf(
                6 to "Lol",
                4 to "Kek",
                2590 to "Cheburek",
                -39 to "ZOG",
                7631702 to "Mizen",
                424 to "Cat",
                -39101619 to "TaReLkA",
                -3424 to "The Value..."
        ).hashCode(), m.hashCode())
        m.remove(2590)
        assertEquals(mapOf(
                6 to "Lol",
                4 to "Kek",
                -39 to "ZOG",
                7631702 to "Mizen",
                424 to "Cat",
                -39101619 to "TaReLkA",
                -3424 to "The Value..."
        ).hashCode(), m.hashCode())

        assertEquals(sample().values.hashCode(), sample().apply {
            remove(2590)
            put(-8, "Cheburek")
        }.values.hashCode())
        assertNotEquals(sample().values.hashCode(), sample().apply {
            remove(2590)
            put(-8, "Cheburak")
        }.values.hashCode())
    }

}
