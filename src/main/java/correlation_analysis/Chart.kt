package correlation_analysis

import javafx.geometry.Point2D
import org.jfree.chart.ChartFactory
import org.jfree.data.xy.XYSeries
import org.jfree.data.xy.XYSeriesCollection
import org.jfree.chart.ChartPanel
import org.jfree.chart.ui.ApplicationFrame
import org.jfree.chart.ui.UIUtils


/**
 * Created by Cyxou on 4/1/18.
 */
class Chart(title: String) : ApplicationFrame(title) {
    fun correlationField(points2d: Array<List<Double>>): Chart {
        assert(points2d.map { it.size }.all { it == 2 })
        val points = points2d.map { Point2D(it[0], it[1]) }.toTypedArray()
        return correlationField(points)
    }

    fun correlationField(points: Array<Point2D>): Chart {
        val dataset = XYSeriesCollection().apply {
            addSeries(XYSeries("Coordinates").apply {
                points.forEach { add(it.x, it.y) }
            })
        }
        val scatterPlotTitle = "Кореляционное поле"
        val chart = ChartFactory.createScatterPlot(scatterPlotTitle, "x", "y", dataset)
        // Create Panel
        val panel = ChartPanel(chart)
        val xyChart = Chart(scatterPlotTitle)
        xyChart.contentPane = panel
        return xyChart
    }

    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event-dispatching thread.
     */
    fun createAndShowGUI() {
        pack()
        UIUtils.centerFrameOnScreen(this)
        isVisible = true
    }

}