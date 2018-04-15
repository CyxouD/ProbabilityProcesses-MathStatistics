package sample_uniformity

import org.junit.Assert

/**
 * Created by Cyxou on 4/16/18.
 */
class TestMannWhitneyTest{
    val firstSample = listOf(10, 3, 18, -1, 20, 10, 3).map { it.toDouble() }
    val secondSample = listOf(15, 7, 0, 10, 25, 9).map { it.toDouble() }
    private val sampleFirst100Elements = (1..100).map { it.toDouble() }
    private val sampleSecond100Elements = (101..200).map { it.toDouble() }

    private val mistakeProbability = 0.05

    @org.junit.Test
    fun testStatistics() {
        val mannWhitneyTest = MannWhitneyTest(firstSample, secondSample)
        val actualValue = java.math.BigDecimal(mannWhitneyTest.statistics)
                .setScale(3, java.math.BigDecimal.ROUND_HALF_EVEN)
        val wantedValue = java.math.BigDecimal.valueOf(-0.286)
        Assert.assertEquals(wantedValue, actualValue)
    }

    @org.junit.Test
    fun testCriterion() {
        val mannWhitneyTest = MannWhitneyTest(firstSample, secondSample)
        val actualValue = mannWhitneyTest.isCriterionTrue(mistakeProbability)
        val wantedValue = true
        Assert.assertEquals(wantedValue, actualValue)
    }

    @org.junit.Test
    fun testCriterionForValuesWithNotIntersectingValues() {
        val mannWhitneyTest = MannWhitneyTest(sampleFirst100Elements, sampleSecond100Elements)
        val actualValue = mannWhitneyTest.isCriterionTrue(mistakeProbability)
        Assert.assertEquals(false, actualValue)
    }

    @org.junit.Test
    fun testCriterionForValuesWithSameSamples() {
        val mannWhitneyTest = MannWhitneyTest(sampleFirst100Elements, sampleFirst100Elements)
        val actualValue = mannWhitneyTest.isCriterionTrue(mistakeProbability)
        Assert.assertEquals(true, actualValue)
    }

}