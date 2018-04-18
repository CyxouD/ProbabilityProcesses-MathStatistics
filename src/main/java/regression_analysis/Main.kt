package regression_analysis

import javafx.geometry.Point2D
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

    val linearOneDimensionalRegression = LinearOneDimensionalRegression(randomPoints)
    val mistakeProbability = 0.05
    with(linearOneDimensionalRegression) {
        println("a1 = [$a1; $a1Variance; $a1Statistics; ${tDistributionInverseCumulativeProbability(mistakeProbability)}; " +
                "${isA1Significant(mistakeProbability)}; ${a1ConfidenceInterval(mistakeProbability)}; " +
                "${if (isA1Significant(mistakeProbability)) "=0" else "!=0"} ")

        println("a2 = [$a2; $a2Variance; $a2Statistics; ${tDistributionInverseCumulativeProbability(mistakeProbability)}; " +
                "${isA2Significant(mistakeProbability)}; ${a2ConfidenceInterval(mistakeProbability)};" +
                "${if (isA2Significant(mistakeProbability)) "=0" else "!=0"} ")
        println("R^2 (CoefficientOfDetermination) = $coefficientOfDetermination")

    }

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
}