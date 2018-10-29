package com.handtruth.avltree

/**
 * AVL binary tree based realisation of [kotlin.collections.Set] interface.
 * This collection uses [AVLTreeMap].
 * @param comparator if keys a not comparable, should be specified
 * @param T the type of values in the Set
 */
class AVLTreeSet<T>(comparator: Comparator<T>? = null) :
        AVLTreeMapKSet<T, Any>(AVLTreeMap<T, Any>(comparator)) where T: Any {
    companion object {
        private val sample = Any()
    }
    override fun add(element: T) = data.put(element, sample) == null
    override fun addAll(elements: Collection<T>) = elements.any { add(it) }
}
