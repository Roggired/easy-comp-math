package ru.roggi.comp.math.view.presenter

import ru.roggi.comp.math.model.*
import ru.roggi.comp.math.model.ConstantEquationTerm
import ru.roggi.comp.math.model.ConstantTerm
import ru.roggi.comp.math.model.LinearTerm
import ru.roggi.comp.math.model.PolynomialTerm
import kotlin.math.abs


const val ACCURACY_FORMAT = "%.4f"
const val BOUND_FORMAT = "%.1f"

class Presenter {
    fun presentAccuracy(accuracy: Double): String = ACCURACY_FORMAT.format(accuracy)

    fun presentBound(bound: Double): String = BOUND_FORMAT.format(bound)

    companion object {
        /**
         * Presents given table using String.format. All numbers interprets like doubles.
         */
        fun present(table: ArrayList<Array<String>>, symbolsPerColumn: Int, symbolsAfterDot: Int): String {
            val stringBuilder = StringBuilder()

            table.forEach {
                it.forEach {
                    try {
                        it.toDouble()
                            .apply { stringBuilder.append("%${symbolsPerColumn}.${symbolsAfterDot}f".format(this)) }
                    } catch (e: NumberFormatException) {
                        stringBuilder.append("%${symbolsPerColumn}s".format(it))
                    }
                }
                stringBuilder.append(System.lineSeparator())
            }

            return stringBuilder.toString()
        }

        fun present(equation: Equation): String {
            val stringBuilder = StringBuilder()

            equation.terms
                    .forEach {
                        if (it.factor == 0.0) {
                            return@forEach
                        }

                        stringBuilder.append(presentSignAndFactor(it.sign, it.factor))
                        stringBuilder.append(presentTerm(it))
                        stringBuilder.append(" ")
                    }

            return deleteExtraPlus(stringBuilder).trim()
        }

        private fun presentTerm(term: Term): String {
            val stringBuilder = StringBuilder()
            with(stringBuilder) {
                when (term) {
                    is LinearTerm -> append("x")
                    is ConstantTerm -> { /*nothing*/  }
                    is ConstantEquationTerm -> append(presentEquation(term.baseEquation))
                    is PolynomialTerm -> {
                        if (term.baseEquation.terms.size == 1 && term.baseEquation.terms[0] is LinearTerm) {
                            append("x")
                        } else {
                            append(presentEquation(term.baseEquation))
                        }
                        append("^")
                        append(presentDouble(term.power))
                    }
                    is TrigonometricTerm -> {
                        append(term.functionName)
                        append(presentEquation(term.baseEquation))
                    }
                    is LogarithmicTerm -> {
                        append("log^")
                        append(presentDouble(term.logBase))
                        append(presentEquation(term.baseEquation))
                    }
                    is MultiTerm -> append(presentMultiTerm(term))
                    else -> {/*nothing*/}
                }
            }
            return deleteExtraPlus(stringBuilder).trim()
        }

        private fun presentEquation(equation: Equation): String {
            val stringBuilder = StringBuilder()
            with(stringBuilder) {
                append("(")
                append(present(equation))
                append(")")
            }
            return deleteExtraPlus(stringBuilder).trim()
        }

        private fun presentMultiTerm(multiTerm: MultiTerm): String {
            val stringBuilder = StringBuilder()

            if (multiTerm.factor == 0.0) {
                return ""
            }

            stringBuilder.append(presentSignAndFactor(multiTerm.sign, multiTerm.factor))

            val term = multiTerm.terms.iterator()
            val operation = multiTerm.operations.iterator()

            while (term.hasNext()) {
                stringBuilder.append(presentTerm(term.next()))

                if (operation.hasNext()) stringBuilder.append(operation.next())
            }

            return deleteExtraPlus(stringBuilder).trim()
        }

        private fun deleteExtraPlus(stringBuilder: StringBuilder): String {
            val result = stringBuilder.toString()
            return if (result.startsWith("+ ")) {
                result.substring(2)
            } else {
                result
            }
        }

        private fun presentSignAndFactor(sign: Sign, factor: Double): String {
            val stringBuilder = StringBuilder()
            if (sign.isMinus() && factor < 0) {
                stringBuilder.append("+ ").append(presentFactor(factor))
            }

            if (sign.isMinus() && factor > 0) {
                stringBuilder.append("- ").append(presentFactor(factor))
            }

            if (sign.isPlus() && factor < 0) {
                stringBuilder.append("- ").append(presentFactor(factor))
            }

            if (sign.isPlus() && factor > 0) {
                stringBuilder.append("+ ").append(presentFactor(factor))
            }

            return stringBuilder.toString()
        }

        private fun presentFactor(factor: Double): String = presentDouble(abs(factor))

        private fun presentDouble(number: Double): String {
            if (number == 1.0) {
                return ""
            }

            if (number.toInt() - number == 0.0) {
                return number.toInt().toString()
            }

            return "%.3f".format(number)
        }
    }
}