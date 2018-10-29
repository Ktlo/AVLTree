@file:Suppress("unused")

package com.handtruth.avltree

fun <K: Comparable<K>, V> avlTreeMapOf() = AVLTreeMap<K, V>()

fun <K: Comparable<K>, V> avlTreeMapOf(vararg pairs: Pair<K, V>) =
        AVLTreeMap<K, V>().apply { pairs.forEach { pair -> put(pair.first, pair.second) } }

fun <K: Any, V> avlTreeMapOf(comparator: Comparator<K>) = AVLTreeMap<K, V>(comparator)

fun <K: Any, V> avlTreeMapOf(vararg pairs: Pair<K, V>, comparator: Comparator<K>) =
        AVLTreeMap<K, V>(comparator).apply { pairs.forEach { pair -> put(pair.first, pair.second) } }

fun <T: Comparable<T>> avlTreeSetOf() = AVLTreeSet<T>()

fun <T: Comparable<T>> avlTreeSetOf(vararg items: T) =
        AVLTreeSet<T>().apply { items.forEach { item -> add(item) } }

fun <T: Any> avlTreeSetOf(comparator: Comparator<T>) = AVLTreeSet(comparator)

fun <T: Any> avlTreeSetOf(vararg items: T, comparator: Comparator<T>) =
        AVLTreeSet(comparator).apply { items.forEach { item -> add(item) } }
