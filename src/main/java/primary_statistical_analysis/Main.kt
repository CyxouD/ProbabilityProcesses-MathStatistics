package primary_statistical_analysis

import org.jfree.chart.JFreeChart
import primary_statistical_analysis.libs.StdIn
import java.io.File
import java.util.*

/**
 * Created by Cyxou on 12/2/17.
 */
fun main(args: Array<String>) {
    val file = File(args[0])
    val ranges = args.drop(1).map { strRange ->
        val (start, endInclusive) = Regex("-?[0-9]+").findAll(strRange)
                .map { it.value }
                .map { it.toDouble() }
                .toList()
        VariationalSeries.DoubleRange(start, endInclusive)
    }

    val input = file.readLines().map { it.toDouble() }
    val variationalSeries = VariationalSeries(input)
    println(variationalSeries)
    TableDisplaying(variationalSeries).createAndShowGUI()
    val dividedAtClasses = variationalSeries.divideAtClasses(ranges)
    println(dividedAtClasses)
    TableDisplaying(dividedAtClasses).createAndShowGUI()
}
