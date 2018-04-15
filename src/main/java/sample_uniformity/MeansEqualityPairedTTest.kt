package sample_uniformity

import org.apache.commons.math3.distribution.TDistribution
import org.hamcrest.MatcherAssert.assertThat
import primary_statistical_analysis.sample_characteristics.Average
import primary_statistical_analysis.sample_characteristics.StandartDeviation

/**
 * Created by Cyxou on 4/15/18.
 */
class MeansEqualityPairedTTest(firstSample: List<Double>, secondSample: List<Double>) :
        Criterion(firstSample, secondSample) {
    init {
        assertThat("Samples must have equal size", firstSample.size == secondSample.size)
    }

    companion object {
        private val nanValueReplacement = Double.POSITIVE_INFINITY
    }

    override val statistics: Double = run {
        val zSample = firstSample.mapIndexed { i: Int, value: Double -> value - secondSample[i] }
        val zAverage = Average(zSample).unBiasedValue()!!
        val sz = StandartDeviation(zSample).unBiasedValue()!!
        val result = zAverage * Math.sqrt(N1) / sz
        if (!result.isNaN()) result else nanValueReplacement
    }

    override fun isCriterionTrue(mistakeProbability: Double): Boolean {
        return Math.abs(statistics) <= TDistribution(N1 - 1).inverseCumulativeProbability(1 - mistakeProbability / 2) ||
                statistics == nanValueReplacement //not expected by formula value
    }

}