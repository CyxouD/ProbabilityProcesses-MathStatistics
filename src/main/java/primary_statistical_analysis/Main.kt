package primary_statistical_analysis

import com.sun.org.apache.xerces.internal.util.SynchronizedSymbolTable
import identification_and_recovery_of_distributions.ProbabilityPaper
import identification_and_recovery_of_distributions.UnionDistribution
import java.io.Console
import java.io.File
import java.io.FileReader
import java.util.*
import java.util.regex.Pattern

/**
 * Created by Cyxou on 12/2/17.
 */
fun main(args: Array<String>) {
    Scanner(System.`in`).use { sc ->
        try {
            if (args.isNotEmpty()) {
                processInput(args.toList())
            }
            readInput(sc)
        } catch (e: Exception) {
            try {
                e.printStackTrace()
                readInput(sc)
            } catch (e: Exception) {
                e.printStackTrace()
                readInput(sc)
            }
        }
    }
}

private fun readInput(sc: Scanner) {
    while (true) {
        val inputStr = sc.nextLine().split(" ")
        processInput(inputStr)
    }
}

private fun processInput(inputStr: List<String>) {
    val file = File(inputStr[0])
//    val ranges = inputStr.drop(1).map { strRange ->
//        val (start, endInclusive) = Regex("-?[0-9]+").findAll(strRange)
//                .map { it.value }
//                .map { it.toDouble() }
//                .toList()
//        VariationalSeries.DoubleRange(start, endInclusive)
//    }
    val input = file.readLines().flatMap { it.split(Pattern.compile("\\s\\s")) }.map { it.replace(",", ".").toDouble() }

    val classesIndex = inputStr.map { it.toUpperCase() }.indexOf("-M")
    val enteredClassNumber = if (classesIndex != -1) {
        inputStr[classesIndex + 1].toInt()
    } else null
    val excIndex = inputStr.map { it.toUpperCase() }.indexOf("-E")
    val excVariable = if (excIndex != -1) {
        inputStr[excIndex + 1].toDouble()
    } else {
        null
    }
    val variationalSeries = VariationalSeries(input).let { variationalSeries ->
        if (excVariable == null) {
            variationalSeries
        } else {
            variationalSeries.excludeAbnormalValues(excVariable)
        }
    }
//    println(variationalSeries)
    TableDisplaying.variationSeries(variationalSeries).createAndShowGUI()
    TableDisplaying.samplingCharacteristics(variationalSeries).createAndShowGUI()
//    Chart.empericalDistributionFunctionVariationSeries(variationalSeries).createAndShowGUI()
    val dividedAtClasses = if (enteredClassNumber != null) {
        variationalSeries.divideAtClasses(enteredClassNumber)
    } else {
        variationalSeries.divideAtClasses()
    }
//    println(dividedAtClasses)
//    TableDisplaying.variationSeriesByClasses(dividedAtClasses).createAndShowGUI()
    Chart.histogramVariationSeriesByClasses(dividedAtClasses).createAndShowGUI()
//    Chart.empericalDistributionFunctionSeriesByClasses(dividedAtClasses).createAndShowGUI()
    val probabilityPaper = ProbabilityPaper(variationalSeries)
    probabilityPaper.identifyUnionDistribution()
//    TableDisplaying.ocenkiParametrov(variationalSeries, UnionDistribution()).createAndShowGUI()
}

class Main {
    companion object {
        val preciseFloatingPoints = 3
    }
}