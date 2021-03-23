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

                        stringBuilder.append(presentTerm(it))
                        stringBuilder.append(" ")
                    }

            return deleteExtraPlus(stringBuilder).trim()
        }

        private fun presentTerm(term: Term): String {
            val stringBuilder = StringBuilder()
            with(stringBuilder) {
                when (term) {
                    is LinearTerm -> {
                        append(presentSignAndFactorErasureOne(term.sign, term.factor))
                        append("x")
                    }
                    is ConstantTerm -> append(presentSignAndFactor(term.sign, term.factor))
                    is ConstantEquationTerm -> {
                        append(presentSignAndFactor(term.sign, term.factor))
                        append(presentEquation(term.baseEquation))
                    }
                    is PolynomialTerm -> {
                        append(presentSignAndFactorErasureOne(term.sign, term.factor))
                        if (term.baseEquation.terms.size == 1 && term.baseEquation.terms[0] is LinearTerm) {
                            append("x")
                        } else {
                            append(presentEquation(term.baseEquation))
                        }
                        append("^")
                        append(presentDoubleErasureOne(term.power))
                    }
                    is TrigonometricTerm -> {
                        append(presentSignAndFactorErasureOne(term.sign, term.factor))
                        append(term.functionName)
                        append(presentEquation(term.baseEquation))
                    }
                    is LogarithmicTerm -> {
                        append(presentSignAndFactorErasureOne(term.sign, term.factor))
                        append("log^")
                        append(presentDoubleErasureOne(term.logBase))
                        append(presentEquation(term.baseEquation))
                    }
                    is MultiTerm -> {
                        append(presentSignAndFactorErasureOne(term.sign, term.factor))
                        append(presentMultiTerm(term))
                    }
                    else -> {/*nothing*/}
                }
            }
            return stringBuilder.toString().trim()
        }

        private fun presentEquation(equation: Equation): String {
            val stringBuilder = StringBuilder()
            with(stringBuilder) {
                append("(")
                append(present(equation))
                append(")")
            }
            return stringBuilder.toString().trim()
        }

        private fun presentMultiTerm(multiTerm: MultiTerm): String {
            val stringBuilder = StringBuilder()

            if (multiTerm.factor == 0.0) {
                return ""
            }

            stringBuilder.append(presentSignAndFactorErasureOne(multiTerm.sign, multiTerm.factor))

            val term = multiTerm.terms.iterator()
            val operation = multiTerm.operations.iterator()

            while (term.hasNext()) {
                stringBuilder.append(presentTerm(term.next()))

                if (operation.hasNext()) stringBuilder.append(operation.next())
            }

            return stringBuilder.toString().trim()
        }

        private fun deleteExtraPlus(stringBuilder: StringBuilder): String {
            val result = stringBuilder.toString()
            return if (result.startsWith("+ ")) {
                result.substring(2)
            } else {
                result
            }
        }

        private fun presentSignAndFactorErasureOne(sign: Sign, factor: Double): String {
            val stringBuilder = StringBuilder()
            if (sign.isMinus() && factor < 0) {
                stringBuilder.append("+ ").append(presentDoubleErasureOne(factor))
            }

            if (sign.isMinus() && factor > 0) {
                stringBuilder.append("- ").append(presentDoubleErasureOne(factor))
            }

            if (sign.isPlus() && factor < 0) {
                stringBuilder.append("- ").append(presentDoubleErasureOne(factor))
            }

            if (sign.isPlus() && factor > 0) {
                stringBuilder.append("+ ").append(presentDoubleErasureOne(factor))
            }

            return stringBuilder.toString()
        }

        private fun presentSignAndFactor(sign: Sign, factor: Double): String {
            val stringBuilder = StringBuilder()
            if (sign.isMinus() && factor < 0) {
                stringBuilder.append("+ ").append(presentDouble(factor))
            }

            if (sign.isMinus() && factor > 0) {
                stringBuilder.append("- ").append(presentDouble(factor))
            }

            if (sign.isPlus() && factor < 0) {
                stringBuilder.append("- ").append(presentDouble(factor))
            }

            if (sign.isPlus() && factor > 0) {
                stringBuilder.append("+ ").append(presentDouble(factor))
            }

            return stringBuilder.toString()
        }

        private fun presentDoubleErasureOne(number: Double): String {
            if (number == 1.0) {
                return ""
            }

            return presentDouble(number)
        }

        private fun presentDouble(number: Double): String {
            if (number.toInt() - number == 0.0) {
                return number.toInt().toString()
            }

            return "%.3f".format(number)
        }
    }
}