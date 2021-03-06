package ru.roggi.comp.math.view.tornado

import tornadofx.Stylesheet
import tornadofx.px

class RootStyles : Stylesheet() {
    companion object {
        const val PREF_HEIGHT = 600
        const val PREF_WIDTH = 950
    }

    init {
        root {
            prefHeight = PREF_HEIGHT.px
            prefWidth = PREF_WIDTH.px
        }
    }
}