package primary_statistical_analysis

/**
 * Created by Cyxou on 12/2/17.
 */
class VariationalSeries(sample: List<Double>) {
    val variationalSeriesRows: List<VariationalSeriesRow>

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


    fun divideAtClasses(ranges: List<DoubleRange>) = VariationalSeriesDividedByClasses(ranges)


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
            return "[$start;$endInclusive]"
        }
    }


}
