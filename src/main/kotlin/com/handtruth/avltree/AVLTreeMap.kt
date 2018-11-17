package com.handtruth.avltree

import java.util.*
import kotlin.math.log2

/**
 * AVL binary tree based realisation of the [kotlin.collections.Map] interface.
 * @param K a type of keys values
 * @param V a type of [kotlin.collections.Map] values
 * @param comparator if keys a not comparable, should be specified
 */
class AVLTreeMap<K, V>(private val comparator: Comparator<K>? = null) :
        MutableMap<K, V>
        where K: Any {

    private var count = 0

    override val size get() = count

    // Корень дерева
    internal var root: Node<K, V>? = null

    override fun containsKey(key: K) = root.find(key, comparator) != null

    override fun containsValue(value: V): Boolean {
        var result = false
        root?.scan {
            if(it.value == value) {
                result = true
                false
            } else true
        }
        return result
    }

    override fun get(key: K) = root.find(key, comparator)

    override fun isEmpty(): Boolean = root == null

    override val entries: MutableSet<MutableMap.MutableEntry<K, V>> = AVLTreeMapKVSet(this)
    override val keys: MutableSet<K> = AVLTreeMapKSet(this)
    override val values: MutableCollection<V> = AVLTreeMapVCollection(this)

    override fun clear() {
        count = 0
        root = null
    }

    override fun put(key: K, value: V): V? {
        value ?: return remove(key)
        val old = Ref<V?>(null)
        root = root.insert(key to value, old, comparator)
        if (old.value == null)
            count++
        return old.value
    }

    override fun putAll(from: Map<out K, V>) {
        from.forEach { put(it.key, it.value) }
    }

    private fun internalRemove(key: K, value: V?): V? {
        val old = Ref<V?>(null)
        root = root.remove(key to value, old, comparator)
        if (old.value != null)
            count--
        return old.value
    }

    override fun remove(key: K) = internalRemove(key, null)

    override fun remove(key: K, value: V) = internalRemove(key, value) != null

    override fun toString() = entries.joinToString(prefix = "{", postfix = "}")

    override fun equals(other: Any?) = other is Map<*, *> && entries == other.entries

    override fun hashCode() = entries.sumBy { it.hashCode() }
}

private class AVLTreeMapIterator<K, V>(data: AVLTreeMap<K, V>) :
        MutableIterator<MutableMap.MutableEntry<K, V>>
        where K: Any {

    private val queue = ArrayDeque<Node<K, V>>((1.44 * log2((data.size + 2).toDouble())).toInt() + 1)

    init {
        val root = data.root
        root != null && queue.add(root)
    }

    override fun hasNext() = queue.isNotEmpty()

    override fun next(): MutableMap.MutableEntry<K, V> {
        val node = queue.pop()
        val r = node.right
        val l = node.left
        r != null && queue.add(r)
        l != null && queue.add(l)
        return node
    }

    override fun remove() = throw UnsupportedOperationException()

}

abstract class AVLTreeMapCollectionBase<T> : MutableCollection<T> {
    abstract val data: AVLTreeMap<*, *>
    override fun add(element: T): Boolean = throw UnsupportedOperationException()
    override val size get() = data.size
    override fun clear() = data.clear()
    override fun isEmpty() = data.isEmpty()
    override fun addAll(elements: Collection<T>): Boolean = throw UnsupportedOperationException()
    override fun removeAll(elements: Collection<T>) = elements.any { remove(it) }
    override fun containsAll(elements: Collection<T>) = elements.all { contains(it) }
    override fun toString() = joinToString(prefix = "[", postfix = "]")
    override fun hashCode() = sumBy { it?.hashCode() ?: 0 }
    override fun equals(other: Any?) = other is Collection<*> && data.size == other.size && all { it in other }
}

@Suppress("EqualsOrHashCode")
private class AVLTreeMapKVSet<K, V>(override val data: AVLTreeMap<K, V>) :
        AVLTreeMapCollectionBase<MutableMap.MutableEntry<K, V>>(), MutableSet<MutableMap.MutableEntry<K, V>>
        where K: Any {
    override fun iterator() = AVLTreeMapIterator(data)

    override fun remove(element: MutableMap.MutableEntry<K, V>) = data.remove(element.key, element.value)

    override fun retainAll(elements: Collection<MutableMap.MutableEntry<K, V>>): Boolean {
        var result = false
        val marked = mutableListOf<K>()
        for (node in AVLTreeMapIterator(data)) {
            if (node !in elements) {
                marked += node.key
                result = true
            }
        }
        for (key in marked)
            data.remove(key)
        return result
    }

    override fun equals(other: Any?): Boolean = other is Set<*> && super.equals(other)

    override fun contains(element: MutableMap.MutableEntry<K, V>) = data[element.key] == element.value
}

@Suppress("EqualsOrHashCode")
open class AVLTreeMapKSet<K, V>(override val data: AVLTreeMap<K, V>) :
        AVLTreeMapCollectionBase<K>(), MutableSet<K>
        where K: Any {
    override fun iterator(): MutableIterator<K> = KeyIterator(AVLTreeMapIterator(data))

    override fun remove(element: K) = data.remove(element) != null

    override fun retainAll(elements: Collection<K>): Boolean {
        var result = false
        val marked = mutableListOf<K>()
        for (node in AVLTreeMapIterator(data)) {
            if (elements.any { it != node.key }) {
                marked += node.key
                result = true
            }
        }
        for (key in marked)
            data.remove(key)
        return result
    }

    override fun contains(element: K) = data.containsKey(element)

    private class KeyIterator<K>(private val iterator: MutableIterator<MutableMap.MutableEntry<K, *>>):
            MutableIterator<K> {
        override fun hasNext() = iterator.hasNext()
        override fun next() = iterator.next().key
        override fun remove() = iterator.remove()
    }

    override fun equals(other: Any?) = other is Set<*> && super.equals(other)
}

private class AVLTreeMapVCollection<V>(override val data: AVLTreeMap<*, V>) : AVLTreeMapCollectionBase<V>() {
    override fun contains(element: V) = data.containsValue(element)

    override fun iterator(): MutableIterator<V> = ValueIterator(AVLTreeMapIterator(data))

    override fun remove(element: V): Boolean {
        for (node in AVLTreeMapIterator(data)) {
            if (node.value == element) {
                data.remove(node.key)
                return true
            }
        }
        return false
    }

    override fun retainAll(elements: Collection<V>): Boolean {
        val items = elements.asArray()
        var countDown = items.size
        var result = false
        val marked = mutableListOf<Any>()
        for (node in AVLTreeMapIterator(data)) {
            val i = items.indexOf(node.value)
            if (i < 0) {
                marked.add(node.key)
                result = true
            } else {
                items[i] = null
                if (--countDown == 0)
                    break
            }
        }
        for (key in marked)
            data.remove(key)
        return result
    }

    private class ValueIterator<V>(private val iterator: MutableIterator<MutableMap.MutableEntry<*, V>>):
            MutableIterator<V> {
        override fun hasNext() = iterator.hasNext()
        override fun next() = iterator.next().value
        override fun remove() = iterator.remove()
    }
}

// Узел дерева
internal class Node<K, V>(override val key: K,                       // Ключ
                          override var value: V,                     // Значение
                          @JvmField var h: Byte = 0,                 // Высота после этого узла
                          @JvmField var left: Node<K, V>? = null,    // Узел слева
                          @JvmField var right: Node<K, V>? = null) : // Узел справа
        MutableMap.MutableEntry<K, V> where K: Any {

    override fun setValue(newValue: V): V {
        val tmp = value
        value = newValue
        return tmp
    }

    // Расхождение высоты дерева (используется для балансировки)
    private val factor get() = right.height - left.height

    private fun fixHeight() {
        val hl = left.height
        val hr = right.height
        h = (max(hl, hr) + 1).toByte()
    }

    private fun rotateRight(): Node<K, V>? {
        val tmp = left!!
        left = tmp.right
        tmp.right = this
        fixHeight()
        tmp.fixHeight()
        return tmp
    }

    private fun rotateLeft(): Node<K, V>? {
        val tmp = right!!
        right = tmp.left
        tmp.left = this
        fixHeight()
        tmp.fixHeight()
        return tmp
    }

    fun balance(): Node<K, V>? {
        fixHeight()
        return when (factor) {
            2 -> {
                val r = right!!
                if (r.factor < 0)
                    right = r.rotateRight()
                rotateLeft()
            }
            -2 -> {
                val l = left!!
                if (l.factor > 0)
                    left = l.rotateLeft()
                rotateRight()
            }
            else -> this
        }
    }

    fun findMin(): Node<K, V> {
        var node = this
        var next = this.left
        while (next != null) {
            node = next
            next = node.left
        }
        return node
    }

    fun removeMin(): Node<K, V>? {
        val l = left ?: return right
        left = l.removeMin()
        return balance()
    }

    // Рекурсивный обход дерева, начиная с этого узла, как с коренного
    fun scan(action: (Node<K, V>) -> Boolean) {
        if (action(this)) {
            left?.scan(action)
            right?.scan(action)
        }
    }

    override fun equals(other: Any?) =
            other is Map.Entry<*, *> && key == other.key && value == other.value

    override fun hashCode() = key.hashCode() xor (value?.hashCode() ?: 0)

    override fun toString() = "$key=$value"
}

// Для отсутствующей ветви возвращает высоту = 0
private val <K: Any, V> Node<K, V>?.height: Byte get() = this?.h ?: 0

// Может оказаться такая ситуация, что "a" не реализует Comparable<T>, а например Comparable<KEK>.
// Я не знаю что с этим делать, к сожалению.
@Suppress("UNCHECKED_CAST")
private fun <T> compare(a: T, b: T, comparator: Comparator<T>?): Int =
        (comparator?.compare(a, b) ?: (a as Comparable<T>).compareTo(b))

private fun <K: Any, V> Node<K, V>?.insert(item: Pair<K, V>,            // Вставляемая пара значений
                                           old: Ref<V?>,                // Ссылка, в которую записывается старое
                                                                        // значение, если есть, иначе null.
                                           comparator: Comparator<K>?): // Сравнивает ключи дерева
        Node<K, V>? {
    this ?: return Node(item.first, item.second)
    val test = compare(item.first, key, comparator)
    when {
        test < 0 -> left = left.insert(item, old, comparator)
        test > 0 -> right = right.insert(item, old, comparator)
        else -> {
            old.value = value
            value = item.second
            return this
        }
    }
    return balance()
}

private fun <K: Any, V> Node<K, V>?.remove(item: Pair<K, V?>,           // Удаляемая пара значений. Если
                                                                        // item.second == null то удаляет только по ключу
                                           old: Ref<V?>,                // Старое значение, если удаление было сделано
                                           comparator: Comparator<K>?): // Сравнивает ключи дерева
        Node<K, V>? {
    this ?: return null
    val test = compare(item.first, key, comparator)
    when {
        test < 0 -> left = left.remove(item, old, comparator)
        test > 0 -> right = right.remove(item, old, comparator)
        else -> {
            if (item.second != null && item.second != value)
                return this
            val l = left
            val r = right
            old.value = this.value
            r ?: return l
            val min = r.findMin()
            min.right = r.removeMin()
            min.left = l
            return min.balance()
        }
    }
    return balance()
}

private fun <K: Any, V> Node<K, V>?.find(itemKey: K, comparator: Comparator<K>?): V? {
    this ?: return null
    val test = compare(itemKey, key, comparator)
    return when {
        test < 0 -> left.find(itemKey, comparator)
        test > 0 -> right.find(itemKey, comparator)
        else -> value
    }
}

private class Ref<T>(var value: T)

private fun max(a: Byte, b: Byte) = if (a > b) a else b

private fun <T> Collection<T>.asArray(): Array<Any?> {
    val arr = Array<Any?>(size) {null}
    for ((i, item) in this.withIndex()) {
        arr[i] = item
    }
    return arr
}
