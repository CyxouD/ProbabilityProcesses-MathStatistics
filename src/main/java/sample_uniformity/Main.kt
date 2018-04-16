package sample_uniformity

import primary_statistical_analysis.VariationalSeries
import java.io.File
import java.util.*
import java.util.regex.Pattern

/**
 * Created by Cyxou on 4/16/18.
 */
fun main(args: Array<String>) {
    val scanner = Scanner(System.`in`)
    val (firstSample, secondSample) = when (args.size) {
        0 -> throw Exception()
        1 -> listOf(processInput(File(args.toList()[0])), processInput(File(args.toList()[0])))
        else -> listOf(processInput(File(args.toList()[0])), processInput(File(args.toList()[1])))
    }
    scanner.use { sc ->
        try {
            readInput(sc, firstSample, secondSample)
        } catch (e: Exception) {
            try {
                e.printStackTrace()
                readInput(sc, firstSample, secondSample)
            } catch (e: Exception) {
                e.printStackTrace()
                readInput(sc, firstSample, secondSample)
            }
        }
    }
}

private fun processInput(file: File): List<Double> {
    val input: List<Double> = file.readLines().map { it.split(Pattern.compile("\\s+")) }
            .map { it.filter { !it.isBlank() } }
            .map { it.map { it.replace(",", ".").toDouble() } }.flatMap { it }
    return input
}

private fun readInput(sc: Scanner, firstSample: List<Double>, secondSample: List<Double>) {
    var firstSample = firstSample
    var secondSample = secondSample
    while (true) {
        val input = sc.nextLine().split(Pattern.compile("\\s+")).map { File(it) }
        firstSample = processInput(input[0])
        secondSample = processInput(input[1])
        TableDisplaying.sampleUniformityCriterions(firstSample,
                secondSample,
                0.05).createAndShowGUI("Проверка однородности выборок")

        primary_statistical_analysis.TableDisplaying.samplingCharacteristics(VariationalSeries(firstSample))
                .createAndShowGUI("Статистические характеристики первой выборки")
        primary_statistical_analysis.TableDisplaying.samplingCharacteristics(VariationalSeries(secondSample))
                .createAndShowGUI("Статистические характеристики второй выборки")
    }
}
