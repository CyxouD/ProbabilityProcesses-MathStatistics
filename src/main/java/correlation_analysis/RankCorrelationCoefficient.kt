package correlation_analysis

import common.Ranks
import javafx.geometry.Point2D

/**
 * Created by Cyxou on 4/7/18.
 */
abstract class RankCorrelationCoefficient : CorrelationCoefficient {

    constructor(points: Array<Point2D>) : super(points)
    constructor(points2d: Array<List<Double>>) : super(points2d)

    protected val hasOnlyDistinctElements =
            allX.size == allX.distinct().size && allY.size == allY.distinct().size

    protected fun rx(): List<Double> {
        return Ranks.ranks(allX)
    }

    protected fun ry(): List<Double> {
        return Ranks.ranks(allY)
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
}