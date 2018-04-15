import junit.framework.Assert.assertEquals
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import sample_uniformity.VarianceEqualityFTest
import java.math.BigDecimal

/**
 * Created by Cyxou on 4/15/18.
 */
class TestVarianceEqualityFTest {
    private val sample23Elements = (1..23).map { it.toDouble() }
    private val sample18Elements = (1..18).map { it.toDouble() }


    private val sampleFirst100Elements = (1..100).map { it.toDouble() }
    private val sampleSecond100Elements = (101..200).map { it.toDouble() }

    private val mistakeProbability = 0.05

    @Test
    fun testStatisticsFirstSampleLargerThanSecond() {
        assertThat("First sample is not larger than second", sample23Elements.size > sample18Elements.size)
        testStatistics(sample23Elements, sample18Elements)
    }

    @Test
    fun testStatisticsFirstSampleSmallerThanSecond() {
        assertThat("First sample is not larger than second", sample23Elements.size > sample18Elements.size)
        testStatistics(sample18Elements, sample23Elements)
    }

    @Test
    fun testCriterionFirstSampleLargerThanSecond() {
        assertThat("First sample is not larger than second", sample23Elements.size > sample18Elements.size)
        val varianceEqualityFTest = VarianceEqualityFTest(sample23Elements, sample18Elements)
        assertEquals(varianceEqualityFTest.isCriterionTrue(mistakeProbability), true)
    }

    @Test
    fun testCriterionFirstSampleSmallerThanSecond() {
        assertThat("First sample is not larger than second", sample23Elements.size > sample18Elements.size)
        val varianceEqualityFTest = VarianceEqualityFTest(sample18Elements, sample23Elements)
        assertEquals(varianceEqualityFTest.isCriterionTrue(mistakeProbability), true)
    }

    @Test
    fun testCriterionForValuesWithEqualRangeAndNumber() {
        val varianceEqualityFTest = VarianceEqualityFTest(sampleFirst100Elements, sampleSecond100Elements)
        assertEquals(varianceEqualityFTest.isCriterionTrue(mistakeProbability), true)
    }

    @Test
    fun testCriterionForValuesWithSameSamples() {
        val varianceEqualityFTest = VarianceEqualityFTest(sampleFirst100Elements, sampleFirst100Elements)
        assertEquals(varianceEqualityFTest.isCriterionTrue(mistakeProbability), true)
    }

    private fun testStatistics(sample1: List<Double>, sample2: List<Double>) {
        val varianceEqualityFTest = VarianceEqualityFTest(sample1, sample2)
        val actualValue = BigDecimal(varianceEqualityFTest.statistics)
                .setScale(3, BigDecimal.ROUND_HALF_EVEN)
        val wantedValue = BigDecimal.valueOf(1.614)
        assertEquals(wantedValue, actualValue)
    }

}