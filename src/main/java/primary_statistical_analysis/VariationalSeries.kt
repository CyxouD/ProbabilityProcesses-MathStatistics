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


    class DoubleRange(override val start: Double, override val endInclusive: Double) : ClosedRange<Double> {
        override fun toString(): String {
            return "[$start;$endInclusive]"
        }
    }

    fun divideAtClasses(ranges: List<DoubleRange>) = VariationalSeriesDividedByClasses(ranges)

    inner class VariationalSeriesDividedByClasses(ranges: List<DoubleRange>) {
        val variationalSeriesDividedByClasses: List<Pair<DoubleRange, List<VariationalSeriesRow>>>

        init {
            variationalSeriesDividedByClasses = ranges.map { range ->
                Pair(range, variationalSeriesRows.filter { it.result in range })
            }
        }

        override fun toString(): String {
            return variationalSeriesDividedByClasses.joinToString(separator = "\n") { (range, rows) ->
                "$range: {\n" +
                        rows.joinToString(separator = "\n") { row -> "\t$row" } +
                        "\n}"
            }
        }
    }


    data class VariationalSeriesRow(val result: Double, val frequency: Int, val relativeFrequency: Double)

    override fun toString(): String {
        return variationalSeriesRows.joinToString("\n")
    }


}
