package identification_and_recovery_of_distributions

import primary_statistical_analysis.VariationalSeries
import primary_statistical_analysis.square

/**
 * Created by Cyxou on 12/18/17.
 */
object СomplianceСriteria {
    val Percent95ConfidenceIntervalPoisson = listOf(1 to 0.00004,
            2 to 0.01003,
            3 to 0.07172,
            4 to 0.20699,
            5 to 0.41174,
            6 to 0.67573,
            7 to 0.98926,
            8 to 1.34441,
            9 to 1.73493,
            10 to 2.15586,
            11 to 2.60322,
            12 to 3.07382,
            13 to 3.56503,
            14 to 4.07467,
            15 to 4.60092,
            16 to 5.14221,
            17 to 5.69722,
            18 to 6.26480,
            19 to 6.84397,
            20 to 7.43384,
            21 to 8.03365,
            22 to 8.64272,
            23 to 9.26042,
            24 to 9.88623,
            25 to 10.51965,
            26 to 11.16024,
            27 to 11.80759,
            28 to 12.46134,
            29 to 13.12115,
            30 to 13.78672).toMap()

    fun poisson(variationalSeries: VariationalSeries,
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
        }.sum(), Percent95ConfidenceIntervalPoisson[classN - 1]!!)
    }
}