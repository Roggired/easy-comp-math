package ru.roggi.comp.math

import javafx.stage.Stage
import ru.roggi.comp.math.utils.injectRouter
import ru.roggi.comp.math.view.*
import ru.roggi.comp.math.view.presenter.Presenter
import ru.roggi.comp.math.view.tornado.GraphService
import ru.roggi.comp.math.view.tornado.GraphView
import ru.roggi.comp.math.view.tornado.RootStyles
import tornadofx.App
import kotlin.random.Random
import kotlin.system.exitProcess

const val VERSION = "1.0"

const val FILE_GRAPH_ROUT = "fileMethod"
const val GRAPH_ROUTE = "method"
internal const val INPUT_ACCURACY_ROUTE = "inputAccuracy"
internal const val INPUT_BOUNDS_ROUTE = "inputBounds"
internal const val INPUT_EQUATION_ROUTE = "inputEquation"

/**
 * Should be started with TornadoFX.launch<EasyCompMath>("INITIAL_ROUTE_HERE", "PACKAGE_WITH_ROUTER_BUILDER_HERE")
 *
 * @author Roggi
 * @since 1.0
 */
class EasyCompMath : App(GraphView::class, RootStyles::class) {
    private val accuracyBounds = Pair(0.001, 1.0)
    private val leftAndRightBoundBounds = Pair(-50.0, 50.0)
    private val presenter = Presenter()


    override fun start(stage: Stage) {
        if (parameters.raw.size != 2) {
            throw IllegalArgumentException("Wrong parameters. Check EasyCompMath docs by CTRL+Q")
        }

        super.start(stage)

        Thread {
            val router = injectRouter(parameters.raw[1])
            router.sceneContext.addExitListener {
                exitProcess(0)
            }

            val initialRoute = parameters.raw[0]

            val salt = Random.nextInt()
            val easyCompMathRoute = "easyCompMath$salt"

            router
                .apply {
                    register(
                        easyCompMathRoute, EasyCompMathScene(
                            initialRoute,
                            VERSION
                        )
                    )
                    register(GRAPH_ROUTE, GraphScene(GraphService.graphModel!!))
                    register(FILE_GRAPH_ROUT, FileGraphScene(GraphService.graphModel!!))
                    register(
                        INPUT_ACCURACY_ROUTE, InputScene(
                            "Enter accuracy for graph between ${
                                presenter.presentAccuracy(
                                    accuracyBounds.first)
                            } and ${
                                presenter.presentAccuracy(
                                    accuracyBounds.second
                                )
                            }",
                            "",
                            String::toDouble,
                        ) { t -> t in accuracyBounds.first..accuracyBounds.second })
                    register(
                        INPUT_BOUNDS_ROUTE, InputTwoScene(
                            "Enter bounds for graph between ${
                                presenter.presentBound(
                                    leftAndRightBoundBounds.first
                                )
                            } and ${
                                presenter.presentBound(
                                    leftAndRightBoundBounds.second
                                )
                            }",
                            "",
                            String::toDouble,
                            String::toDouble,
                        ) { left, right ->
                            left in leftAndRightBoundBounds.first..leftAndRightBoundBounds.second
                                    && right in leftAndRightBoundBounds.first..leftAndRightBoundBounds.second
                                    && left < right
                        })
                    register(INPUT_EQUATION_ROUTE, EquationScene("Enter equation", ""))
                }.switch(easyCompMathRoute)
        }.start()
    }
}