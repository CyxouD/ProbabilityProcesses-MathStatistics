package emperical_methods

import correlation_analysis.CorrelationRatio
import correlation_analysis.KendallRankCorrelationCoefficient
import javafx.geometry.Point2D
import junit.framework.Assert.assertEquals
import org.junit.Test
import java.math.BigDecimal

/**
 * Created by Cyxou on 4/8/18.
 */
class TestCorrelationRatio {

    private val notDistinctElements = arrayOf(Point2D(10.0, 13.0), Point2D(7.0, 5.0), Point2D(11.0, 9.0), Point2D(3.0, 5.0), Point2D(7.0, 8.0), Point2D(12.0, 15.0))
    private val distinctElements = arrayOf(Point2D(1.0, 2.0), Point2D(3.0, 4.0), Point2D(5.0, 6.0), Point2D(7.0, 8.0), Point2D(9.0, 1.0), Point2D(2.0, 3.0), Point2D(4.0, 5.0), Point2D(6.0, 7.0), Point2D(8.0, 9.0))
    private val notDistinctCorrelationCoefficient = CorrelationRatio(notDistinctElements)
    private val distinctCorrelationCoefficient = CorrelationRatio(distinctElements)


    @Test
    fun testCoefficient() {
        val actualValue = BigDecimal(notDistinctCorrelationCoefficient.coefficient)
                .setScale(3, BigDecimal.ROUND_HALF_EVEN)
        val wantedValue = BigDecimal.valueOf(0.853)
        assertEquals(wantedValue, actualValue)
    }

    @Test
    fun testStatistics() {
        val actualValue = BigDecimal(notDistinctCorrelationCoefficient.statistics)
                .setScale(4, BigDecimal.ROUND_HALF_EVEN)
        val wantedValue = BigDecimal.valueOf(1.7746)
        assertEquals(wantedValue, actualValue)
    }

    @Test
    fun testSignificance() {
        val actualValue = notDistinctCorrelationCoefficient.isSignificant(0.05)!!
        assertEquals(false, actualValue)
    }


}
