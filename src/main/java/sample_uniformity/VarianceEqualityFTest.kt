package sample_uniformity

import javafx.geometry.Point2D
import org.apache.commons.math3.distribution.FDistribution
import primary_statistical_analysis.sample_characteristics.Variance
import primary_statistical_analysis.square

/**
 * Created by Cyxou on 4/15/18.
 */
class VarianceEqualityFTest(firstSample: List<Double>, secondSample: List<Double>) :
        Criterion(firstSample, secondSample) {
    val unBiasedVarianceX = Variance(firstSample).unBiasedValue()!!
    val unBiasedVarianceY = Variance(secondSample).unBiasedValue()!!

    override val statistics: Double = run {
        Math.max((unBiasedVarianceX / unBiasedVarianceY), (unBiasedVarianceY / unBiasedVarianceX))
    }

    override fun isCriterionTrue(mistakeProbability: Double): Boolean? {
        val (v1, v2) = if (unBiasedVarianceX >= unBiasedVarianceY) Pair(N1 - 1, N2 - 1) else Pair(N2 - 1, N1 - 1)
        return statistics < FDistribution(v1, v2).inverseCumulativeProbability(1 - mistakeProbability / 2)
    }
}