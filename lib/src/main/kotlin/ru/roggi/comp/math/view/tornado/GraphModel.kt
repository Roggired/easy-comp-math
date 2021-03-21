package ru.roggi.comp.math.view.tornado

import ru.roggi.comp.math.model.Graph
import ru.roggi.console.application.model.State
import tornadofx.ItemViewModel

class GraphModel: ItemViewModel<Graph>(), State {
    val accuracy = bind(Graph::accuracyProperty)
    val leftBound = bind(Graph::leftBoundProperty)
    val rightBound = bind(Graph::rightBoundProperty)
    val equation = bind(Graph::equationProperty)
}