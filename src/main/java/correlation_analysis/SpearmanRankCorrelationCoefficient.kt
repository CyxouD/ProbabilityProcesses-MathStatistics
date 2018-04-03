package correlation_analysis

import javafx.geometry.Point2D
import primary_statistical_analysis.DoubleRange
import primary_statistical_analysis.square

/**
 * Created by Cyxou on 4/3/18.
 */
class SpearmanRankCorrelationCoefficient : CorrelationCoefficient {

    constructor(points: Array<Point2D>) : super(points)
    constructor(points2d: Array<List<Double>>) : super(points2d)

    override val statistics: Double?
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.

    override val coefficient = {
        val onlyDistinctElements = {
            val allX = points.map { (x, _) -> x }
            val allY = points.map { (_, y) -> y }
            allX.size == allX.distinct().size && allY.size == allY.distinct().size
        }.invoke()

        val N = points.size.toDouble()
        val sum = rx().mapIndexed { index: Int, rxi: Double -> (rxi - ry()[index]) }.map { it.square() }.sum()
        if (onlyDistinctElements) {
            1 - 6 / (N * (N.square() - 1)) * sum
        } else {
            TODO()
            val A =  (1 / 12.0)
            val B = (1 / 12.0)
            (1 / 6.0) * N * (N.square() - 1) - sum - A - B
        }
    }.invoke()

    override fun coefficientConfidenceInterval(probability: Double): DoubleRange? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun isSignificant(probability: Double): Boolean? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private fun rx(): List<Double> {
        val allX = points.map { (x, _) -> x }
        val sortedRanks = sortedRanks(allX)
        return allX.map { x -> sortedRanks[x]!! }
    }

    private fun ry(): List<Double> {
        val allY = points.map { (_, y) -> y }
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