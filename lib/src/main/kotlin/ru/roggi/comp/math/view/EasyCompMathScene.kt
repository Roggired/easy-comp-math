package ru.roggi.comp.math.view

import ru.roggi.console.application.view.scene.Scene
import ru.roggi.console.application.view.scene.SceneContext

class EasyCompMathScene(
    private val routeToSwitch: String,
    private val version: String
): Scene {
    override fun start(sceneContext: SceneContext) {
        println("Thanks for using EasyCompMath $version!")
        println("EasyCompMath brings you useful tools for buildings computational maths labs easier:")
        println("1. Equation model to parse almost any equations (currently, only exponential functions is not supported).")
        println("2. Common scenes to get values and equations from user.")
        println("3. GraphScene and FileGraphScene to draw graphs with TornadoFX.")
        println("NOTE! EasyCompMath is not a ready lab. You still need to write computations based on your variant.")
        println("Author: Roggi")
        println()

        sceneContext.router.switch(routeToSwitch)
    }
}