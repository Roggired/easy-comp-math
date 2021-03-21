package ru.roggi.comp.math.view

import ru.roggi.comp.math.model.Equation
import ru.roggi.comp.math.model.createEquationFrom
import ru.roggi.console.application.model.Intent
import ru.roggi.console.application.view.readNotNullStringFromStdin
import ru.roggi.console.application.view.scene.Scene
import ru.roggi.console.application.view.scene.SceneContext


class EquationIntent(val equation: Equation): Intent


class EquationScene(
    private val greetings: String,
    private val endings: String,
): Scene {
    override fun start(sceneContext: SceneContext, stateReducer: (Intent) -> Unit) {
        println(greetings)

        var equation: Equation

        while (true) {
            val userInput = readNotNullStringFromStdin()

            try {
                equation = createEquationFrom(userInput)
                stateReducer(EquationIntent(equation))
                println(endings)
                break
            } catch (e: IllegalArgumentException) {
                println("You have entered invalid equation.")
                println("Cause: ${e.message}")
                println("Please, enter again:")
                continue
            }
        }

    }

    override fun start(sceneContext: SceneContext) {
        throw RuntimeException("Not available")
    }
}