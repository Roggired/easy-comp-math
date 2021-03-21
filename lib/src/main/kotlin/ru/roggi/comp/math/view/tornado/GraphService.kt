package ru.roggi.comp.math.view.tornado

import javafx.collections.ObservableList
import javafx.scene.chart.XYChart
import ru.roggi.comp.math.model.Equation
import tornadofx.Controller
import tornadofx.toObservable

class GraphService : Controller() {
    fun getPlotMeta(
        equation: Equation?,
        from: Double,
        to: Double,
        step: Double
    ): ObservableList<XYChart.Data<Double, Double>>? {
        if (step == 0.0) return mutableListOf<XYChart.Data<Double, Double>>().toObservable()
        if (equation == null) return mutableListOf<XYChart.Data<Double, Double>>().toObservable()

        val dots: MutableMap<Double, Double> = linkedMapOf()

        var fromCopy = from
        while (fromCopy <= to) {
            dots[fromCopy] = equation.evaluate(fromCopy)
            fromCopy += step
        }

        return dots.toList().map { pair: Pair<Double, Double> ->
            XYChart.Data(pair.first, pair.second)
        }.toObservable()
    }

    companion object {
        var graphModel: GraphModel? = null

        var show: (() -> Unit)? = null

        var hide: (() -> Unit)? = null
    }
}
