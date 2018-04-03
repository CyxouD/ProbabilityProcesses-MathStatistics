package correlation_analysis

import primary_statistical_analysis.*
import primary_statistical_analysis.sample_characteristics.Average
import primary_statistical_analysis.sample_characteristics.Kurtosis
import primary_statistical_analysis.sample_characteristics.Skewness
import primary_statistical_analysis.sample_characteristics.StandartDeviation
import java.io.File
import java.util.regex.Pattern

/**
 * Created by Cyxou on 4/1/18.
 */
fun main(args: Array<String>) {
    val file = File(args.toList()[0])
    val input = file.readLines().map { it.split(Pattern.compile("\\s+")) }
            .map { it.filter { !it.isBlank() } }
            .map { it.map { it.replace(",", ".").toDouble() } }.toTypedArray()
    Chart("Кор").correlationField(input).createAndShowGUI()
    val xVariationalSeries = VariationalSeries(input.map { it[0] })
    val xSampleCharacteristicsToShow = listOf(Average(xVariationalSeries.orderedSample),
            StandartDeviation(xVariationalSeries.orderedSample),
            Skewness(xVariationalSeries.orderedSample),
            Kurtosis(xVariationalSeries.orderedSample))
    TableDisplaying.samplingCharacteristics(xVariationalSeries, xSampleCharacteristicsToShow).createAndShowGUI("X ознака")
    val yVariationalSeries = VariationalSeries(input.map { it[1] })
    val ySampleCharacteristicsToShow = listOf(Average(yVariationalSeries.orderedSample),
            StandartDeviation(yVariationalSeries.orderedSample),
            Skewness(yVariationalSeries.orderedSample),
            Kurtosis(yVariationalSeries.orderedSample))

    TableDisplaying.samplingCharacteristics(yVariationalSeries, ySampleCharacteristicsToShow).createAndShowGUI("Y ознака")


    val pirsonRCorrelationCoefficient = PirsonRCorrelationCoefficient(input)
    println("r = ${pirsonRCorrelationCoefficient.coefficient}")
    println("statistics = ${pirsonRCorrelationCoefficient.statistics}")
    println("isSignificant = ${pirsonRCorrelationCoefficient.isSignificant(0.95)}")
    println("coefficientConfidenceInterval = ${pirsonRCorrelationCoefficient.coefficientConfidenceInterval(0.95)}")


    val spearmanRankCorrelationCoefficient = SpearmanRankCorrelationCoefficient(input)
    println("r = ${spearmanRankCorrelationCoefficient.coefficient}")
    println("statistics = ${spearmanRankCorrelationCoefficient.statistics}")
    println("isSignificant = ${spearmanRankCorrelationCoefficient.isSignificant(0.95)}")
    println("coefficientConfidenceInterval = ${spearmanRankCorrelationCoefficient.coefficientConfidenceInterval(0.95)}")

}