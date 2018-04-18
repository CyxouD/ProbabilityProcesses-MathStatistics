package regression_analysis

import javafx.geometry.Point2D

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



    val linearOneDimensionalRegression = LinearOneDimensionalRegression(tt)
    val mistakeProbability = 0.05
    with(linearOneDimensionalRegression) {
        println("a1 = [$a1; $a1Variance; $a1Statistics; ${tDistributionInverseCumulativeProbability(mistakeProbability)}; " +
                "${isA1Significant(mistakeProbability)}; ${a1ConfidenceInterval(mistakeProbability)} ")

        println("a2 = [$a2; $a2Variance; $a2Statistics; ${tDistributionInverseCumulativeProbability(mistakeProbability)}; " +
                "${isA2Significant(mistakeProbability)}; ${a2ConfidenceInterval(mistakeProbability)} ")

    }
}