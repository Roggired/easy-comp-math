package ru.roggi.comp.math.view

import ru.roggi.console.application.model.Intent
import ru.roggi.console.application.view.scene.Scene
import ru.roggi.console.application.view.scene.SceneContext

class YesNoIntent(
    val isYes: Boolean
): Intent

/**
 * Asking user "yes or no". Greetings with question should be places in sceneContext
 *
 * @author Roggi
 * @since 1.0
 */
class YesNoScene(
    private val endings: String = ""
): Scene {
    override fun start(sceneContext: SceneContext) {
        throw RuntimeException("Not available")
    }

    override fun start(sceneContext: SceneContext, stateReducer: (Intent) -> Unit) {
        if (sceneContext.get("greetings") == null) throw IllegalArgumentException("Need greetings in scene context")

        while (true) {
            println("${sceneContext.get("greetings")} (Y)es, (N)o:")
            val userInput = readLine() ?: continue

            if (userInput.toUpperCase() == "Y" || userInput.toLowerCase() == "yes") {
                stateReducer(YesNoIntent(true))
                break
            }

            if (userInput.toUpperCase() == "N" || userInput.toLowerCase() == "no") {
                stateReducer(YesNoIntent(false))
                break
            }
        }
        sceneContext.remove("greetings")
        println(endings)
    }
}