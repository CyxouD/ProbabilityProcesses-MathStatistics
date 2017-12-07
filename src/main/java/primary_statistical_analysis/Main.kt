package primary_statistical_analysis

import java.io.File

/**
 * Created by Cyxou on 12/2/17.
 */
fun main(args: Array<String>) {
    val file = File(args[0])
//    val ranges = args.drop(1).map { strRange ->
//        val (start, endInclusive) = Regex("-?[0-9]+").findAll(strRange)
//                .map { it.value }
//                .map { it.toDouble() }
//                .toList()
//        VariationalSeries.DoubleRange(start, endInclusive)
//    }
    val input = file.readLines().map { it.replace(",", ".").toDouble() }
    val enteredClassNumber = if (args.size > 1) {
        args[1].toInt()
    } else {
        null
    }
    val variationalSeries = VariationalSeries(input)
    println(variationalSeries)
    TableDisplaying.variationSeries(variationalSeries).createAndShowGUI()
    TableDisplaying.samplingCharacteristics(variationalSeries).createAndShowGUI()
    Chart.empericalDistributionFunctionVariationSeries(variationalSeries).createAndShowGUI()
    val dividedAtClasses = if (enteredClassNumber != null) {
        variationalSeries.divideAtClasses(enteredClassNumber)
    } else {
        variationalSeries.divideAtClasses()
    }
    println(dividedAtClasses)
    TableDisplaying.variationSeriesByClasses(dividedAtClasses).createAndShowGUI()
    Chart.histogramVariationSeriesByClasses(dividedAtClasses).createAndShowGUI()
    Chart.empericalDistributionFunctionSeriesByClasses(dividedAtClasses).createAndShowGUI()
}

class Main {
    companion object {
        val preciseFloatingPoints = 3
    }
}