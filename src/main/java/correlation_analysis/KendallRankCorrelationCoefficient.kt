package correlation_analysis

import javafx.geometry.Point2D
import org.apache.commons.math3.distribution.NormalDistribution
import org.apache.commons.math3.exception.OutOfRangeException

/**
 * Created by Cyxou on 4/7/18.
 */
class KendallRankCorrelationCoefficient : RankCorrelationCoefficient {

    constructor(points: Array<Point2D>) : super(points)
    constructor(points2d: Array<List<Double>>) : super(points2d)

    override val coefficient: Double? = run {
        val ry = ry()
        val sortedByRx = rx().mapIndexed { index: Int, rxi: Double -> Pair(rxi, ry[index]) }.sortedBy { (rx, ry) -> rx }
        val part = 1 / 2.0 * N * (N - 1)
        if (hasOnlyDistinctElements) {
            val S = (1..N.toInt()).map { it }.combineUnique()
                    .map { (i, j) ->
                        val (_, ryi) = sortedByRx[i - 1]
                        val (_, ryj) = sortedByRx[j - 1]
                        Pair(ryi, ryj)
                    }.map { (ryi, ryj) -> if (ryi < ryj) 1 else -1 }
                    .sum()

            S / part
        } else {
            val S = (1..N.toInt()).map { it }.combineUnique()
                    .map { (i, j) ->
                        val (rxi, ryi) = sortedByRx[i - 1]
                        val (rxj, ryj) = sortedByRx[j - 1]
                        Pair(Pair(rxi, ryi), Pair(rxj, ryj))
                    }.map { (iPair, jPair) ->
                val (rxi, ryi) = iPair
                val (rxj, ryj) = jPair
                when {
                    ryi < ryj && rxi != rxj -> 1
                    ryi > ryj && rxi != rxj -> -1
                    else -> 0
                }
            }.sum()

            val C = CorD(allX)
            val D = CorD(allY)
            S / Math.sqrt((part - C) * (part - D))
        }
    }

    private fun CorD(elements: List<Double>): Double {
        val groupedElements = groupedElements(elements)
        return 1 / 2.0 * (1..groupedElements.size).map { index ->
            val element = AjOrBj(groupedElements, index)
            element * (element - 1)
        }.sum()
    }


    override val statistics = coefficient?.let { coefficient -> coefficient * Math.sqrt(9 * N * (N - 1)) / Math.sqrt(2 * (2 * N + 5)) }
    override fun isSignificant(mistakeProbability: Double): Boolean? {
        if (mistakeProbability < 0.0 || mistakeProbability > 1.0) {
            throw OutOfRangeException(mistakeProbability, 0, 1)
        }

        return statistics?.let { u ->
            Math.abs(u) > NormalDistribution().inverseCumulativeProbability(1 - mistakeProbability / 2)
        }
    }
}