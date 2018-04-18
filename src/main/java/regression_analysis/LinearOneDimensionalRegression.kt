package regression_analysis

import correlation_analysis.PirsonRCorrelationCoefficient
import javafx.geometry.Point2D
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

    protected val allX = points.map { (x, _) -> x }
    protected val allY = points.map { (_, y) -> y }

    protected val N = points.size.toDouble()

    val a2 = PirsonRCorrelationCoefficient(points).coefficient!! * StandartDeviation(allY).unBiasedValue()!! /
            StandartDeviation(allX).unBiasedValue()!!
    val a1 = Average(allY).unBiasedValue()!! - a2 * Average(allX).unBiasedValue()!!

    val nFunctionParameters = 2
    val s = points.map { (xi, yi) -> yi - regression(xi) }.map { ei -> ei.square() }.sum() / (N - nFunctionParameters)

    private fun regression(x: Double) = a1 + a2 * x

    val a1Variance = s * (1 / N + Average(allX).unBiasedValue()!!.square() / (N * Variance(allX).unBiasedValue()!!))
    val a2Variance = s / (N * Variance(allX).unBiasedValue()!!)

    val a1Statistics = a1 / Math.sqrt(a1Variance)
    val a2Statistics = a2 / Math.sqrt(a2Variance)

    fun isA1Significant(mistakeProbability: Double) =
            Math.abs(a1Statistics) <= tDistributionInverseCumulativeProbability(mistakeProbability) || a1Statistics.isNaN()

    fun isA2Significant(mistakeProbability: Double) =
            Math.abs(a2Statistics) <= tDistributionInverseCumulativeProbability(mistakeProbability)|| a2Statistics.isNaN()

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


}