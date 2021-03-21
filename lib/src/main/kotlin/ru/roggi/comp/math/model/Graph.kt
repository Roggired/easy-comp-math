package ru.roggi.comp.math.model

import javafx.beans.property.SimpleDoubleProperty
import javafx.beans.property.SimpleObjectProperty
import tornadofx.*

class Graph {
    val accuracyProperty = SimpleDoubleProperty(this, "accuracy", 0.1)
    var accuracy by accuracyProperty

    val leftBoundProperty = SimpleDoubleProperty(this, "leftBound", -5.0)
    var leftBound by leftBoundProperty

    val rightBoundProperty = SimpleDoubleProperty(this, "rightBound", 5.0)
    var rightBound by rightBoundProperty

    val equationProperty =  SimpleObjectProperty<Equation>(this, "equation", null)
    var equation by equationProperty
}