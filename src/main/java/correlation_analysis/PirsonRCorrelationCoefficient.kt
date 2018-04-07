package correlation_analysis

import identification_and_recovery_of_distributions.UnionDistribution
import javafx.geometry.Point2D
import org.apache.commons.math3.distribution.TDistribution
import org.apache.commons.math3.distribution.UniformRealDistribution
import org.apache.commons.math3.exception.OutOfRangeException
import primary_statistical_analysis.DoubleRange
import primary_statistical_analysis.sample_characteristics.Average
import primary_statistical_analysis.sample_characteristics.StandartDeviation
import primary_statistical_analysis.square

/**
 * Created by Cyxou on 4/2/18.
 */
class PirsonRCorrelationCoefficient : CorrelationCoefficient {

    constructor(points: Array<Point2D>) : super(points)
    constructor(points2d: Array<List<Double>>) : super(points2d)

    override val coefficient: Double? = {
        val averageXY = Average(points.map { (x, y) -> x * y }).unBiasedValue()
        val allX = points.map { (x, _) -> x }
        val allY = points.map { (_, y) -> y }
        val averageX = Average(allX).unBiasedValue()
        val averageY = Average(allY).unBiasedValue()
        val biasedStandartDeviationX = StandartDeviation(allX).biasedValue()
        val biasedStandartDeviationY = StandartDeviation(allY).biasedValue()
        if (listOf(averageX, averageY, averageXY, biasedStandartDeviationX, biasedStandartDeviationY).all { it != null }) {
            (averageXY!! - averageX!! * averageY!!) / (biasedStandartDeviationX!! * biasedStandartDeviationY!!)
        } else {
            null
        }
    }.invoke()

    override val statistics: Double? = {
        coefficient?.let { r -> (r * Math.sqrt(N - 2)) / Math.sqrt(1 - r.square()) }
    }.invoke()


    override fun isSignificant(probability: Double): Boolean? {
        if (probability < 0.0 || probability > 1.0) {
            throw OutOfRangeException(probability, 0, 1)
        }

        return statistics?.let { t ->
            Math.abs(t) > TDistribution(points.size.toDouble() - 2).inverseCumulativeProbability(probability)
        }
    }

    override fun coefficientConfidenceInterval(probability: Double) =
            isSignificant(probability)?.let {
                val r = coefficient!!
                val leftSum = r + r * (1 - r.square()) / (2 * N)
                val rightSum = UniformRealDistribution().inverseCumulativeProbability(probability) * (1 - r.square()) / (Math.sqrt(N - 1))
                DoubleRange(leftSum - rightSum, leftSum + rightSum)
            }
}