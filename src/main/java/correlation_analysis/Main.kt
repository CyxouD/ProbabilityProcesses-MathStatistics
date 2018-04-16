package correlation_analysis

import primary_statistical_analysis.*
import primary_statistical_analysis.sample_characteristics.Average
import primary_statistical_analysis.sample_characteristics.Kurtosis
import primary_statistical_analysis.sample_characteristics.Skewness
import primary_statistical_analysis.sample_characteristics.StandartDeviation
import java.io.File
import java.util.*
import java.util.regex.Pattern

/**
 * Created by Cyxou on 4/1/18.
 */
fun main(args: Array<String>) {
    val scanner = Scanner(System.`in`)
    val file = File(args.toList()[0])
    val input: Array<List<Double>> = processInput(file)
    scanner.use { sc ->
        try {
            readInput(sc, input)
        } catch (e: Exception) {
            try {
                e.printStackTrace()
                readInput(sc, input)
            } catch (e: Exception) {
                e.printStackTrace()
                readInput(sc, input)
            }
        }
    }
}

private fun processInput(file: File): Array<List<Double>> {
    val input: Array<List<Double>> = file.readLines().map { it.split(Pattern.compile("\\s+")) }
            .map { it.filter { !it.isBlank() } }
            .map { it.map { it.replace(",", ".").toDouble() } }.toTypedArray()
    return input
}

private fun readInput(sc: Scanner, input: Array<List<Double>>) {
    var needToProcess = input
    while (true) {
        needToProcess = processInput(File(sc.nextLine()))
        Chart("Кор").correlationField(needToProcess).createAndShowGUI()
        val xVariationalSeries = VariationalSeries(needToProcess.map { it[0] })
        val xSampleCharacteristicsToShow = listOf(Average(xVariationalSeries.orderedSample),
                StandartDeviation(xVariationalSeries.orderedSample),
                Skewness(xVariationalSeries.orderedSample),
                Kurtosis(xVariationalSeries.orderedSample))
        primary_statistical_analysis.TableDisplaying.samplingCharacteristics(xVariationalSeries, xSampleCharacteristicsToShow).createAndShowGUI("X ознака")
        val yVariationalSeries = VariationalSeries(needToProcess.map { it[1] })
        val ySampleCharacteristicsToShow = listOf(Average(yVariationalSeries.orderedSample),
                StandartDeviation(yVariationalSeries.orderedSample),
                Skewness(yVariationalSeries.orderedSample),
                Kurtosis(yVariationalSeries.orderedSample))

        primary_statistical_analysis.TableDisplaying.samplingCharacteristics(yVariationalSeries, ySampleCharacteristicsToShow).createAndShowGUI("Y ознака")
        val mistakeProbability = 0.05
        TableDisplaying.correlationCoefficients(needToProcess, mistakeProbability).createAndShowGUI("Коефіцієнти кореляції")

//    val pirsonRCorrelationCoefficient = PirsonRCorrelationCoefficient(needToProcess)
//    println("r = ${pirsonRCorrelationCoefficient.coefficient}")
//    println("statistics = ${pirsonRCorrelationCoefficient.statistics}")
//    println("isSignificant = ${pirsonRCorrelationCoefficient.isSignificant(mistakeProbability)}")
//    println("coefficientConfidenceInterval = ${pirsonRCorrelationCoefficient.coefficientConfidenceInterval(mistakeProbability)}")
//
//
//    val spearmanRankCorrelationCoefficient = SpearmanRankCorrelationCoefficient(needToProcess)
//    println("r = ${spearmanRankCorrelationCoefficient.coefficient}")
//    println("statistics = ${spearmanRankCorrelationCoefficient.statistics}")
//    println("isSignificant = ${spearmanRankCorrelationCoefficient.isSignificant(mistakeProbability)}")
//
//    val kendallRankCorrelationCoefficient = KendallRankCorrelationCoefficient(needToProcess)
//    println("r = ${kendallRankCorrelationCoefficient.coefficient}")
//    println("statistics = ${kendallRankCorrelationCoefficient.statistics}")
//    println("isSignificant = ${kendallRankCorrelationCoefficient.isSignificant(mistakeProbability)}")
//
//    val correlationRatio = CorrelationRatio(needToProcess)
//    println("r = ${correlationRatio.coefficient}")
//    println("statistics = ${correlationRatio.statistics}")
//    println("isSignificant = ${correlationRatio.isSignificant(mistakeProbability)}")
    }
}
