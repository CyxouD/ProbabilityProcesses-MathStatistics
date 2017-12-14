package primary_statistical_analysis

import primary_statistical_analysis.Main.Companion.preciseFloatingPoints

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
        0 -> VariationalSeries(listOf())
        1 -> VariationalSeries(listOf(orderedSample.single()))
        else -> {
            //first quartile
            val Q1 = findMedian(orderedSample.toList().dropLast(N.toInt() / 2))
            //third quartile
            val Q3 = findMedian(orderedSample.drop(N.toInt() / 2))
            val a = Q1 - k * (Q3 - Q1)
            val b = Q3 + k * (Q3 - Q1)
            val notExcludedSamples = unorderedSample.filter { it in a..b }
            VariationalSeries(notExcludedSamples)

        }
    }


    fun getEmpiricalDistributionFunction(value: Double) =
            variationalSeriesRows.filter { it.result <= value }
                    .map { it.relativeFrequency }
                    .sum()


    fun median() = orderedSample.let { results ->
        if (N != 0.0) {
            findMedian(results)
        } else {
            null
        }
    }

    fun rootMeanSquare() = variationalSeriesRows.map { it.result }.let { results ->
        Math.sqrt(results.map { it.square() }.sum().div(results.size))
    }

    inner class Variance : SampleCharacteristic() {
        override fun biasedValue() = orderedSample.let { results ->
            when (N) {
                0.0 -> null
                1.0 -> 0.0
                else -> deviationSum(results).div(results.size)
            }
        }

        override fun unBiasedValue() = orderedSample.let { results ->
            when (N) {
                0.0 -> null
                1.0 -> 0.0
                else -> deviationSum(results).div(results.size.dec())
            }
        }

        override fun standartDeviation() = unBiasedValue()!!.div(Math.sqrt(2 * N)).square()


        private fun deviationSum(results: List<Double>) = results.map { result -> (result - Average().unBiasedValue()!!).square() }.sum()
    }

    inner class Average : SampleCharacteristic() {
        override fun biasedValue() = if (N != 0.0) {
            orderedSample.average()
        } else null


        override fun unBiasedValue() = biasedValue()

        override fun standartDeviation() = if (N != 0.0) {
            StandartDeviation().unBiasedValue()!! / Math.sqrt(N)
        } else {
            null
        }

    }

    inner class Skewness : SampleCharacteristic() {
        override fun biasedValue() = if (N > 1.0) {
            orderedSample.let { results ->
                results.map { result ->
                    Math.pow(result - Average().unBiasedValue()!!, 3.0)
                }.sum().div(results.size * Math.pow(StandartDeviation().biasedValue()!!, 3.0))
            }
        } else {
            null
        }


        override fun unBiasedValue() = when (N) {
            0.0, 1.0 -> null
            2.0 -> 0.0
            else -> biasedValue()!! * Math.sqrt(N * N.dec()).div(N - 2)
        }


        override fun standartDeviation() = if (N > 1) {
            Math.sqrt(
                    6 * (N - 2) / N.inc() / (N + 3)
            )
        } else {
            null
        }

    }

    inner class Kurtosis : SampleCharacteristic() {
        override fun biasedValue() = if (N > 1.0) {
            orderedSample.let { results ->
                results.map { result ->
                    Math.pow(result - Average().unBiasedValue()!!, 4.0)
                }.sum().div(results.size * Math.pow(StandartDeviation().biasedValue()!!, 4.0))
            }
        } else {
            null
        }


        override fun unBiasedValue() = if (N > 3) {
            N.square().dec().div((N - 2) * (N - 3)) * ((biasedValue()!! - 3) + (6 / N.inc()))
        } else {
            null
        }

        override fun standartDeviation() = if (N > 3) {
            Math.sqrt(24.div(N) * (1 - (225 / (15 * N + 124))))
        } else {
            null
        }

    }

    inner class AntiKurtosis : SampleCharacteristic() {
        override fun biasedValue() = if (N > 1) {
            1.div(Math.sqrt(Math.abs(Kurtosis().biasedValue()!!)))
        } else {
            null
        }


        override fun unBiasedValue() = biasedValue()

        override fun standartDeviation() = if (N > 1) {
            Math.sqrt(Kurtosis().biasedValue()!!.div(29 * N)) *
                    Math.pow(Math.pow(Math.abs(Kurtosis().biasedValue()!!.square().dec()), 3.0), 1 / 4.0)
        } else {
            null
        }

    }

    inner class StandartDeviation : SampleCharacteristic() {
        override fun biasedValue() = if (N != 0.0) {
            Math.sqrt(Variance().biasedValue()!!)
        } else {
            null
        }

        override fun unBiasedValue() = if (N != 0.0) {
            Math.sqrt(Variance().unBiasedValue()!!)
        } else {
            null
        }

        override fun standartDeviation() = if (N != 0.0) {
            unBiasedValue()!!.div(Math.sqrt(2 * N))
        } else {
            null
        }

    }

    inner class CV : SampleCharacteristic() {
        override fun biasedValue() =
                if (N != 0.0) {
                    StandartDeviation().unBiasedValue()!! / Average().unBiasedValue()!!
                } else {
                    null
                }

        override fun unBiasedValue() = biasedValue()

        override fun standartDeviation() =
                if (N != 0.0) {
                    unBiasedValue()!! * Math.sqrt((2 * unBiasedValue()!!.square() + 1).div(2 * N))
                } else {
                    null
                }

    }

    abstract class SampleCharacteristic {
        abstract fun biasedValue(): Double?
        abstract fun unBiasedValue(): Double?
        abstract fun standartDeviation(): Double?
        fun confidenceInterval() = unBiasedValue()?.let { value ->
            val difference = 1.96 * standartDeviation()!!
            DoubleRange(value - difference, value + difference)
        }

    }

    fun divideAtClasses(classNumber: Int): VariationalSeriesDividedByClasses {
        return if (N > 0) {
            val minValue = orderedSample.min()!!
            val maxValue = orderedSample.max()!!
            val classWidth = maxValue.minus(minValue).div(classNumber)
            val ranges = (1..classNumber).map { index ->
                val startClassValue = minValue + classWidth * (index - 1)
                val endClassValue = if (index != classNumber) {
                    minValue + classWidth * index
                } else maxValue //because of classWidth rounding
                DoubleRange(startClassValue, endClassValue)
            }
            divideAtClasses(ranges)
        } else {
            VariationalSeriesDividedByClasses(listOf())
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

    private fun divideAtClasses(ranges: List<DoubleRange>) = VariationalSeriesDividedByClasses(ranges)

    private fun findMedian(results: List<Double>): Double {
        return if (results.size % 2 == 0) {
            results[results.size / 2].plus(results[(results.size / 2).dec()]).div(2)
        } else {
            results[results.size / 2]
        }
    }

    inner class VariationalSeriesDividedByClasses(ranges: List<DoubleRange>) {

        val variationalSeriesDividedByClasses: List<VariationalClass>

        init {
            variationalSeriesDividedByClasses = ranges.map { range ->
                VariationalClass(range, variationalSeriesRows.filter { it.result in range })
            }
        }


        inner class VariationalClass(val range: DoubleRange, val rows: List<VariationalSeriesRow>) {

            val empiricalDistributionFunction =
                    getEmpiricalDistributionFunction(range.endInclusive) - getEmpiricalDistributionFunction(range.start)
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

    class DoubleRange(override val start: Double, override val endInclusive: Double) :
            ClosedRange<Double>,
            Comparable<DoubleRange> {
        override fun toString(): String {
            return "[${start.toPreciseFloatingPoints(preciseFloatingPoints)};${endInclusive.toPreciseFloatingPoints(3)}]"
        }

        override fun compareTo(other: DoubleRange): Int {
            return this.start.compareTo(other.start).let { startCompare ->
                if (startCompare != 0) startCompare
                else {
                    this.endInclusive.compareTo(other.endInclusive)
                }
            }
        }
    }


}
