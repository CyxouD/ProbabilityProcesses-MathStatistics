package correlation_analysis

import javafx.geometry.Point2D
import org.apache.commons.math3.distribution.TDistribution
import org.apache.commons.math3.exception.OutOfRangeException
import primary_statistical_analysis.DoubleRange
import primary_statistical_analysis.square

/**
 * Created by Cyxou on 4/3/18.
 */
class SpearmanRankCorrelationCoefficient : CorrelationCoefficient {

    private val allX = points.map { (x, _) -> x }
    private val allY = points.map { (_, y) -> y }

    constructor(points: Array<Point2D>) : super(points)
    constructor(points2d: Array<List<Double>>) : super(points2d)


    override val coefficient: Double? = run {
        val onlyDistinctElements = run {
            allX.size == allX.distinct().size && allY.size == allY.distinct().size
        }

        val sum = rx().mapIndexed { index: Int, rxi: Double -> (rxi - ry()[index]) }.map { it.square() }.sum()
        if (onlyDistinctElements) {
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
        val groupedElements = elements.sorted().groupBy { number -> number }
                .filter { (_, equalNumbers) -> equalNumbers.size > 1 }
                .map { (number, equalNumbers) -> Pair(number, equalNumbers.size) }
                .mapIndexed { index, (_, equalNumbersSize) -> Pair(index, equalNumbersSize) }.toMap()
        return 1 / 12.0 * (1..groupedElements.size).map { index ->
            val aj = groupedElements[index - 1]!!.toDouble()
            Math.pow(aj, 3.0) - aj
        }.sum()
    }

    override fun coefficientConfidenceInterval(probability: Double): DoubleRange? {
        throw Exception()
    }

    override fun isSignificant(probability: Double): Boolean? {
        if (probability < 0.0 || probability > 1.0) {
            throw OutOfRangeException(probability, 0, 1)
        }

        return statistics?.let { t ->
            Math.abs(t) > TDistribution(points.size.toDouble() - 2).inverseCumulativeProbability(probability)
        }
    }

    private fun rx(): List<Double> {
        val sortedRanks = sortedRanks(allX)

        return allX.map { x -> sortedRanks[x]!! }
    }

    private fun ry(): List<Double> {
        val sortedRanks = sortedRanks(allY)
        return allY.map { y -> sortedRanks[y]!! }
    }

    private fun sortedRanks(values: List<Double>): Map<Double, Double> {
        val sorted = values.sorted()
        val sortedRanks = sorted.map { number ->
            val indexNumberOfFirstNumber = sorted.indexOfFirst { it == number } + 1
            val indexNumberOfLastNumber = sorted.indexOfLast { it == number } + 1
            val identicalNumbers = indexNumberOfLastNumber - indexNumberOfFirstNumber + 1
            val rank = (indexNumberOfFirstNumber..indexNumberOfLastNumber).fold(0, { acc, next -> acc + next }) / identicalNumbers.toDouble()
            Pair(number, rank)
        }.toMap()
        return sortedRanks
    }

}