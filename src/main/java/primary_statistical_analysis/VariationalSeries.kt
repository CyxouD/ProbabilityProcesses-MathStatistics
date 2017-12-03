package primary_statistical_analysis

import org.nield.kotlinstatistics.median
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

    fun Double.square() = Math.pow(this, 2.0)

    fun average() = if (size() != 0.0) {
        variationalSeriesRows.map { it.result }.average()
    } else null

    fun averageStandartDeviation() = if (size() != 0.0) {
        standartDeviation()!! / Math.sqrt(size())
    } else {
        null
    }

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

    fun variance() = variationalSeriesRows.map { it.result }.let { results ->
        when (size()) {
            0.0 -> null
            1.0 -> 0.0
            else -> results.map { result -> (result - average()!!).square() }.sum().div(results.size.dec())
        }
    }

    /**
     * The asymmetry coefficient
     */
    fun biasedSkewness() = if (size() > 1.0) {
        variationalSeriesRows.map { it.result }.let { results ->
            results.map { result ->
                Math.pow(result - average()!!, 3.0)
            }.sum().div(results.size * Math.pow(standartDeviation()!!, 3.0))
        }
    } else {
        null
    }

    fun unbiasedSkewness() = when (size()) {
        0.0, 1.0 -> null
        2.0 -> 0.0
        else -> biasedSkewness()!! * Math.sqrt(size() * size().dec()).div(size() - 2)
    }

    fun skewnessStandartDeviation() = if (size() > 1) {
        Math.sqrt(
                6 * (size() - 2) / size().inc() / (size() + 3)
        )
    } else {
        null
    }

    fun biasedKurtosis() = if (size() > 1.0) {
        variationalSeriesRows.map { it.result }.let { results ->
            results.map { result ->
                Math.pow(result - average()!!, 4.0)
            }.sum().div(results.size * Math.pow(standartDeviation()!!, 4.0))
        }
    } else {
        null
    }

    fun unbiasedKurtosis() = if (size() > 3) {
        size().square().dec().div((size() - 2) * (size() - 3)) * ((biasedKurtosis()!! - 3) + (6 / size().inc()))
    } else {
        null
    }

    fun kurtosisStandartDeviation() = if (size() > 3) {
        Math.sqrt(
                24 * size() * (size() - 2) * (size() - 3)
                        / (size().inc().square()) / (size() + 3) / (size() + 5)
        )
    } else {
        null
    }

    fun antiKurtosis() = if (size() > 1) {
        1.div(Math.sqrt(Math.abs(biasedKurtosis()!!)))
    } else {
        null
    }

    fun antiKurtosisStandartDeviation() = if (size() > 1) {
        Math.sqrt(biasedKurtosis()!!.div(29 * size())) *
                Math.pow(Math.pow(Math.abs(biasedKurtosis()!!.square().dec()), 3.0), 1 / 4.0)
    } else {
        null
    }

    fun standartDeviation() = if (size() != 0.0) {
        Math.sqrt(variance()!!)
    } else {
        null
    }

    fun standartDeviationStandartDeviation() = if (size() != 0.0) {
        standartDeviation()!!.div(Math.sqrt(2 * size()))
    } else {
        null
    }

    /**
     * Coefficient of variation
     */
    fun cv() = if (size() != 0.0) {
        standartDeviation()!! / average()!!
    } else {
        null
    }

    fun cvStandartDeviation() = if (size() != 0.0) {
        cv()!! * Math.sqrt(2 * cv()!!.square().inc().div(2 * size()))
    } else {
        null
    }


    fun divideAtClasses(ranges: List<DoubleRange>) = VariationalSeriesDividedByClasses(ranges)

    fun sampleCharacteristicsConfidenceInterval(sampleCharacteristic: Double, sampleCharacteristicStandartDeviation: Double): DoubleRange {
        val difference = 1.96 * sampleCharacteristicStandartDeviation
        return DoubleRange(sampleCharacteristic - difference, sampleCharacteristic + difference)
    }

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
