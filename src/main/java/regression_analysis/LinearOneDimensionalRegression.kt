package regression_analysis

import correlation_analysis.PirsonRCorrelationCoefficient
import javafx.geometry.Point2D
import org.apache.commons.math3.distribution.FDistribution
import org.apache.commons.math3.distribution.TDistribution
import primary_statistical_analysis.DoubleRange
import primary_statistical_analysis.sample_characteristics.Average
import primary_statistical_analysis.sample_characteristics.StandartDeviation
import primary_statistical_analysis.sample_characteristics.Variance
import primary_statistical_analysis.square

/**
 * Created by Cyxou on 4/17/18.
 */
class LinearOneDimensionalRegression(val points: Array<Point2D>) {
    operator fun Point2D.component1() = this.x
    operator fun Point2D.component2() = this.y


    val allX = points.map { (x, _) -> x }
    val allY = points.map { (_, y) -> y }

    protected val N = points.size.toDouble()

    val a2 = PirsonRCorrelationCoefficient(points).coefficient!! * StandartDeviation(allY).unBiasedValue()!! /
            StandartDeviation(allX).unBiasedValue()!!
    val a1 = Average(allY).unBiasedValue()!! - a2 * Average(allX).unBiasedValue()!!

    val nFunctionParameters = 2
    val residualVariance = points.mapIndexed { index, _ -> ei(index) }.map { ei -> ei.square() }.sum() / (N - nFunctionParameters)


    val a1Variance = residualVariance * (1 / N + Average(allX).unBiasedValue()!!.square() / (N * Variance(allX).unBiasedValue()!!))

    val a2Variance = residualVariance / (N * Variance(allX).unBiasedValue()!!)

    val a1Statistics = a1 / Math.sqrt(a1Variance)

    val a2Statistics = a2 / Math.sqrt(a2Variance)

    val coefficientOfDetermination = PirsonRCorrelationCoefficient(points).coefficient!!.square()

    constructor(points2d: Array<List<Double>>) : this(points2d.map { Point2D(it[0], it[1]) }.toTypedArray())

    fun regressionConfidenceInterval(x: Double, mistakeProbability: Double): DoubleRange {
        val samePart = tDistributionInverseCumulativeProbability(mistakeProbability) * Math.sqrt(regressionVariance(x))
        return DoubleRange(regression(x) - samePart, regression(x) + samePart)
    }

    fun predictingValueConfidenceInterval(x: Double, mistakeProbability: Double): DoubleRange {
        val samePart = tDistributionInverseCumulativeProbability(mistakeProbability) * Math.sqrt(predictedValueVariance(x))
        return DoubleRange(regression(x) - samePart, regression(x) + samePart)
    }

    fun regressionVariance(x: Double) = residualVariance * 1 / N + a1Variance * (x - Average(allX).unBiasedValue()!!).square()

    fun predictedValueVariance(x: Double) = regressionVariance(x) + residualVariance

    fun isA1Significant(mistakeProbability: Double) =
            Math.abs(a1Statistics) <= tDistributionInverseCumulativeProbability(mistakeProbability) || a1Statistics.isNaN()

    fun isA2Significant(mistakeProbability: Double) =
            Math.abs(a2Statistics) <= tDistributionInverseCumulativeProbability(mistakeProbability) || a2Statistics.isNaN()

    fun a1ConfidenceInterval(mistakeProbability: Double): DoubleRange {
        val repeatedPart = Math.sqrt(a1Variance) * tDistributionInverseCumulativeProbability(mistakeProbability)
        return DoubleRange(a1 - repeatedPart, a1 + repeatedPart)
    }

    fun a2ConfidenceInterval(mistakeProbability: Double): DoubleRange {
        val repeatedPart = Math.sqrt(a2Variance) * tDistributionInverseCumulativeProbability(mistakeProbability)
        return DoubleRange(a2 - repeatedPart, a2 + repeatedPart)
    }

    fun tDistributionInverseCumulativeProbability(mistakeProbability: Double) =
            TDistribution(N - 2).inverseCumulativeProbability(1 - mistakeProbability / 2)

    fun pointsToRegressionFunction() = points.map { (x, _) ->
        val y = regression(x)
        Point2D(x, y)
    }.toTypedArray()

    fun pointsToRegressionFunctionConfidenceInterval(mistakeProbability: Double) =
            allX.map { x ->
                val interval = regressionConfidenceInterval(x, mistakeProbability)
                Pair(Point2D(x, interval.start), Point2D(x, interval.endInclusive))
            }.toTypedArray()

    fun pointsToRegressionFunctionPredictingValue(mistakeProbability: Double) =
            allX.map { x ->
                val interval = predictingValueConfidenceInterval(x, mistakeProbability)
                Pair(Point2D(x, interval.start), Point2D(x, interval.endInclusive))
            }.toTypedArray()

    // residual plot
    fun pointsToRegressionValuesAsXAndPredictingValueAsY() = points.mapIndexed { index, (xi, _) -> Point2D(regression(xi), ei(index)) }.toTypedArray()

    fun isRegressionSignificantBasedOnCoffOfDetermination(mistakeProbability: Double): Boolean {
        val denominatorDegreesOfFreedom = N - nFunctionParameters - 1
        val f = coefficientOfDetermination / (1 - coefficientOfDetermination) *
                denominatorDegreesOfFreedom / nFunctionParameters
        return f > fTestDistributionInverseCumulativeProbability(denominatorDegreesOfFreedom, mistakeProbability)
    }

    fun fTestDistributionInverseCumulativeProbability(denominatorDegreesOfFreedom: Double = N - nFunctionParameters - 1,
                                                      mistakeProbability: Double) =
            FDistribution(nFunctionParameters.toDouble(), denominatorDegreesOfFreedom)
                    .inverseCumulativeProbability(1 - mistakeProbability)

    private fun regression(x: Double) = a1 + a2 * x

    private fun ei(i: Int) = allY[i] - regression(allX[i])
}