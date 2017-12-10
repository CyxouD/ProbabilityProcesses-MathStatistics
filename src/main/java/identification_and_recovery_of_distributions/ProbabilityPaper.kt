package identification_and_recovery_of_distributions

import javafx.geometry.Point2D
import org.jfree.chart.ChartFactory
import org.jfree.chart.ChartPanel
import org.jfree.chart.plot.PlotOrientation
import org.jfree.data.xy.DefaultXYDataset
import org.jfree.data.xy.XYDataset
import primary_statistical_analysis.Chart
import primary_statistical_analysis.VariationalSeries
import java.awt.Point

/**
 * Created by Cyxou on 12/10/17.
 */
class ProbabilityPaper(val variationalSeries: VariationalSeries) {

    fun identifyUnionDistribution() {
        val z = { empiricalDistributionFunction: Double, a: Double, b: Double ->
            empiricalDistributionFunction * (b - a) + a
        }
        val t = { result: Double -> result }

        val variationalSeriesRow = variationalSeries.variationalSeriesRows.toList()
        val a = variationalSeriesRow.first().result
        val b = variationalSeriesRow.last().result
        val newCoordinates = variationalSeriesRow.drop(1).map { row ->
            // i = 2, N
            val zi = z(variationalSeries.getEmpiricalDistributionFunction(row.result), a, b)
            val ti = t(row.result)
            Point2D(ti, zi)
        }
        Chart.probabilityPaper(newCoordinates).createAndShowGUI()
    }
}
