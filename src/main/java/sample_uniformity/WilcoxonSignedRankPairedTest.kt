package sample_uniformity

import common.Ranks
import org.apache.commons.math3.distribution.NormalDistribution
import org.hamcrest.MatcherAssert

/**
 * Created by Cyxou on 4/15/18.
 */
class WilcoxonSignedRankPairedTest(firstSample: List<Double>, secondSample: List<Double>) :
        Criterion(firstSample, secondSample) {
    init {
        MatcherAssert.assertThat("Samples must have equal size", firstSample.size == secondSample.size)
    }

    companion object {
        private val nanValueReplacement = Double.POSITIVE_INFINITY
    }

    //safe if zSample.size > 15
    override val statistics: Double = run {
        val zSample = firstSample.mapIndexed { i: Int, value: Double -> value - secondSample[i] }
                .filter { it != 0.0 }
        val s = zSample.map { zi -> if (zi > 0) 1.0 else 0.0 }
        val r = Ranks.ranks(zSample.map { zi -> Math.abs(zi) })

        MatcherAssert.assertThat("s and r must have equal size", s.size == r.size)
        val t = s.mapIndexed { i: Int, value: Double -> value * r[i] }.sum()

        val e = 1 / 4.0 * s.size * (s.size + 1)
        val d = 1 / 24.0 * s.size * (s.size + 1) * (2 * s.size + 1)
        val result = (t - e) / Math.sqrt(d)
        if (!result.isNaN()) result else nanValueReplacement
    }

    override fun isCriterionTrue(mistakeProbability: Double): Boolean =
            Math.abs(statistics) <= NormalDistribution().inverseCumulativeProbability(1 - mistakeProbability / 2) ||
                    statistics == nanValueReplacement//not expected by formula value

}