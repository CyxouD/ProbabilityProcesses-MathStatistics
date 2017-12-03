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

    fun average() = variationalSeriesRows.map { it.result }.average()
    fun averageStandartDeviation() = standartDeviation() / Math.sqrt(size())
    fun median() = variationalSeriesRows.map { it.result }.median()
    fun rootMeanSquare() = variationalSeriesRows.map { it.result }.let { results ->
        Math.sqrt(results.map { it.square() }.sum().div(results.size))
    }

    fun variance() = variationalSeriesRows.map { it.result }.let { results ->
        results.map { result -> (result - average()).square() }.sum().div(results.size.dec())
    }

    /**
     * The asymmetry coefficient
     */
    fun biasedSkewness() = variationalSeriesRows.map { it.result }.let { results ->
        results.map { result ->
            Math.pow(result - average(), 3.0)
        }.sum().div(results.size * Math.pow(standartDeviation(), 3.0))
    }

    fun unbiasedSkewness(): Double {
        return biasedSkewness() * Math.sqrt(size() * size().dec()).div(size() - 2)
    }

    fun skewnessStandartDeviation() = Math.sqrt(
            6 * (size() - 2) / size().inc() / (size() + 3)
    )

    fun biasedKurtosis() = variationalSeriesRows.map { it.result }.let { results ->
        results.map { result ->
            Math.pow(result - average(), 4.0)
        }.sum().div(results.size * Math.pow(standartDeviation(), 4.0))
    }

    fun unbiasedKurtosis(): Double {
        val size = size()
        return size.square().dec().div((size - 2) * (size - 3)) * ((biasedKurtosis() - 3) + (6 / size.inc()))
    }

    fun kurtosisStandartDeviation() = Math.sqrt(
            24 * size() * (size() - 2) * (size() - 3)
                    / (size().inc().square()) / (size() + 3) / (size() + 5)
    )

    fun antiKurtosis() = 1.div(Math.sqrt(Math.abs(biasedKurtosis())))
    fun antiKurtosisStandartDeviation() = Math.sqrt(biasedKurtosis().div(29 * size())) *
            Math.pow(Math.pow(Math.abs(biasedKurtosis().square().dec()), 3.0), 1 / 4.0)

    fun standartDeviation() = Math.sqrt(variance())
    fun standartDeviationStandartDeviation() = standartDeviation().div(Math.sqrt(2 * size()))

    /**
     * Coefficient of variation
     */
    fun cv() = standartDeviation() / average()

    fun cvStandartDeviation() = cv() * Math.sqrt(2 * cv().square().inc().div(2 * size()))


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
