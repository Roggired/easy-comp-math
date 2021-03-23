package ru.roggi.comp.math.view

import ru.roggi.console.application.model.Intent
import ru.roggi.console.application.view.readFromStdin
import ru.roggi.console.application.view.scene.Scene
import ru.roggi.console.application.view.scene.SceneContext

class InputTwoIntent<T, E>(val a: T, val b: E): Intent

/**
 * Use this scene when you need to invite user to enter two values.
 * These values will be validated and apply to a state via stateReducer
 * If user enters "exit", invokes SceneContext.exit
 *
 * @param greetings This string will be printed before reading
 * @param castT This lambda should convert String to target type. For example, for doubles you can use String::toDouble
 * Lambda should throw NumberFormatException if userInput cannot be cast to T.
 * @param castE This lambda should convert String to target type. For example, for doubles you can use String::toDouble
 * Lambda should throw NumberFormatException if userInput cannot be cast to E.
 * @param validate This lambda should check entered values by it's very values. For example, for doubles place here checks for their ranges.
 * @param endings This string will be printed after reading
 * @param T type of first value to be entered
 * @param E type of second value to be entered
 *
 * @author Roggi
 * @since 1.0
 */
class InputTwoScene<T: Any, E: Any>(
    private val greetings: String,
    private val castT: (userInput: String) -> T,
    private val castE: (userInput: String) -> E,
    private val validate: (t: T, e: E) -> Boolean,
    private val endings: String = "",
): Scene {
    override fun start(sceneContext: SceneContext, stateReducer: (Intent) -> Unit) {
        while (true) {
            val userInput = readFromStdin(greetings)

            if (userInput.toLowerCase() == "exit") {
                sceneContext.exit()
                return
            }

            val parts = userInput.split(Regex(" +"))

            if (parts.size != 2) {
                println("You should enter 2 values")
                continue
            }

            val t: T
            val e: E

            try {
                t = castT(parts[0])
                e = castE(parts[1])
            } catch (e: NumberFormatException) {
                println("One of values has wrong type")
                continue
            }

            if (!validate(t, e)) {
                println("You have entered wrong values")
                continue
            }

            println(endings)
            stateReducer(InputTwoIntent(t, e))
            break
        }
    }

    override fun start(sceneContext: SceneContext) {
        throw RuntimeException("Forbidden to use")
    }
}