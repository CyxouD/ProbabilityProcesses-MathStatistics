package correlation_analysis

import javafx.geometry.Point2D
import org.apache.commons.math3.distribution.TDistribution
import org.apache.commons.math3.exception.OutOfRangeException
import primary_statistical_analysis.sample_characteristics.Average
import primary_statistical_analysis.sample_characteristics.StandartDeviation
import primary_statistical_analysis.square
import java.awt.Point

/**
 * Created by Cyxou on 4/2/18.
 */
class PirsonRCorrelationCoefficient(override val points: Array<Point2D>) : CorrelationCoefficient {

    constructor(points2d: Array<List<Double>>) : this(points2d.map { Point2D(it[0], it[1]) }.toTypedArray())

    override fun coefficient(): Double? {
        val averageXY = Average(points.map { (x, y) -> x * y }).unBiasedValue()
        val allX = points.map { (x, _) -> x }
        val allY = points.map { (_, y) -> y }
        val averageX = Average(allX).unBiasedValue()
        val averageY = Average(allY).unBiasedValue()
        val biasedStandartDeviationX = StandartDeviation(allX).biasedValue()
        val biasedStandartDeviationY = StandartDeviation(allY).biasedValue()
        return if (listOf(averageX, averageY, averageXY, biasedStandartDeviationX, biasedStandartDeviationY).all { it != null }) {
            (averageXY!! - averageX!! * averageY!!) / (biasedStandartDeviationX!! * biasedStandartDeviationY!!)
        } else {
            null
        }
    }

    override fun statistics(): Double? {
        val N = points.size.toDouble()
        return coefficient()?.let { r -> (r * Math.sqrt(N - 2)) / Math.sqrt(1 - r.square()) }
    }

    override fun significance(probability: Double): Boolean? {
        if (probability < 0.0 || probability > 1.0) {
            throw OutOfRangeException(probability, 0, 1)
        }

        return statistics()?.let { t ->
            Math.abs(t) <= TDistribution(points.size.toDouble() - 2).inverseCumulativeProbability(probability)
        }
    }
}