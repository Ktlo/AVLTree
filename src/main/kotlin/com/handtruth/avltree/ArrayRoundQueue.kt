package com.handtruth.avltree

// Вспомогательный класс не реализующий ни одной коллекции, но смело называющий себя Queue.
// Представляет из себя очередь на основе массива, где элементы не копируются при сдвиге.
// Вместо этого передвигаются указатели на удаляемый и вставляемый элементы.
// Минус этой очереди в том, что она имеет фиксированный размер.
internal class ArrayRoundQueue<T>(private val capacity: Int) {

    private val data = Array<Any?>(capacity) { null }

    private var i = 0
    private var j = 0

    fun put(item: T): Boolean {
        data[j++] = item
        if (j >= capacity)
            j = 0
        return true
    }

    fun pop(): T {
        if (isEmpty)
            throw NoSuchElementException("No elements left!")
        val item = data[i++]
        if (i >= capacity)
            i = 0
        // JVM is <3
        @Suppress("UNCHECKED_CAST")
        return item as T
    }

    fun peek(): T {
        if (isEmpty)
            throw NoSuchElementException("No elements left!")
        // JVM is the best! (Not actually)
        @Suppress("UNCHECKED_CAST")
        return data[i] as T
    }

    val isEmpty get() = i == j

}