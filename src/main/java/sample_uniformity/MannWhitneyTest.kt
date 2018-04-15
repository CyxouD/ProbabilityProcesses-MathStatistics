package sample_uniformity

import org.apache.commons.math3.distribution.NormalDistribution

/**
 * Created by Cyxou on 4/16/18.
 */
class MannWhitneyTest(firstSample: List<Double>, secondSample: List<Double>) :
        Criterion(firstSample, secondSample) {

    override val statistics: Double = run {
        val u = firstSample.map { xi ->
            secondSample.map { yj ->
                when {
                    xi > yj -> 1.0
                    xi == yj -> 0.5
                    else -> 0.0
                }
            }.sum()
        }.sum()

        val e = 1 / 2.0 * N1 * N2
        val d = 1 / 12.0 * N1 * N2 * (N1 + N2 + 1)

        (u - e) / Math.sqrt(d)
    }

    override fun isCriterionTrue(mistakeProbability: Double) =
            Math.abs(statistics) <= NormalDistribution().inverseCumulativeProbability(1 - mistakeProbability / 2)
}