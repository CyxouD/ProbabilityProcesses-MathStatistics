package emperical_methods;

import correlation_analysis.CorrelationCoefficient;
import correlation_analysis.KendallRankCorrelationCoefficient;
import correlation_analysis.SpearmanRankCorrelationCoefficient;
import javafx.geometry.Point2D;
import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;

/**
 * Created by Cyxou on 4/7/18.
 */
public class TestKendallRankCorrelationCoefficient {

    private Point2D[] notDistinctElements = {
            new Point2D(10, 13),
            new Point2D(7, 5),
            new Point2D(11, 9),
            new Point2D(3, 5),
            new Point2D(7, 8),
            new Point2D(12, 15)
    };
    private Point2D[] distinctElements = {
            new Point2D(1, 2),
            new Point2D(3, 4),
            new Point2D(5, 6),
            new Point2D(7, 8),
            new Point2D(9, 1),
            new Point2D(2, 3),
            new Point2D(4, 5),
            new Point2D(6, 7),
            new Point2D(8, 9)
    };
    private CorrelationCoefficient notDistinctCorrelationCoefficient = new KendallRankCorrelationCoefficient(notDistinctElements);
    private CorrelationCoefficient distinctCorrelationCoefficient = new KendallRankCorrelationCoefficient(distinctElements);


    @Test
    public void testCoefficientForNotDistinctElements() {
        BigDecimal actualValue = new BigDecimal(notDistinctCorrelationCoefficient.getCoefficient())
                .setScale(3, BigDecimal.ROUND_HALF_EVEN);
        BigDecimal wantedValue = BigDecimal.valueOf(0.786);
        assertEquals(wantedValue, actualValue);
    }

    @Test
    public void testCoefficientForDistinctElements() {
        BigDecimal actualValue = new BigDecimal(distinctCorrelationCoefficient.getCoefficient())
                .setScale(3, BigDecimal.ROUND_HALF_EVEN);
        BigDecimal wantedValue = BigDecimal.valueOf(0.556);
        assertEquals(wantedValue, actualValue);
    }

    @Test
    public void testStatistics() {
        BigDecimal actualValue = new BigDecimal(notDistinctCorrelationCoefficient.getStatistics())
                .setScale(3, BigDecimal.ROUND_HALF_EVEN);
        BigDecimal wantedValue = BigDecimal.valueOf(2.214);
        assertEquals(wantedValue, actualValue);

        BigDecimal actualValue2 = new BigDecimal(distinctCorrelationCoefficient.getStatistics())
                .setScale(3, BigDecimal.ROUND_HALF_EVEN);
        BigDecimal wantedValue2 = BigDecimal.valueOf(2.085);
        assertEquals(wantedValue2, actualValue2);
    }

    @Test
    public void testSignificance() {
        boolean actualValue = notDistinctCorrelationCoefficient.isSignificant(0.05);
        assertEquals(true, actualValue);

        boolean actualValue2 = distinctCorrelationCoefficient.isSignificant(0.05);
        assertEquals(true, actualValue2);

    }


}
