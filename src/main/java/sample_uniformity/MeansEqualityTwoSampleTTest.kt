package sample_uniformity

import org.apache.commons.math3.distribution.TDistribution
import primary_statistical_analysis.sample_characteristics.Average
import primary_statistical_analysis.sample_characteristics.Variance
import primary_statistical_analysis.square

/**
 * Created by Cyxou on 4/15/18.
 */
class MeansEqualityTwoSampleTTest(firstSample: List<Double>, secondSample: List<Double>) :
        Criterion(firstSample, secondSample) {
    private val varianceFirstSample = Variance(firstSample).unBiasedValue()!!
    private val varianceSecondSample = Variance(secondSample).unBiasedValue()!!
    private val equalVariances = varianceFirstSample == varianceSecondSample //for really it is just an assumption

    override val statistics: Double = run {

        if (equalVariances) {
            val pooledVariance = ((N1 - 1) * varianceFirstSample + (N2 - 1) * varianceSecondSample) / (N1 + N2 - 2)
            calculateStatistics(pooledVariance, pooledVariance)
        } else {
            calculateStatistics(varianceFirstSample, varianceSecondSample)
        }
    }

    override fun isCriterionTrue(mistakeProbability: Double): Boolean? {
        val v = if (equalVariances) {
            N1 + N2 - 2 //in presentation - 1
        } else {
            (varianceFirstSample.square() / N1 + varianceSecondSample.square() / N2).square() /
                    (1.0 / (N1 - 1) * (varianceFirstSample.square() / N1).square() + 1.0 / (N2 - 1) * (varianceSecondSample.square() / N2).square())
        }
        return Math.abs(statistics) <= TDistribution(v).inverseCumulativeProbability(1 - mistakeProbability / 2)
    }

    private fun calculateStatistics(firstSampleVariance: Double, secondSampleVariance: Double) =
            (Average(firstSample).unBiasedValue()!! - Average(secondSample).unBiasedValue()!!) /
                    Math.sqrt(firstSampleVariance / N1 + secondSampleVariance / N2)
}