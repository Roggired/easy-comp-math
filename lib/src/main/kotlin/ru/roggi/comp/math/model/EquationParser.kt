package ru.roggi.comp.math.model

import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.tan


fun createEquationFrom(string: String): Equation {
    val openParenthesisesNumber = string.count { c -> c == '(' }
    val closeParenthesisesNumber = string.count { c -> c == ')' }

    if (openParenthesisesNumber != closeParenthesisesNumber) throw IllegalArgumentException("Numbers of opened and closed parenthesises is not equal")

    val input = string.trim()
                      .replace(" ", "")
                      .replace(",", ".")

    val terms: ArrayList<Term> = ArrayList()

    var openParenthesises = 0
    var term = ""

    input.forEach {
        when (it) {
            '(' -> openParenthesises++
            ')' -> openParenthesises--
            '+', '-' -> {
                if (openParenthesises == 0 && term.isNotEmpty() && term.last() != '^') {
                    terms.add(parse(term))
                    term = ""
                }
            }
        }

        term += it
    }

    terms.add(parse(term))

    return Equation(terms)
}

private fun parse(term: String): Term =
    if (term.contains("*") || term.contains("/"))
        parseMultiTerm(term)
    else
        parseSingleTerm(term)

private fun parseSingleTerm(term: String): Term {
    var right = term.length - 1
    var left = 0

    while (left < term.length - 1 && term[left] != '(') left++

    if (left == term.length - 1) return invokeCreateTerm(term, emptyEquation())

    while (right > 0 && term[right] != ')') right--

    if (right == 0) return invokeCreateTerm(term, emptyEquation())

    return invokeCreateTerm(term.substring(0, left), createEquationFrom(term.substring(left + 1, right)))
}

private fun invokeCreateTerm(term: String, baseEquation: Equation): Term {
    val sign = getSign(term)
    var factor = getFactor(term)

    var targetIndex = factor.length
    if (term.startsWith("-") || term.startsWith("+")){
        targetIndex++
    }

    if (factor.isEmpty()) {
        factor = "1"
    }

    return createTerm(sign, factor, term.substring(targetIndex), baseEquation)
}

private fun parseMultiTerm(term: String): Term {
    val termsStrings: ArrayList<String> = ArrayList()

    var openParenthesises = 0
    var currentTermString = ""

    term.forEach {
        when (it) {
            '(' -> openParenthesises++
            ')' -> openParenthesises--
            '*', '/' -> {
                if (openParenthesises == 0) {
                    termsStrings.add(currentTermString)
                    currentTermString = ""
                }
            }
        }

        currentTermString += it
    }

    termsStrings.add(currentTermString)

    val terms: ArrayList<Term> = ArrayList()
    val operations: ArrayList<Operation> = ArrayList()

    termsStrings.forEach {
        if (it.startsWith("*") || it.startsWith("/")) {
            operations.add(Operation(it[0].toString()))
            terms.add(parseSingleTerm(it.substring(1)))
        } else {
            terms.add(parseSingleTerm(it))
        }
    }

    return MultiTerm(Sign("+"), 1.0, terms.toTypedArray(), operations.toTypedArray())
}

private fun getSign(term: String): String {
    if (term.startsWith("-")) {
        return "-"
    }

    return "+"
}

private fun getFactor(term: String): String {
    var factor = ""

    term.forEach {
        if (it != '+' && it != '-' && (it.isDigit() || it == '.')) {
            factor += it
        }

        if (it != '+' && it != '-' && !it.isDigit() && it != '.') {
            return factor
        }
    }

    return factor
}

private fun createTerm(signStr: String, factorStr: String, term: String, baseEquation: Equation): Term {
    val sign = Sign(signStr)
    val factor = factorStr.toDouble()

    if (term.isEmpty()) {
        return ConstantEquationTerm(sign, factor, baseEquation)
    }

    when (term) {
        "sin" -> return TrigonometricTerm(sign, factor, baseEquation, "sin", ::sin)
        "cos" -> return TrigonometricTerm(sign, factor, baseEquation, "cos", ::cos)
        "tg" -> return TrigonometricTerm(sign, factor, baseEquation, "tg", ::tan)
        "ctg" -> return TrigonometricTerm(sign, factor, baseEquation, "ctg", ::ctg)
        "x" -> return LinearTerm(sign, factor)
    }

    if (term.contains("log")) {
        return LogarithmicTerm(sign, factor, baseEquation, term.substring(term.indexOf("^") + 1).toDouble())
    }

    if (term.contains("^") && term.contains("x")) {
        return PolynomialTerm(sign, factor, linearEquation(), term.substring(term.indexOf("^") + 1).toDouble())
    }

    if (term.contains("^")) {
        return PolynomialTerm(sign, factor, baseEquation, term.substring(term.indexOf("^") + 1).toDouble())
    }

    throw IllegalArgumentException("Found unparsable symbols")
}

private fun linearEquation(): Equation = Equation(mutableListOf(LinearTerm(Sign("+"), 1.0)))