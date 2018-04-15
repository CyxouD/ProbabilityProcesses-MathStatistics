package sample_uniformity

import junit.framework.Assert.assertEquals

/**
 * Created by Cyxou on 4/15/18.
 */
class TestWilcoxonSignedRankPairedTest {
    val firstSample = listOf(125, 115, 130, 140, 140, 115, 140, 125, 140, 135).map { it.toDouble() }
    val secondSample = listOf(110, 122, 125, 120, 140, 124, 123, 137, 135, 145).map { it.toDouble() }

    val thirdSample = listOf(3, 9, 5, 8, 5, 6, 9, 7).map { it.toDouble() }
    val fouthSample = listOf(5, 4, 5, 9, 6, 10, 3, 8).map { it.toDouble() }

    private val sampleFirst100Elements = (1..100).map { it.toDouble() }
    private val sampleSecond100Elements = (101..200).map { it.toDouble() }

    private val mistakeProbability = 0.05


    @org.junit.Test
    fun testStatistics1() {
        val wilcoxonSignedRankTest = WilcoxonSignedRankPairedTest(firstSample, secondSample)
        val actualValue = java.math.BigDecimal(wilcoxonSignedRankTest.statistics)
                .setScale(4, java.math.BigDecimal.ROUND_HALF_EVEN)
        val wantedValue = java.math.BigDecimal.valueOf(0.5331)
        assertEquals(wantedValue, actualValue)
    }

    @org.junit.Test
    fun testStatistics2() {
        val wilcoxonSignedRankTest = WilcoxonSignedRankPairedTest(thirdSample, fouthSample)
        val actualValue = java.math.BigDecimal(wilcoxonSignedRankTest.statistics)
                .setScale(3, java.math.BigDecimal.ROUND_HALF_EVEN)
        val wantedValue = java.math.BigDecimal.valueOf(-.169)
        assertEquals(wantedValue, actualValue)
    }

    @org.junit.Test
    fun testCriterionForStatistics2() {
        val wilcoxonSignedRankTest = WilcoxonSignedRankPairedTest(thirdSample, fouthSample)
        val actualValue = wilcoxonSignedRankTest.isCriterionTrue(mistakeProbability)
        val wantedValue = true
        assertEquals(wantedValue, actualValue)
    }

    @org.junit.Test
    fun testCriterionForValuesWithNotIntersectingValues() {
        val wilcoxonSignedRankPairedTest = WilcoxonSignedRankPairedTest(sampleFirst100Elements, sampleSecond100Elements)
        val actualValue = wilcoxonSignedRankPairedTest.isCriterionTrue(mistakeProbability)
        assertEquals(false, actualValue)
    }

    @org.junit.Test
    fun testCriterionForValuesWithSameSamples() {
        val wilcoxonSignedRankPairedTest = WilcoxonSignedRankPairedTest(sampleFirst100Elements, sampleFirst100Elements)
        val actualValue = wilcoxonSignedRankPairedTest.isCriterionTrue(mistakeProbability)
        assertEquals(true, actualValue)
    }
}