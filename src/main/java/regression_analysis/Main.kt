package regression_analysis

import javafx.geometry.Point2D
import primary_statistical_analysis.VariationalSeries
import primary_statistical_analysis.sample_characteristics.AntiKurtosis
import primary_statistical_analysis.sample_characteristics.Average
import primary_statistical_analysis.sample_characteristics.Kurtosis
import primary_statistical_analysis.sample_characteristics.StandartDeviation
import java.io.File
import java.util.*
import java.util.regex.Pattern

/**
 * Created by Cyxou on 4/17/18.
 */
fun main(args: Array<String>) {
    val scanner = Scanner(System.`in`)
    scanner.use { sc ->
        try {
            val file = File(args.toList()[0])
            val input: Array<List<Double>> = processInput(file)
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

        } catch (e: Exception) {
            readInput(scanner, arrayOf())
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


//        regressionAnalysisForLinearOneDimensionalRegression(needToProcess)
        regressionAnalysisForLinearOneDimensionalRegression(OneDimensionCoordinateMapping.map(needToProcess,
                xToT = { x -> Math.log(x) },
                yToZ = { y -> Math.log(y) })
        )
    }
}


private fun regressionAnalysisForLinearOneDimensionalRegression(points: Array<List<Double>>) {
    val linearOneDimensionalRegression = LinearOneDimensionalRegression(points)
    val mistakeProbability = 0.05

    val x = linearOneDimensionalRegression.allX
    primary_statistical_analysis.TableDisplaying.samplingCharacteristics(
            VariationalSeries(x), listOf(Average(x), StandartDeviation(x), AntiKurtosis(x), Kurtosis(x))
    ).createAndShowGUI("X ознака")

    val y = linearOneDimensionalRegression.allX
    primary_statistical_analysis.TableDisplaying.samplingCharacteristics(
            VariationalSeries(y), listOf(Average(y), StandartDeviation(y), AntiKurtosis(y), Kurtosis(y))
    ).createAndShowGUI("Y ознака")

    correlation_analysis.TableDisplaying.correlationCoefficients(
            linearOneDimensionalRegression.points.map { listOf(it.x, it.y) }.toTypedArray(),
            mistakeProbability
    ).createAndShowGUI("Коефіцієнти кореляції")

    TableDisplaying.ocenkiParametrovLinearOneDimensionalRegression(linearOneDimensionalRegression.points, mistakeProbability)
            .createAndShowGUI("Оцінки параметрів регресії")

    TableDisplaying.adequacyOfRegressionCriterias(linearOneDimensionalRegression.points, mistakeProbability)
            .createAndShowGUI("Перевірка адекватності відтворених регресійних залежностей")


    Chart("Кореляционное поле с функцией регрессии").correlationFieldAndRegression(linearOneDimensionalRegression.points,
            linearOneDimensionalRegression.pointsToRegressionFunction()
    ).createAndShowGUI()
    val regressionConfidenceIntervalPoints = linearOneDimensionalRegression.pointsToRegressionFunctionConfidenceInterval(mistakeProbability)
    val predictingValueConfidenceIntervalPoints = linearOneDimensionalRegression.pointsToRegressionFunctionPredictingValue(mistakeProbability)
    Chart("Довірчі інтервали для лінії регресії та прогнозних значень")
            .regressionWithPredictingValueConfidenceIntervalAndRegressionConfidenceInterval(
                    regressionPoints = linearOneDimensionalRegression.pointsToRegressionFunction(),
                    regressionConfidenceIntervalPoints = Chart.ConfidenceIntervalPoints(
                            regressionConfidenceIntervalPoints.map { it.first },
                            regressionConfidenceIntervalPoints.map { it.second }),
                    predictingValueConfidenceIntervalPoints = Chart.ConfidenceIntervalPoints(
                            predictingValueConfidenceIntervalPoints.map { it.first },
                            predictingValueConfidenceIntervalPoints.map { it.second })

            ).createAndShowGUI()
    Chart("діагностична діаграма, residual plot")
            .residualPlot(linearOneDimensionalRegression.pointsToRegressionValuesAsXAndPredictingValueAsY())
            .createAndShowGUI()
}