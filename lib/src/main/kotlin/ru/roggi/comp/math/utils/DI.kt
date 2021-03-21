package ru.roggi.comp.math.utils

import org.reflections.Reflections
import org.reflections.scanners.SubTypesScanner
import org.reflections.util.ClasspathHelper
import ru.roggi.console.application.view.scene.Router
import kotlin.reflect.full.createInstance


interface RouterBuilder {
    fun build(): Router
}


fun injectRouter(packageName: String): Router {
    val reflections = Reflections(ClasspathHelper.forPackage(packageName), SubTypesScanner())
    val routerBuilders = reflections.getSubTypesOf(RouterBuilder::class.java)

    when (routerBuilders.size) {
        1 -> return routerBuilders.iterator().next().kotlin.createInstance().build()
        0 -> throw RuntimeException("Cannot find router builder")
        else -> throw RuntimeException("Too many router builders")
    }
}