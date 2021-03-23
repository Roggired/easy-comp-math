package ru.roggi.comp.math.view

import ru.roggi.console.application.model.Intent
import ru.roggi.console.application.view.readFromStdin
import ru.roggi.console.application.view.scene.Scene
import ru.roggi.console.application.view.scene.SceneContext

class InputIntent<T>(val value: T): Intent

/**
 * Use this scene when you need to invite user to enter a value.
 * This value will be validated and applied to a state via stateReducer.
 * If user enters "exit", invokes SceneContext.exit
 *
 * @param greetings will be printed before reading
 * @param cast should convert String to target type. For example, for doubles you can use String::toDouble
 * Lambda should throw NumberFormatException if userInput cannot be cast to T.
 * @param validate should check entered value by it's very value. For example, for doubles place here checks for their ranges.
 * @param endings will be printed after reading
 * @param T type of value to be entered
 *
 * @author Roggi
 * @since 1.0
 */
class InputScene<T: Any>(
    private val greetings: String,
    private val cast: (userInput: String) -> T,
    private val validate: (t: T) -> Boolean,
    private val endings: String = "",
): Scene {
    override fun start(sceneContext: SceneContext, stateReducer: (Intent) -> Unit) {
        while (true) {
            val userInput = readFromStdin(greetings)

            if (userInput.toLowerCase() == "exit") {
                sceneContext.exit()
                return
            }

            val t: T

            try {
                t = cast(userInput)
            } catch (e: NumberFormatException) {
                println("Value has wrong type")
                continue
            }

            when (validate(t)) {
                false -> {
                    println("You have entered wrong value")
                    continue
                }
            }

            println(endings)
            stateReducer(InputIntent(t))
            break
        }
    }

    override fun start(sceneContext: SceneContext) {
        throw RuntimeException("Forbidden to use")
    }
}