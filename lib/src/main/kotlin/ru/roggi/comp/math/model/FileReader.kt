package ru.roggi.comp.math.model

import java.io.FileInputStream
import java.lang.NumberFormatException
import java.util.*


class FileReader(fileName: String) {
    private val scanner: Scanner = Scanner(FileInputStream(fileName))


    fun readEquation(): String {
        if (!scanner.hasNextLine()) {
            throw IllegalStateException("Cannot find equation")
        }

        return scanner.nextLine().trim()
    }

    fun readBounds(): Pair<Double, Double> {
        if (!scanner.hasNextLine()) {
            throw IllegalStateException("Cannot find bounds")
        }

        val parts = scanner.nextLine().trim().split(Regex(" +"))

        try {
            return Pair(parts[0].toDouble(), parts[1].toDouble())
        } catch (e: NumberFormatException) {
            throw IllegalStateException("Cannot parse bounds")
        }
    }

    fun readAccuracy(): Double {
       if (!scanner.hasNextLine()) {
           throw IllegalStateException("Cannot find accuracy")
       }

       try {
           return scanner.nextLine().trim().toDouble()
       } catch (e: NumberFormatException) {
           throw IllegalStateException("Cannot parse accuracy")
       }
    }

    fun close() {
        scanner.close()
    }
}