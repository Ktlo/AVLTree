package com.handtruth.avltree.presentation

import com.handtruth.avltree.avlTreeMapOf
import javafx.geometry.Pos
import javafx.scene.Group
import javafx.scene.control.ScrollBar
import tornadofx.*
import java.util.*
import kotlin.math.abs

class PresentationView : View("AVL Tree") {

    private val model = ModifyTreeForm()
    private var map = avlTreeMapOf<Int, String>()

    private val random = Random()

    private var view: Group by singleAssign()

    companion object {
        val words = arrayOf("redeem", "pitch", "flight", "nonremittal", "muscle", "missile", "archive", "native",
                "company", "provision", "artificial", "medicine", "tin", "craft", "flat", "prediction", "quality",
                "censorship", "wagon", "jewel", "greet", "promise", "allow", "fur", "embarrassment", "similar", "net",
                "chest", "survivor", "exact")
    }

    override val root = splitpane {
        prefHeight = 500.0
        prefWidth = 800.0

        setDividerPositions(prefHeight / prefWidth)

        scrollpane(true, true) {
            stackpane {
                vbox(alignment = Pos.CENTER) {
                    imageview("/content/avltree.png")
                    text("Tree").addClass(Styles.treeText)
                }
                view = group {
                    scaleXProperty().bind(ViewSettingsModel.scaleProperty)
                    scaleYProperty().bind(ViewSettingsModel.scaleProperty)
                    scaleZProperty().bind(ViewSettingsModel.scaleProperty)
                }
            }
        }
        vbox(10, Pos.CENTER) {
            form {
                fieldset("Tree modifying") {
                    field("Key") {
                        textfield(model.key).required()
                    }
                    field("Value") {
                        textfield(model.value).required()
                    }
                    buttonbar {
                        button("Insert") {
                            action {
                                model.action.value = ModifyTreeModel.Action.Insert
                                commit()
                            }
                            isDefaultButton = true
                        }
                        button("Random") {
                            action {
                                model.key.value = random.nextInt() % 100_000
                                model.value.value = words[abs(random.nextInt()) % words.size]
                                model.action.value = ModifyTreeModel.Action.Insert
                                commit()
                            }
                        }
                        button("Remove") {
                            action {
                                model.action.value = ModifyTreeModel.Action.Remove
                                commit()
                            }
                        }
                    }
                }
                fieldset("Settings") {
                    field("Scale") {
                        this += ScrollBar().apply {
                            max = 3.0
                            min = 0.01
                            valueProperty().bindBidirectional(ViewSettingsModel.scaleProperty)
                        }
                    }
                }
            }
        }
    }

    private fun commit() {
        model.commit()
        when (ModifyTreeModel.action) {
            ModifyTreeModel.Action.Insert -> map[ModifyTreeModel.key] = ModifyTreeModel.value
            ModifyTreeModel.Action.Remove -> map.remove(ModifyTreeModel.key)
            else -> throw UnsupportedOperationException("How did you?..")
        }
        view.children.clear()
        val node = map.root
        if (node != null) {
            view += TreeElement(map)
        }
    }
}