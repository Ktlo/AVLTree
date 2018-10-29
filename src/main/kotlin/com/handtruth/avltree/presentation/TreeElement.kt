package com.handtruth.avltree.presentation

import com.handtruth.avltree.AVLTreeMap
import com.handtruth.avltree.Node
import javafx.scene.Group
import javafx.scene.paint.Color
import tornadofx.*
import kotlin.math.log2
import kotlin.math.pow

class TreeElement private constructor(private val node: Node<Int, String>,
                  xOffset: Int, yOffset: Int,
                  depth: Double) : Group() {

    constructor(map: AVLTreeMap<Int, String>) : this(map.root!!, 0, 0, countDepth(map))

    companion object {
        private const val lineWidth = 5.0
        private const val radius = 40

        /*
            0b00000000_00000000_00000000
            R=  r  r  _r  r  r _ r  r  r
            G= g  g  g_  g  g  _g  g  g
            B=b  b  b _ b  b  b_  b  b
         */
        private fun calculateColor(color: Int): Color {
            var r = 0
            var g = 0
            var b = 0
            var c = color
            for (i in 0..23) {
                val t = c and 1
                when (i % 3) {
                    0 -> r = (r shl 1) or t
                    1 -> g = (g shl 1) or t
                    2 -> b = (b shl 1) or t
                }
                c = c ushr 1
            }
            return Color.rgb(r, g, b)
        }

        private fun countDepth(map: Map<Int, String>) = log2(map.size.toDouble() + 2) - 1.0

    }

    private var left: TreeElement? = null
    private var right: TreeElement? = null

    init {
        layoutX = xOffset.toDouble()
        layoutY = yOffset.toDouble()

        val l = node.left
        val r = node.right
        val x = radius * 2.0.pow(depth).toInt()
        val y = 2 * radius
        if (l != null) {
            line(-x, y, 0, 0) {
                fill = Color.AQUA
                strokeWidth = lineWidth
            }
            val leftElement = TreeElement(l, -x, y, depth - 1)
            this += leftElement
            left = leftElement
        }
        if (r != null) {
            line(x, y, 0, 0) {
                fill = Color.AQUA
                strokeWidth = lineWidth
            }
            val rightElement = TreeElement(r, x, y, depth - 1)
            this += rightElement
            right = rightElement
        }
        circle(0, 0, radius) {
            fill = calculateColor(node.key)
        }
        text(node.key.toString()) {
            addClass(Styles.keyLabel)
            layoutX = -layoutBounds.width / 2
            layoutY = -20.0
            fill = Color.WHITE
        }
        text(node.value) {
            layoutX = -layoutBounds.width / 2
            layoutY = +10.0
            fill = Color.WHITE
        }
    }

}