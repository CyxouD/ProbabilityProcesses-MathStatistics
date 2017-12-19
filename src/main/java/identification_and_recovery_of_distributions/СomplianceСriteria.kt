package identification_and_recovery_of_distributions

import primary_statistical_analysis.VariationalSeries
import primary_statistical_analysis.square

/**
 * Created by Cyxou on 12/18/17.
 */
object СomplianceСriteria {
    fun qPirson95PercentConfidenceInterval(classNumber: Int): Double {
        val v = (classNumber - 1).toDouble()
        val standartNormalDistributionAlpha5Percent = 1.67
        val samePart = 2 / (9 * v)
        return v * Math.pow(1 - samePart + standartNormalDistributionAlpha5Percent * Math.sqrt(samePart), 3.0)
    }

    fun pirson(variationalSeries: VariationalSeries,
               variationalSeriesDividedByClasses: VariationalSeries.VariationalSeriesDividedByClasses,
               N: Int,
               distribution: UnionDistribution): Pair<Double, Double> {
        val ocenkaA = distribution.ocenkaA(variationalSeries)
        val ocenkaB = distribution.ocenkaB(variationalSeries)
        val classN = variationalSeriesDividedByClasses.variationalSeriesDividedByClasses.size
        return Pair(variationalSeriesDividedByClasses.variationalSeriesDividedByClasses.map { variationalClass ->
            val ni = variationalClass.frequency
            val rightX = variationalClass.range.endInclusive
            val leftX = variationalClass.range.start
            val pi = distribution.distributionFunction(rightX, ocenkaA, ocenkaB) -
                    distribution.distributionFunction(leftX, ocenkaA, ocenkaB)
            val theoreticFrequency = N * pi
            (ni - theoreticFrequency).square() / theoreticFrequency
        }.sum(), qPirson95PercentConfidenceInterval(classN))
    }
}