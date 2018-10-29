package com.handtruth.avltree.presentation

import javafx.scene.paint.Color
import javafx.scene.text.FontWeight
import tornadofx.*

class Styles : Stylesheet() {

    companion object {
        val keyLabel by cssclass()
        val treeText by cssclass()
    }

    init {
        keyLabel {
            fontWeight = FontWeight.EXTRA_BOLD
            fontScale = 5
        }
        treeText {
            fontSize = 80.px
            fill = Color.LIGHTGRAY
        }
    }

}