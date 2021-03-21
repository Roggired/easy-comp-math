package ru.roggi.comp.math.model

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class EquationTest {
    @Test
    fun `Polynomial Equation 1`() {
        assertEquals(
            createEquationFrom("x^9 + x^8 + x^7 + x^6 + x^5 + x^4 + x^3 + x^2 + x + 1").evaluate(1.0),
            10.0
        )
    }

    @Test
    fun `Polynomial Equation 2`() {
        assertEquals(
            createEquationFrom("x^9 + x^8 + x^7 + x^6 + x^5 + x^4 + x^3 + x^2 + x + 1").evaluate(2.0),
            1023.0
        )
    }
}