package ru.roggi.comp.math.view

import ru.roggi.comp.math.model.FileReader
import ru.roggi.comp.math.model.createEquationFrom
import ru.roggi.comp.math.view.tornado.GraphModel
import ru.roggi.console.application.model.Intent
import ru.roggi.console.application.view.scene.SceneContext
import ru.roggi.console.application.view.scene.StatefulScene

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

        val fileReader = FileReader(fileName)

        val equation = createEquationFrom(fileReader.readEquation())
        val bounds = fileReader.readBounds()
        val accuracy = fileReader.readAccuracy()
        fileReader.close()

        state.equation.value = equation
        state.leftBound.value = bounds.first
        state.rightBound.value = bounds.second
        state.accuracy.value = accuracy

        stateReducer(GraphIntent(accuracy, bounds.first, bounds.second, equation))
    }
}