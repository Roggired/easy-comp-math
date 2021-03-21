package ru.roggi.comp.math.view.tornado

import javafx.application.Platform
import javafx.beans.property.Property
import javafx.beans.property.SimpleListProperty
import javafx.collections.ObservableList
import javafx.scene.Cursor
import javafx.scene.chart.NumberAxis
import javafx.scene.chart.XYChart
import ru.roggi.comp.math.model.Equation
import tornadofx.*

class GraphView : View("EasyCompMath") {
    private val graphService: GraphService by inject()
    private val graphModel: GraphModel by inject()
    private val currentGraph: Property<ObservableList<XYChart.Data<Number, Number>>> = SimpleListProperty()

    init {
        GraphService.graphModel = graphModel
        GraphService.show = { Platform.runLater { primaryStage.show() } }
        GraphService.hide = { Platform.runLater { primaryStage.hide() } }

        currentGraph.value =
            graphService.getPlotMeta(
                graphModel.equation.value,
                graphModel.leftBound.value,
                graphModel.rightBound.value,
                graphModel.accuracy.value
            ) as? ObservableList<XYChart.Data<Number, Number>>?

        graphModel.apply {
            equation.onChange {
                redraw(it, leftBound.value, rightBound.value, accuracy.value)
            }
            leftBound.onChange {
                redraw(equation.value, it, rightBound.value, accuracy.value)
            }
            rightBound.onChange {
                redraw(equation.value, leftBound.value, it, accuracy.value)
            }
            accuracy.onChange {
                redraw(equation.value, leftBound.value, rightBound.value, it)
            }
        }
    }

    override val root = vbox {
        linechart(
            "Graph",
            NumberAxis(),
            NumberAxis()) {
            isLegendVisible = false
            cursor = Cursor.CROSSHAIR

            prefWidth = RootStyles.PREF_WIDTH.toDouble()
            prefHeight = RootStyles.PREF_HEIGHT.toDouble()

            series("Graph", elements = currentGraph.value)
        }
    }

    private fun redraw(equation: Equation?, left: Double, right: Double, accuracy: Double) {
        Platform.runLater {
            currentGraph.value.clear()
            for (value in graphService.getPlotMeta(equation, left, right, accuracy)!!) {
                currentGraph.value.add(value as XYChart.Data<Number, Number>)
            }
        }
    }
}
