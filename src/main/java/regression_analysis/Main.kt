package regression_analysis

import javafx.geometry.Point2D
import primary_statistical_analysis.VariationalSeries
import primary_statistical_analysis.sample_characteristics.AntiKurtosis
import primary_statistical_analysis.sample_characteristics.Average
import primary_statistical_analysis.sample_characteristics.Kurtosis
import primary_statistical_analysis.sample_characteristics.StandartDeviation
import java.util.*

/**
 * Created by Cyxou on 4/17/18.
 */
fun main(args: Array<String>) {
    val linearOneDimensionalFunctionThroughStartCoordinatesPositive = arrayOf(Point2D(3.0, 3.0),
            Point2D(4.0, 4.0),
            Point2D(5.0, 5.0))

    val linearOneDimensionalFunctionThroughStartCoordinatesNegative = arrayOf(Point2D(3.0, -3.0),
            Point2D(4.0, -4.0),
            Point2D(5.0, -5.0))

    val tt = arrayOf(Point2D(0.0, 1.0),
            Point2D(1.0, 2.0),
            Point2D(2.0, 3.0))


    val lineParallexToAndAboveXAxis = arrayOf(Point2D(3.0, 4.0),
            Point2D(4.0, 4.0),
            Point2D(5.0, 4.0))

    val lineParallexToAndBelowXAxis = arrayOf(Point2D(3.0, -4.0),
            Point2D(4.0, -4.0),
            Point2D(5.0, -4.0))

    val randomPoints = (0..100).map { Point2D(Random().nextDouble() * 100, Random().nextDouble() * 100) }.toTypedArray()

    regressionAnalysisForLinearOneDimensionalRegression(randomPoints)
    regressionAnalysisForLinearOneDimensionalRegression(OneDimensionCoordinateMapping.map(randomPoints,
            xToT = { x -> Math.log(x) },
            yToZ = { y -> Math.log(y) })
    )
}

private fun regressionAnalysisForLinearOneDimensionalRegression(randomPoints: Array<Point2D>) {
    val linearOneDimensionalRegression = LinearOneDimensionalRegression(randomPoints)
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