import junit.framework.Assert.assertEquals
import org.junit.Test
import sample_uniformity.MeansEqualityPairedTTest
import java.math.BigDecimal

/**
 * Created by Cyxou on 4/15/18.
 */
class TestMeansEqualityParedTTest {

    private val firstSample = (1..3).map { it.toDouble() }
    private val secondSample = (3..5).map { it.toDouble() }

    private val sampleFirst100Elements = (1..200).map { (it * 2).toDouble() }
    private val sampleSecond100Elements = (1..200).map { (it * 2 - 1).toDouble() }

    @Test
    fun testStatistics() {
        val meansEqualityPairedTTest = MeansEqualityPairedTTest(firstSample, secondSample)
        val actualValue = try {
            BigDecimal(meansEqualityPairedTTest.statistics)
                    .setScale(4, BigDecimal.ROUND_HALF_EVEN)
        } catch (e: NumberFormatException) {
            meansEqualityPairedTTest.statistics
        }
        assertEquals(Double.NEGATIVE_INFINITY, actualValue)
    }

    @Test
    fun testSignificance() {
        val meansEqualityPairedTTest = MeansEqualityPairedTTest(firstSample, secondSample)
        val actualValue = meansEqualityPairedTTest.isCriterionTrue(0.05)
        assertEquals(false, actualValue)
    }

    @Test
    fun testSignificanceWithSameSamples() {
        val meansEqualityPairedTTest = MeansEqualityPairedTTest(sampleFirst100Elements, sampleFirst100Elements)
        val actualValue = meansEqualityPairedTTest.isCriterionTrue(0.05)
        assertEquals(true, actualValue)
    }

    @Test
    fun testSignificanceWithAlmostSameMean() {
        val meansEqualityPairedTTest = MeansEqualityPairedTTest(sampleFirst100Elements, sampleSecond100Elements)
        val actualValue = meansEqualityPairedTTest.isCriterionTrue(0.05)
        assertEquals(true, actualValue)
    }

}