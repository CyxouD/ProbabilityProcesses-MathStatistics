package correlation_analysis

import common.Utils
import javafx.geometry.Point2D
import org.apache.commons.math3.distribution.FDistribution
import org.apache.commons.math3.exception.OutOfRangeException
import primary_statistical_analysis.DoubleRange
import primary_statistical_analysis.VariationalSeries
import primary_statistical_analysis.sample_characteristics.Average
import primary_statistical_analysis.square

/**
 * Created by Cyxou on 4/8/18.
 */
class CorrelationRatio : CorrelationCoefficient {

    private val classNumber = Math.ceil(1 + 1.44 * Math.log(N)).toInt()

    constructor(points: Array<Point2D>) : super(points)
    constructor(points2d: Array<List<Double>>) : super(points2d)

    override val coefficient = run {
        val (_, ranges) = Utils.rangesByClassNumber(classNumber, allX)
        val xy = allX.mapIndexed { index: Int, rxi: Double -> Pair(rxi, allY[index]) }
        val transitionedValues = xy.groupBy { (x, y) -> ranges.indexOfFirst { range -> x in range } }.map { Pair(it.key, it.value.map { it.second }) }.toMap()

        val averageY = transitionedValues.map { (_, allYOfClass) -> Average(allYOfClass).unBiasedValue()!! * allYOfClass.size }.sum() / N
        val numerator = transitionedValues.map { (_, allYOfClass) -> allYOfClass.size * (Average(allYOfClass).unBiasedValue()!! - averageY).square() }.sum()
        val denominator = transitionedValues.map { (_, allYOfClass) -> allYOfClass.map { it } }.flatMap { it }.map { y -> (y - averageY).square() }.sum()
        Math.sqrt(numerator / denominator)
    }
    override val statistics = coefficient.square() / (classNumber - 1) / ((1 - coefficient.square()) / (N - classNumber))
    override fun isSignificant(mistakeProbability: Double): Boolean? {
        if (mistakeProbability < 0.0 || mistakeProbability > 1.0) {
            throw OutOfRangeException(mistakeProbability, 0, 1)
        }

        return Math.abs(statistics) > FDistribution(classNumber - 1.0, N - classNumber).inverseCumulativeProbability(1 - mistakeProbability)
    }
}