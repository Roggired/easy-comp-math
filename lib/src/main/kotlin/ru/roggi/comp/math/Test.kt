package ru.roggi.comp.math

import ru.roggi.comp.math.model.Equation
import ru.roggi.comp.math.model.createEquationFrom
import ru.roggi.comp.math.model.emptyEquation
import ru.roggi.comp.math.utils.RouterBuilder
import ru.roggi.comp.math.view.GraphIntent
import ru.roggi.comp.math.view.presenter.Presenter
import ru.roggi.console.application.model.State
import ru.roggi.console.application.view.scene.Router
import ru.roggi.console.application.view.scene.SceneContext
import ru.roggi.console.application.view.scene.StatefulScene
import tornadofx.launch

class HalfDivisionMethodState: State {
    var accuracy: Double = 0.0
    var leftBound: Double = 0.0
    var rightBound: Double = 0.0
    var equation: Equation = emptyEquation()
}

class HalfDivisionMethodScene: StatefulScene<HalfDivisionMethodState>(HalfDivisionMethodState()) {
    override fun start(sceneContext: SceneContext) {
//        sceneContext.put("fileName", "test.txt")
        sceneContext.put("accuracy", 0.01)
        sceneContext.put("leftBound", 2)
        sceneContext.put("rightBound", 5)
        sceneContext.put("equation", createEquationFrom("x^2 + sin(x)"))

        sceneContext.router.switch(GRAPH_ROUTE) {
            it as GraphIntent
            state.accuracy = it.accuracy
            state.leftBound = it.leftBound
            state.rightBound = it.rightBound
            state.equation = it.equation
        }
        println("Entered: ${state.accuracy} ${state.leftBound} ${state.rightBound}")
        println("Entered equation: ${Presenter.present(state.equation)}")
        println("Value for leftBound: ${state.equation.evaluate(state.leftBound)}")
    }
}

fun main() {
    launch<EasyCompMath>("main", "ru.roggi.comp.math")
}

class RouterBuilderTest: RouterBuilder {
    override fun build(): Router =
        Router()
        .apply {
            register("main", HalfDivisionMethodScene())
        }
}