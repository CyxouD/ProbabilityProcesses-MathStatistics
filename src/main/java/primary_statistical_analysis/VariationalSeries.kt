package primary_statistical_analysis

import primary_statistical_analysis.Main.Companion.preciseFloatingPoints

/**
 * Created by Cyxou on 12/2/17.
 */
class VariationalSeries(sample: List<Double>) {
    val variationalSeriesRows: List<VariationalSeriesRow>
    private val confidenceInterval = 0.95

    init {
        variationalSeriesRows = sample.sorted().map { digit ->
            val result = sample.count { it == digit }
            VariationalSeriesRow(digit, result, result / sample.size.toDouble())
        }
    }


    fun getEmpiricalDistributionFunction(value: Double) =
            variationalSeriesRows.filter { it.result <= value }
                    .map { it.relativeFrequency }
                    .sum()

    
    fun median() = variationalSeriesRows.map { it.result }.let { results ->
        if (size() != 0.0) {
            if (size().toInt() % 2 == 0) {
                results[size().toInt() / 2].plus(results[(size().toInt() / 2).dec()]).div(2)
            } else {
                results[size().toInt() / 2]
            }
        } else {
            null
        }
    }

    fun rootMeanSquare() = variationalSeriesRows.map { it.result }.let { results ->
        Math.sqrt(results.map { it.square() }.sum().div(results.size))
    }

    inner class Variance : SampleCharacteristic() {
        override fun biasedValue() = variationalSeriesRows.map { it.result }.let { results ->
            when (size()) {
                0.0 -> null
                1.0 -> 0.0
                else -> results.map { result -> (result - Average().unBiasedValue()!!).square() }.sum().div(results.size.dec())
            }
        }

        override fun unBiasedValue() = biasedValue()

        override fun standartDeviation() = unBiasedValue()!!.div(Math.sqrt(2 * size())).square()
    }

    inner class Average : SampleCharacteristic() {
        override fun biasedValue() = if (size() != 0.0) {
            variationalSeriesRows.map { it.result }.average()
        } else null


        override fun unBiasedValue() = biasedValue()

        override fun standartDeviation() = if (size() != 0.0) {
            StandartDeviation().unBiasedValue()!! / Math.sqrt(size())
        } else {
            null
        }

    }

    inner class Skewness : SampleCharacteristic() {
        override fun biasedValue() = if (size() > 1.0) {
            variationalSeriesRows.map { it.result }.let { results ->
                results.map { result ->
                    Math.pow(result - Average().unBiasedValue()!!, 3.0)
                }.sum().div(results.size * Math.pow(StandartDeviation().unBiasedValue()!!, 3.0))
            }
        } else {
            null
        }


        override fun unBiasedValue() = when (size()) {
            0.0, 1.0 -> null
            2.0 -> 0.0
            else -> biasedValue()!! * Math.sqrt(size() * size().dec()).div(size() - 2)
        }


        override fun standartDeviation() = if (size() > 1) {
            Math.sqrt(
                    6 * (size() - 2) / size().inc() / (size() + 3)
            )
        } else {
            null
        }

    }

    inner class Kurtosis : SampleCharacteristic() {
        override fun biasedValue() = if (size() > 1.0) {
            variationalSeriesRows.map { it.result }.let { results ->
                results.map { result ->
                    Math.pow(result - Average().unBiasedValue()!!, 4.0)
                }.sum().div(results.size * Math.pow(StandartDeviation().unBiasedValue()!!, 4.0))
            }
        } else {
            null
        }


        override fun unBiasedValue() = if (size() > 3) {
            size().square().dec().div((size() - 2) * (size() - 3)) * ((biasedValue()!! - 3) + (6 / size().inc()))
        } else {
            null
        }

        override fun standartDeviation() = if (size() > 3) {
            Math.sqrt(
                    24 * size() * (size() - 2) * (size() - 3)
                            / (size().inc().square()) / (size() + 3) / (size() + 5)
            )
        } else {
            null
        }

    }

    inner class AntiKurtosis : SampleCharacteristic() {
        override fun biasedValue() = if (size() > 1) {
            1.div(Math.sqrt(Math.abs(Kurtosis().biasedValue()!!)))
        } else {
            null
        }


        override fun unBiasedValue() = biasedValue()

        override fun standartDeviation() = if (size() > 1) {
            Math.sqrt(Kurtosis().biasedValue()!!.div(29 * size())) *
                    Math.pow(Math.pow(Math.abs(Kurtosis().biasedValue()!!.square().dec()), 3.0), 1 / 4.0)
        } else {
            null
        }

    }

    inner class StandartDeviation : SampleCharacteristic() {
        override fun biasedValue() = if (size() != 0.0) {
            Math.sqrt(Variance().biasedValue()!!)
        } else {
            null
        }

        override fun unBiasedValue() = biasedValue()

        override fun standartDeviation() = if (size() != 0.0) {
            unBiasedValue()!!.div(Math.sqrt(2 * size()))
        } else {
            null
        }

    }

    inner class CV : SampleCharacteristic() {
        override fun biasedValue() =
                if (size() != 0.0) {
                    StandartDeviation().unBiasedValue()!! / Average().unBiasedValue()!!
                } else {
                    null
                }

        override fun unBiasedValue() = biasedValue()

        override fun standartDeviation() =
                if (size() != 0.0) {
                    biasedValue()!! * Math.sqrt(2 * biasedValue()!!.square().inc().div(2 * size()))
                } else {
                    null
                }

    }

    abstract class SampleCharacteristic {
        abstract fun biasedValue(): Double?
        abstract fun unBiasedValue(): Double?
        abstract fun standartDeviation(): Double?
        fun confidenceInterval() = biasedValue()?.let { value ->
            val difference = 1.96 * standartDeviation()!!
            DoubleRange(value - difference, value + difference)
        }

    }

    fun divideAtClasses(ranges: List<DoubleRange>) = VariationalSeriesDividedByClasses(ranges)

    private fun size() = variationalSeriesRows.size.toDouble()

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

    class DoubleRange(override val start: Double, override val endInclusive: Double) : ClosedRange<Double> {
        override fun toString(): String {
            return "[${start.toPreciseFloatingPoints(preciseFloatingPoints)};${endInclusive.toPreciseFloatingPoints(3)}]"
        }
    }


}
