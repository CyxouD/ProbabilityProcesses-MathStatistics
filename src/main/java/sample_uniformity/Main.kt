package sample_uniformity

import primary_statistical_analysis.VariationalSeries

/**
 * Created by Cyxou on 4/16/18.
 */
fun main(args: Array<String>) {
    val firstSample = listOf(5, 3, 7).map { it.toDouble() }
    val secondSample = listOf(5, 3, 7).map { it.toDouble() }
    TableDisplaying.sampleUniformityCriterions(firstSample,
            secondSample,
            0.05).createAndShowGUI("Проверка однородности выборок")

    primary_statistical_analysis.TableDisplaying.samplingCharacteristics(VariationalSeries(firstSample))
            .createAndShowGUI("Статистические характеристики первой выборки")
    primary_statistical_analysis.TableDisplaying.samplingCharacteristics(VariationalSeries(secondSample))
            .createAndShowGUI("Статистические характеристики второй выборки")
}
