package sample_uniformity

import junit.framework.Assert.assertEquals
import org.hamcrest.MatcherAssert.assertThat

/**
 * Created by Cyxou on 4/15/18.
 */
class TestVarianceEqualityFTest {
    private val sample23Elements = (1..23).map { it.toDouble() }
    private val sample18Elements = (1..18).map { it.toDouble() }


    private val sampleFirst100Elements = (1..100).map { it.toDouble() }
    private val sampleSecond100Elements = (101..200).map { it.toDouble() }

    private val mistakeProbability = 0.05

    @org.junit.Test
    fun testStatisticsFirstSampleLargerThanSecond() {
        assertThat("First sample is not larger than second", sample23Elements.size > sample18Elements.size)
        testStatistics(sample23Elements, sample18Elements)
    }

    @org.junit.Test
    fun testStatisticsFirstSampleSmallerThanSecond() {
        assertThat("First sample is not larger than second", sample23Elements.size > sample18Elements.size)
        testStatistics(sample18Elements, sample23Elements)
    }

    @org.junit.Test
    fun testCriterionFirstSampleLargerThanSecond() {
        assertThat("First sample is not larger than second", sample23Elements.size > sample18Elements.size)
        val varianceEqualityFTest = sample_uniformity.VarianceEqualityFTest(sample23Elements, sample18Elements)
        assertEquals(varianceEqualityFTest.isCriterionTrue(mistakeProbability), true)
    }

    @org.junit.Test
    fun testCriterionFirstSampleSmallerThanSecond() {
        assertThat("First sample is not larger than second", sample23Elements.size > sample18Elements.size)
        val varianceEqualityFTest = sample_uniformity.VarianceEqualityFTest(sample18Elements, sample23Elements)
        assertEquals(varianceEqualityFTest.isCriterionTrue(mistakeProbability), true)
    }

    @org.junit.Test
    fun testCriterionForValuesWithEqualRangeAndNumber() {
        val varianceEqualityFTest = sample_uniformity.VarianceEqualityFTest(sampleFirst100Elements, sampleSecond100Elements)
        val actualValue = varianceEqualityFTest.isCriterionTrue(mistakeProbability)
        assertEquals(true, actualValue)
    }

    @org.junit.Test
    fun testCriterionForValuesWithSameSamples() {
        val varianceEqualityFTest = sample_uniformity.VarianceEqualityFTest(sampleFirst100Elements, sampleFirst100Elements)
        val actualValue = varianceEqualityFTest.isCriterionTrue(mistakeProbability)
        assertEquals(true, actualValue)
    }

    private fun testStatistics(sample1: List<Double>, sample2: List<Double>) {
        val varianceEqualityFTest = sample_uniformity.VarianceEqualityFTest(sample1, sample2)
        val actualValue = java.math.BigDecimal(varianceEqualityFTest.statistics)
                .setScale(3, java.math.BigDecimal.ROUND_HALF_EVEN)
        val wantedValue = java.math.BigDecimal.valueOf(1.614)
        assertEquals(wantedValue, actualValue)
    }

}