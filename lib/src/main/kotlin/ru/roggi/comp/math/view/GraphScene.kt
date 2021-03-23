package ru.roggi.comp.math.view

import ru.roggi.comp.math.INPUT_ACCURACY_ROUTE
import ru.roggi.comp.math.INPUT_BOUNDS_ROUTE
import ru.roggi.comp.math.INPUT_EQUATION_ROUTE
import ru.roggi.comp.math.model.Equation
import ru.roggi.comp.math.view.tornado.GraphModel
import ru.roggi.console.application.model.Intent
import ru.roggi.console.application.view.scene.SceneContext
import ru.roggi.console.application.view.scene.StatefulScene


class GraphIntent(
    val accuracy: Double,
    val leftBound: Double,
    val rightBound: Double,
    val equation: Equation
): Intent

/**
 * Consistently reads accuracy, left bound and right bound and equation and draw a graph based on these.
 * If one of this values can be found in sceneContext, than GraphScene will not read them again.
 *
 * @author Roggi
 * @since 1.0
 */
class GraphScene(
    graphModel: GraphModel
): StatefulScene<GraphModel>(graphModel) {
    override fun start(sceneContext: SceneContext, stateReducer: (Intent) -> Unit) {
        if (sceneContext.get("equation") == null) {
            sceneContext.router.switch(INPUT_EQUATION_ROUTE) {
                it as EquationIntent
                state.equation.value = it.equation
            }
        } else {
            if (sceneContext.get("equation") !is Equation) throw IllegalArgumentException("Found illegal equation in sceneContext: should be an instance of Equation")

            state.equation.value = sceneContext.get("equation") as Equation
            sceneContext.remove("equation")
        }

        if (sceneContext.get("leftBound") == null || sceneContext.get("rightBound") == null) {
            sceneContext.router.switch(INPUT_BOUNDS_ROUTE) {
                it as InputTwoIntent<*, *>
                state.leftBound.value = it.a as Double
                state.rightBound.value = it.b as Double
            }
        } else {
            when (val leftBound = sceneContext.get("leftBound")) {
                is Double -> state.leftBound.value = leftBound
                is Int -> state.leftBound.value = leftBound.toDouble()
                else -> throw IllegalArgumentException("Left bound should be a number")
            }

            when (val rightBound = sceneContext.get("rightBound")) {
                is Double -> state.rightBound.value = rightBound
                is Int -> state.rightBound.value = rightBound.toDouble()
                else -> throw IllegalArgumentException("Right bound should be a number")
            }

            sceneContext.remove("leftBound")
            sceneContext.remove("rightBound")
        }

        if (sceneContext.get("accuracy") == null) {
            sceneContext.router.switch(INPUT_ACCURACY_ROUTE) {
                state.accuracy.value = (it as InputIntent<*>).value as Double
            }
        } else {
            val accuracy = when (val temp = sceneContext.get("accuracy")) {
                is Int -> temp.toDouble()
                is Double -> temp
                else -> throw IllegalArgumentException("Accuracy should be a number")
            }

            if (accuracy == 0.0) {
                throw IllegalArgumentException("Accuracy cannot be 0")
            }

            state.accuracy.value = sceneContext.get("accuracy") as Double
            sceneContext.remove("accuracy")
        }

        stateReducer(GraphIntent(state.accuracy.value!!, state.leftBound.value!!, state.rightBound.value!!, state.equation.value!!))
    }

    override fun start(sceneContext: SceneContext) {
        throw RuntimeException("Not available")
    }
}