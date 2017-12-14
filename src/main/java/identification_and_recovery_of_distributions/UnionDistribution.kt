package identification_and_recovery_of_distributions

import javafx.geometry.Point2D
import primary_statistical_analysis.VariationalSeries
import primary_statistical_analysis.square

/**
 * Created by Cyxou on 12/11/17.
 */
//    методом моментов
class UnionDistribution {
//    private fun densityFunction(a: Double, b: Double) = 1  / (b - a) * 1.6542857142857144

//    fun coordinates(a: Double, b: Double) = listOf(Point2D(a, densityFunction(a, b)), Point2D(b, densityFunction(a, b)))

//    fun coordinates(variationalSeries: VariationalSeries) = coordinates(
//            variationalSeries.variationalSeriesRows.map { it.result }.min()!!,
//            variationalSeries.variationalSeriesRows.map { it.result }.max()!!
//    )

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
        val samples = variationalSeries.sample
        val N = samples.size
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
        val samples = variationalSeries.sample
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