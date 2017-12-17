package primary_statistical_analysis

import identification_and_recovery_of_distributions.ProbabilityPaper
import java.io.File
import java.util.*
import java.util.regex.Pattern

/**
 * Created by Cyxou on 12/2/17.
 */
val variationalSeriesStack = mutableListOf<VariationalSeries>()

fun main(args: Array<String>) {
    val file = File(args.toList()[0])
    val input = file.readLines().flatMap { it.split(Pattern.compile("\\s\\s")) }.map { it.replace(",", ".").toDouble() }
    Scanner(System.`in`).use { sc ->
        try {
            readInput(sc, input)
        } catch (e: Exception) {
            try {
                e.printStackTrace()
                readInput(sc, input)
            } catch (e: Exception) {
                e.printStackTrace()
                readInput(sc, input)
            }
        }
    }
}

private fun readInput(sc: Scanner, input: List<Double>) {
    while (true) {
        val inputStr = sc.nextLine().split(" ")
        processInput(sc,
                variationalSeriesStack.let { if (it.size > 0) variationalSeriesStack.last() else VariationalSeries(input) },
                inputStr)
    }
}

private fun processInput(sc: Scanner, lastVariationalSeries: VariationalSeries, inputStr: List<String>) {
//    val ranges = inputStr.drop(1).map { strRange ->
//        val (start, endInclusive) = Regex("-?[0-9]+").findAll(strRange)
//                .map { it.value }
//                .map { it.toDouble() }
//                .toList()
//        VariationalSeries.DoubleRange(start, endInclusive)
//    }

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
    val (excludedValues, variationalSeries) = lastVariationalSeries.let { variationalSeries ->
        if (excVariable == null) {
            VariationalSeries.ExcludeResult(listOf<Double>(), variationalSeries)
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

    if (excludedValues.isNotEmpty()) {
        println("Excluded ${excludedValues.size} values: $excludedValues")
        println("Exclude them? (Say 'y' or 'n')")
        val exclude = when (sc.nextLine().singleOrNull()?.toLowerCase()) {
            'y' -> {
                true
            }
            'n' -> {
                false
            }
            else -> {
                false
            }
        }
        if (exclude) {
            variationalSeriesStack.add(variationalSeries)
        } else {
            println("No values excluded")
        }
    } else {
        println("No values excluded")
    }
}

class Main {
    companion object {
        val preciseFloatingPoints = 3
    }
}