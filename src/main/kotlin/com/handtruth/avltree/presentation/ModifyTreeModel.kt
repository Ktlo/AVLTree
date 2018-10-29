package com.handtruth.avltree.presentation

import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.SimpleStringProperty
import tornadofx.*

object ModifyTreeModel {
    enum class Action { None, Insert, Remove }

    val keyProperty = SimpleIntegerProperty(42)
    var key: Int by keyProperty

    val valueProperty = SimpleStringProperty("value")
    var value: String by valueProperty

    val actionProperty = SimpleObjectProperty<Action>(Action.None)
    var action: Action by actionProperty
}

class ModifyTreeForm : ViewModel() {
    val key = bind { ModifyTreeModel.keyProperty }
    val value = bind { ModifyTreeModel.valueProperty }
    val action = bind { ModifyTreeModel.actionProperty }
}
