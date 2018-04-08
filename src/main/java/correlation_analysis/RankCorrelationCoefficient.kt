package correlation_analysis

import javafx.geometry.Point2D
import primary_statistical_analysis.DoubleRange

/**
 * Created by Cyxou on 4/7/18.
 */
abstract class RankCorrelationCoefficient : CorrelationCoefficient {

    constructor(points: Array<Point2D>) : super(points)
    constructor(points2d: Array<List<Double>>) : super(points2d)

    protected val hasOnlyDistinctElements =
            allX.size == allX.distinct().size && allY.size == allY.distinct().size

    protected fun rx(): List<Double> {
        val sortedRanks = sortedRanks(allX)

        return allX.map { x -> sortedRanks[x]!! }
    }

    protected fun ry(): List<Double> {
        val sortedRanks = sortedRanks(allY)
        return allY.map { y -> sortedRanks[y]!! }
    }

    protected fun groupedElements(elements: List<Double>): Map<Int, Int> {
        return elements.sorted().groupBy { number -> number }
                .filter { (_, equalNumbers) -> equalNumbers.size > 1 }
                .map { (number, equalNumbers) -> Pair(number, equalNumbers.size) }
                .mapIndexed { index, (_, equalNumbersSize) -> Pair(index, equalNumbersSize) }.toMap()
    }

    protected fun AjOrBj(groupedElements: Map<Int, Int>, index: Int): Double {
        val element = groupedElements[index - 1]!!.toDouble()
        return element
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