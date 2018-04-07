package correlation_analysis

import javafx.geometry.Point2D
import org.apache.commons.math3.distribution.TDistribution
import org.apache.commons.math3.exception.OutOfRangeException
import primary_statistical_analysis.square

/**
 * Created by Cyxou on 4/3/18.
 */
class SpearmanRankCorrelationCoefficient : RankCorrelationCoefficient {

    constructor(points: Array<Point2D>) : super(points)
    constructor(points2d: Array<List<Double>>) : super(points2d)

    override val coefficient: Double? = run {
        val sum = rx().mapIndexed { index: Int, rxi: Double -> (rxi - ry()[index]) }.map { it.square() }.sum()
        if (hasOnlyDistinctElements) {
            1 - 6 / (N * (N.square() - 1)) * sum
        } else {
            val A = AorB(allX)
            val B = AorB(allY)
            val part = 1 / 6.0 * N * (N.square() - 1)
            (part - sum - A - B) / Math.sqrt((part - 2 * A) * (part - 2 * B))
        }
    }
    override val statistics: Double? = run {
        coefficient?.let { coefficient ->
            coefficient * Math.sqrt(N - 2) / Math.sqrt(1 - coefficient.square())
        }
    }

    private fun AorB(elements: List<Double>): Double {
        val groupedElements = groupedElements(elements)
        return 1 / 12.0 * (1..groupedElements.size).map { index ->
            val element = AjOrBj(groupedElements, index)
            Math.pow(element, 3.0) - element
        }.sum()
    }

    override fun isSignificant(mistakeProbability: Double): Boolean? {
        if (mistakeProbability < 0.0 || mistakeProbability > 1.0) {
            throw OutOfRangeException(mistakeProbability, 0, 1)
        }

        return statistics?.let { t ->
            Math.abs(t) > TDistribution(points.size.toDouble() - 2).inverseCumulativeProbability(1 - mistakeProbability / 2)
        }
    }

}