package identification_and_recovery_of_distributions

import javafx.geometry.Point2D
import primary_statistical_analysis.VariationalSeries
import primary_statistical_analysis.square
import java.awt.Point

/**
 * Created by Cyxou on 12/11/17.
 */
//    методом моментов
class UnionDistribution {
    private fun densityFunction(a: Double, b: Double) = 1 / (b - a) * 1.6542857142857144

    private fun coordinates(a: Double, b: Double, classWidth: Double, N: Int): List<Point2D> {
        val yCoordinate = densityFunction(a, b) * classWidth * N
        return listOf(Point2D(a, yCoordinate), Point2D(b, yCoordinate))
    }

    private fun coordinates2(a: Double, b: Double): List<Point2D> {
        return listOf(Point2D(a, 0.0), Point2D(a, 0.0))
    }

    fun normalizedDensityFunctionCoordinates(variationalSeries: VariationalSeries, classWidth: Double) = coordinates(
            ocenkaA(variationalSeries),
            ocenkaB(variationalSeries),
            classWidth,
            variationalSeries.orderedSample.size
    )

    fun distributionFunctionCoordinates(variationalSeries: VariationalSeries, classesN: Int)
            = listOf(Point2D(0.0, 0.0),
            Point2D(ocenkaA(variationalSeries), 0.0),
            Point2D(ocenkaB(variationalSeries), 1.0),
            Point2D(ocenkaB(variationalSeries) + Math.abs(ocenkaA(variationalSeries)), 1.0))

    //    fun M1(variationalSeries: VariationalSeries) = variationalSeries.Average().unBiasedValue()
//    fun M2(variationalSeries: VariationalSeries) = variationalSeries.variationalSeriesRows.map { it.result }.let { results ->
//        results.map { it.square() }.sum().div(results.size)
//    }
//
//    методом моментов

    fun getParametrsOcenki(variationalSeries: VariationalSeries) = listOf(
            OcenkaParametra("a",
                    ocenkaA(variationalSeries),
                    variationOcenkaA(variationalSeries),
                    confidenceIntervalOcenkaA(variationalSeries)),
            OcenkaParametra("b",
                    ocenkaB(variationalSeries),
                    variationOcenkaB(variationalSeries),
                    confidenceIntervalOcenkaB(variationalSeries))
    )

    fun ocenkaA(variationalSeries: VariationalSeries) = variationalSeries.Average().unBiasedValue()!! -
            Math.sqrt(3.0) * variationalSeries.StandartDeviation().unBiasedValue()!!

    fun ocenkaB(variationalSeries: VariationalSeries) = variationalSeries.Average().unBiasedValue()!! +
            Math.sqrt(3.0) * variationalSeries.StandartDeviation().unBiasedValue()!!


    fun confidenceIntervalOcenkaA(variationalSeries: VariationalSeries): VariationalSeries.DoubleRange {
        val difference = 1.96.times(Math.sqrt(variationOcenkaA(variationalSeries)))
        return VariationalSeries.DoubleRange(
                ocenkaA(variationalSeries).minus(difference),
                ocenkaA(variationalSeries).plus(difference))
    }

    fun confidenceIntervalOcenkaB(variationalSeries: VariationalSeries): VariationalSeries.DoubleRange {
        val difference = 1.96.times(Math.sqrt(variationOcenkaB(variationalSeries)))
        return VariationalSeries.DoubleRange(
                ocenkaB(variationalSeries).minus(difference),
                ocenkaB(variationalSeries).plus(difference))
    }


    /**
     * @return возвращает D{a} - дисперсию оценки a
     */
    fun variationOcenkaA(variationalSeries: VariationalSeries): Double {
        val samples = variationalSeries.unorderedSample
        val N = variationalSeries.N
        val average = variationalSeries.Average().unBiasedValue()!!
        return (1.0.plus(
                Math.sqrt(3.0.div(N - 1))
                        .times(samples.map { xi -> xi - average }.sum())
                        .div(Math.sqrt(samples.map { xi -> (xi - average).square() }.sum()))
        )).square()
                .times(samples.map { it.square() }.sum().minus(average.square()).div(N))
    }

    /**
     * @return возвращает D{b} - дисперсию оценки b
     */
    fun variationOcenkaB(variationalSeries: VariationalSeries): Double {
        val samples = variationalSeries.unorderedSample
        val N = samples.size
        val average = variationalSeries.Average().unBiasedValue()!!
        return (1.0.minus(
                Math.sqrt(3.0.div(N - 1))
                        .times(samples.map { xi -> xi - average }.sum())
                        .div(Math.sqrt(samples.map { xi -> (xi - average).square() }.sum()))
        )).square()
                .times(samples.map { it.square() }.sum().minus(average.square()).div(N))
    }

    data class OcenkaParametra(val param: String, val ocenka: Double, val ocenkaStandartDeviation: Double, val confidenceInterval: VariationalSeries.DoubleRange)
}