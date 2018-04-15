package sample_uniformity

import common.Ranks
import org.apache.commons.math3.distribution.NormalDistribution

/**
 * Created by Cyxou on 4/16/18.
 */
class WilcoxonUnpairedTest(firstSample: List<Double>, secondSample: List<Double>) :
        Criterion(firstSample, secondSample) {
    override val statistics: Double = run {
        val pooledSample = firstSample.plus(secondSample)
        val rankedPooledSample = Ranks.ranks(pooledSample)

        val w = rankedPooledSample.take(N1.toInt()).sum()

        val e = 1 / 2.0 * N1 * (N1 + N2 + 1)
        val d = 1 / 12.0 * N1 * N2 * (N1 + N2 + 1)

        (w - e) / Math.sqrt(d)
    }

    override fun isCriterionTrue(mistakeProbability: Double) =
            Math.abs(statistics) <= NormalDistribution().inverseCumulativeProbability(1 - mistakeProbability / 2)
}