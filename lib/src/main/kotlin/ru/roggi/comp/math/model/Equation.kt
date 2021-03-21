package ru.roggi.comp.math.model

import kotlin.math.log
import kotlin.math.pow
import kotlin.math.tan

/**
 * Equation is a base class for operations with equations. Can be evaluated by invoking Equation.evaluate
 *
 * @author Roggi
 * @since 1.0
 */
class Equation(
    internal val terms: Array<Term>
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Equation

        if (!terms.contentEquals(other.terms)) return false

        return true
    }

    override fun hashCode(): Int {
        return terms.contentHashCode()
    }

    override fun toString(): String {
        return "Equation(tokens=${terms.contentToString()})"
    }

    fun evaluate(x: Double): Double = terms.fold(0.0, { acc, term -> acc + term.evaluate(x) })

    fun evaluateFirstDerivative(x: Double): Double =
                (-evaluate(x + 2 * DERIVATIVE_STEP)
                + 8 * evaluate(x + DERIVATIVE_STEP)
                - 8 * evaluate(x - DERIVATIVE_STEP)
                + evaluate(x - 2 * DERIVATIVE_STEP)
                ) / (12 * DERIVATIVE_STEP)

    fun evaluateSecondDerivative(x: Double): Double =
                (-evaluateFirstDerivative(x + 2 * DERIVATIVE_STEP)
                + 8 * evaluateFirstDerivative(x + DERIVATIVE_STEP)
                - 8 * evaluateFirstDerivative(x - DERIVATIVE_STEP)
                + evaluateFirstDerivative(x + 2 * DERIVATIVE_STEP)
                ) / (12 * DERIVATIVE_STEP)

    companion object {
        private const val DERIVATIVE_STEP = 0.00001
    }
}

fun emptyEquation(): Equation = Equation(arrayOf(ConstantTerm(Sign("+"), 1.0)))

class Sign(private val value: String) {
    fun applyTo(x: Double): Double =
        when(value) {
            "+" -> x
            "-" -> -x
            else -> TODO("Unreachable")
        }

    override fun toString(): String {
        return "Sign(value='$value')"
    }

    fun isMinus(): Boolean = value == "-"

    fun isPlus(): Boolean = value == "+"
}

internal class Operation(internal val op: String) {
    fun apply(acc: Double, value: Double): Double =
        when(op) {
            "*" -> acc * value
            "/" -> acc / value
            else -> acc
        }
}

internal class MultiTerm(
    sign: Sign,
    factor: Double,
    internal val terms: Array<Term>,
    internal val operations: Array<Operation>
): Term(sign, factor) {
    override fun evaluate(x: Double): Double {
        val value = terms.map { it.evaluate(x) }.iterator()
        var acc = value.next()
        val operation = operations.iterator()

        while (operation.hasNext()) {
            acc = operation.next().apply(acc, value.next())
        }

        return acc
    }
}

abstract class Term(
    internal val sign: Sign,
    internal val factor: Double
) {
    companion object Presenter

    abstract fun evaluate(x: Double): Double
    override fun toString(): String {
        return "Term(sign=$sign, factor=$factor)"
    }
}

internal class LinearTerm(
    sign: Sign,
    factor: Double
): Term(sign, factor) {
    override fun evaluate(x: Double): Double = sign.applyTo(factor * x)
}

internal class ConstantEquationTerm(
    sign: Sign,
    factor: Double,
    baseEquation: Equation
): ComplexTerm(sign, factor, baseEquation) {
    override fun evaluate(x: Double): Double = super.evaluate(x) * baseEquation.evaluate(x)
}

internal class ConstantTerm(
    sign: Sign,
    factor: Double
): Term(sign, factor) {
    override fun evaluate(x: Double): Double = sign.applyTo(factor)
}

internal abstract class ComplexTerm(
    sign: Sign,
    factor: Double,
    internal val baseEquation: Equation
): Term(sign, factor) {
    override fun evaluate(x: Double): Double = sign.applyTo(factor)

    override fun toString(): String {
        return "ComplexTerm(baseEquation=$baseEquation)"
    }
}

internal class PolynomialTerm(
    sign: Sign,
    factor: Double,
    baseEquation: Equation,
    internal val power: Double
) : ComplexTerm(sign, factor, baseEquation) {
    override fun evaluate(x: Double): Double = super.evaluate(x) * baseEquation.evaluate(x).pow(power)

    override fun toString(): String {
        return "PolynomialTerm(power=$power)" + " " + super.toString()
    }
}

internal class TrigonometricTerm(
    sign: Sign,
    factor: Double,
    baseEquation: Equation,
    internal val functionName: String,
    internal val function: (x: Double) -> Double
): ComplexTerm(sign, factor, baseEquation) {
    override fun evaluate(x: Double): Double = super.evaluate(x) * function(baseEquation.evaluate(x))

    override fun toString(): String {
        return "TrigonometricTerm(function=$function)" + " " + super.toString()
    }
}

internal fun ctg(x: Double): Double = 1/ tan(x)

internal class LogarithmicTerm(
    sign: Sign,
    factor: Double,
    baseEquation: Equation,
    internal val logBase: Double
): ComplexTerm(sign, factor, baseEquation) {
    override fun evaluate(x: Double): Double = super.evaluate(x) * log(baseEquation.evaluate(x), logBase)

    override fun toString(): String {
        return "LogarithmicTerm(logBase=$logBase)" + " " + super.toString()
    }
}