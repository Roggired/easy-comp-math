package ru.roggi.comp.math.view

import ru.roggi.comp.math.model.Equation
import ru.roggi.comp.math.model.FileReader
import ru.roggi.comp.math.model.createEquationFrom
import ru.roggi.comp.math.model.emptyEquation
import ru.roggi.comp.math.view.tornado.GraphModel
import ru.roggi.console.application.model.Intent
import ru.roggi.console.application.view.scene.SceneContext
import ru.roggi.console.application.view.scene.StatefulScene
import java.io.FileNotFoundException
import java.io.IOException

class FileGraphIntent(
    equation: Equation,
    leftBound: Double,
    rightBound: Double,
    accuracy: Double,
    val success: Boolean
): GraphIntent(accuracy, leftBound, rightBound, equation)


class FileGraphScene(
    graphModel: GraphModel
): StatefulScene<GraphModel>(graphModel) {
    override fun start(sceneContext: SceneContext) {
        throw RuntimeException("Not available")
    }

    override fun start(sceneContext: SceneContext, stateReducer: (Intent) -> Unit) {
        if (sceneContext.get("fileName") == null) {
            throw IllegalArgumentException("SceneContext should contain name of a file which should be read")
        }

        val fileName = sceneContext.get("fileName") as String
        println("Read file: $fileName")

        val equation: Equation
        val bounds: Pair<Double, Double>
        val accuracy: Double
        try {
            val fileReader = FileReader(fileName)

            equation = createEquationFrom(fileReader.readEquation())
            bounds = fileReader.readBounds()
            accuracy = fileReader.readAccuracy()
            fileReader.close()
        } catch (e: FileNotFoundException) {
            println("Cannot find a file with given name: $fileName")
            stateReducer(FileGraphIntent(emptyEquation(), 0.0, 0.0, 0.01, false))
            sceneContext.remove("fileName")
            return
        } catch (e: IOException) {
            println("Cannot read file with given name: $fileName. Probably, file has wrong structure.")
            stateReducer(FileGraphIntent(emptyEquation(), 0.0, 0.0, 0.01, false))
            sceneContext.remove("fileName")
            return
        } catch (e: IllegalArgumentException) {
            println("Cannot read file with given name: $fileName. Probably, file has wrong structure.")
            stateReducer(FileGraphIntent(emptyEquation(), 0.0, 0.0, 0.01, false))
            sceneContext.remove("fileName")
            return
        }

        state.equation.value = equation
        state.leftBound.value = bounds.first
        state.rightBound.value = bounds.second
        state.accuracy.value = accuracy

        stateReducer(FileGraphIntent(equation, bounds.first, bounds.second, accuracy, true))
        sceneContext.remove("fileName")
    }
}