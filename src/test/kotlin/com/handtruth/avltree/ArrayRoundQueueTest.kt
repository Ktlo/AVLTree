package com.handtruth.avltree

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ArrayRoundQueueTest {

    @Test fun `Test common queue functionality`() {
        val q = ArrayRoundQueue<String>(10)
        q.put("Lol")
        q.put("Kek")
        q.put("Cheburek")
        assertEquals(q.peek(), "Lol")
        assertEquals(q.pop(), "Lol")
        assertEquals(q.pop(), "Kek")
        q.put("https://mc.handtruth.com")
        assertFalse(q.isEmpty)
        assertEquals(q.pop(), "Cheburek")
        assertEquals(q.peek(), "https://mc.handtruth.com")
        assertEquals(q.pop(), "https://mc.handtruth.com")
        assertTrue { q.isEmpty }
        assertFailsWith(NoSuchElementException::class) { q.peek() }
    }

    @Test fun `Fill up the queue`() {
        val q = ArrayRoundQueue<String>(6)
        q.put("Lol")
        q.put("Kek")
        q.put("Cheburek")
        q.put("Popka")
        q.put("Vodka")
        assertEquals(q.peek(), "Lol")
        assertEquals(q.pop(), "Lol")
        assertEquals(q.pop(), "Kek")
        assertEquals(q.pop(), "Cheburek")
        assertEquals(q.pop(), "Popka")
        assertEquals(q.pop(), "Vodka")
        assertTrue { q.isEmpty }
        assertFailsWith(NoSuchElementException::class) { q.pop() }
    }

}