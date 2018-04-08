package primary_statistical_analysis

import common.Utils.rangesByClassNumber
import primary_statistical_analysis.sample_characteristics.Median

/**
 * Created by Cyxou on 12/2/17.
 */
class VariationalSeries(val unorderedSample: List<Double>) {
    val variationalSeriesRows: Set<VariationalSeriesRow>
    val orderedSample = unorderedSample.sorted()
    val N = unorderedSample.size.toDouble()
    private val confidenceInterval = 0.95

    init {
        variationalSeriesRows = unorderedSample.sorted().map { digit ->
            val result = unorderedSample.count { it == digit }
            VariationalSeriesRow(digit, result, result / unorderedSample.size.toDouble())
        }.toSet() //remove not unique value
    }


    fun excludeAbnormalValues(k: Double = 2.5) = when (N.toInt()) {
        0 -> ExcludeResult(listOf(), VariationalSeries(listOf()))
        1 -> ExcludeResult(listOf(), VariationalSeries(listOf(orderedSample.single())))
        else -> {
            //first quartile
            val Q1 = Median.findMedian(orderedSample.toList().dropLast(N.toInt() / 2))
            //third quartile
            val Q3 = Median.findMedian(orderedSample.drop(N.toInt() / 2))
            val a = Q1 - k * (Q3 - Q1)
            val b = Q3 + k * (Q3 - Q1)
            val notExcludedSamples = unorderedSample.filter { it in a..b }
            val excludedSamples = unorderedSample.filter { it !in a..b }
            ExcludeResult(excludedSamples, VariationalSeries(notExcludedSamples))
        }
    }

    class ExcludeResult(val excludedValues: List<Double>, val variationalSeries: VariationalSeries) {
        operator fun component1() = excludedValues
        operator fun component2() = variationalSeries
    }

    fun getEmpiricalDistributionFunction(value: Double) =
            variationalSeriesRows.filter { it.result <= value }
                    .map { it.relativeFrequency }
                    .sum()


    fun rootMeanSquare() = variationalSeriesRows.map { it.result }.let { results ->
        Math.sqrt(results.map { it.square() }.sum().div(results.size))
    }

    fun divideAtClasses(classNumber: Int): VariationalSeriesDividedByClasses {
        return if (N > 0) {
            val (classWidth, ranges) = rangesByClassNumber(classNumber, orderedSample)
            divideAtClasses(classWidth, ranges)
        } else {
            VariationalSeriesDividedByClasses(-1.0, listOf())
        }
    }

    fun divideAtClasses(): VariationalSeriesDividedByClasses {
        val classNumber = if (N < 100) {
            val ceilSqrtFromSize = Math.ceil(Math.sqrt(N)).toInt()
            if (ceilSqrtFromSize % 2 == 0) {
                ceilSqrtFromSize - 1
            } else {
                ceilSqrtFromSize
            }
        } else {
            val ceilCubeSqrtFromSize = Math.ceil(Math.pow(N, 1 / 3.0)).toInt()
            if (ceilCubeSqrtFromSize % 2 == 0) {
                ceilCubeSqrtFromSize - 1
            } else {
                ceilCubeSqrtFromSize
            }
        }
        return divideAtClasses(classNumber)
    }

    private fun divideAtClasses(classWidth: Double, ranges: List<DoubleRange>)
            = VariationalSeriesDividedByClasses(classWidth, ranges)


    inner class VariationalSeriesDividedByClasses(val classWidth: Double, ranges: List<DoubleRange>) {
        val variationalSeriesDividedByClasses: List<VariationalClass>

        init {
            val variationalSeriesDividedByClasses = mutableListOf<VariationalClass>()
            ranges.map { range ->
                val rows = variationalSeriesRows.filter { it.result in range }
                Triple(range, rows, rows.map { it.relativeFrequency }.sum())
            }.fold(mutableListOf<Triple<DoubleRange, List<VariationalSeriesRow>, Double>>(), { acc, (range, rows, classRelativeFrequency) ->
                acc.add(Triple(range, rows, classRelativeFrequency))
                val empiricalDistributionFunction = acc.map { (_, _, classRelativeFrequency) -> classRelativeFrequency }.sum()
                variationalSeriesDividedByClasses.add(VariationalClass(range, rows, empiricalDistributionFunction))
                acc
            })

            this.variationalSeriesDividedByClasses = variationalSeriesDividedByClasses
        }


        inner class VariationalClass(val range: DoubleRange, val rows: List<VariationalSeriesRow>, val empiricalDistributionFunction: Double) {
            val frequency = rows.map { it.frequency }.sum()
            val relativeFrequency = rows.map { it.relativeFrequency }.sum()
        }


        override fun toString(): String {
            return variationalSeriesDividedByClasses.joinToString(separator = "\n") { `class` ->
                "${`class`.range}: {\n" +
                        `class`.rows.joinToString(separator = "\n") { row -> "\t$row" } +
                        "\n}"
            }
        }
    }

    override fun toString(): String {
        return variationalSeriesRows.joinToString("\n")
    }


    data class VariationalSeriesRow(val result: Double, val frequency: Int, val relativeFrequency: Double)


}
