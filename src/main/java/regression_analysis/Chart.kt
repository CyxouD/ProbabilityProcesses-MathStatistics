package regression_analysis

import javafx.geometry.Point2D
import org.jfree.chart.ChartFactory
import org.jfree.chart.ChartPanel
import org.jfree.chart.JFreeChart
import org.jfree.chart.axis.NumberAxis
import org.jfree.chart.plot.DatasetRenderingOrder
import org.jfree.chart.plot.XYPlot
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer
import org.jfree.chart.ui.ApplicationFrame
import org.jfree.chart.ui.UIUtils
import org.jfree.data.xy.XYSeries
import org.jfree.data.xy.XYSeriesCollection
import java.awt.Dimension

/**
 * Created by Cyxou on 4/18/18.
 */
class Chart(title: String) : ApplicationFrame(title) {
    fun correlationFieldAndRegression(originalPoints: Array<List<Double>>, regressionPoints: Array<List<Double>>): Chart {
        assert(originalPoints.map { it.size }.all { it == 2 })
        assert(regressionPoints.map { it.size }.all { it == 2 })
        val origPoints = originalPoints.map { Point2D(it[0], it[1]) }.toTypedArray()
        val regPoints = regressionPoints.map { Point2D(it[0], it[1]) }.toTypedArray()
        return correlationFieldAndRegression(origPoints, regPoints)
    }

    fun correlationFieldAndRegression(originalPoints: Array<Point2D>, regressionPoints: Array<Point2D>): Chart {

        val originalPointsDataSet = XYSeriesCollection().apply {
            addSeries(XYSeries("y").apply {
                originalPoints.forEach { add(it.x, it.y) }
            })
        }
        val scatterPlotTitle = "Кореляционное поле"
        val categoryAxis = NumberAxis("x")
        val valueAxis = NumberAxis("y")
        val renderer1 = XYLineAndShapeRenderer();
        val renderer2 = XYLineAndShapeRenderer()
        val plot = XYPlot(originalPointsDataSet, categoryAxis, valueAxis, renderer1)

        val regressionPointsDataSet = XYSeriesCollection().apply {
            addSeries(XYSeries("f(x)").apply {
                regressionPoints.forEach { add(it.x, it.y) }
            })
        }

        plot.setDataset(1, regressionPointsDataSet)
        plot.setRenderer(1, renderer2)

        plot.datasetRenderingOrder = DatasetRenderingOrder.FORWARD

        val plotTitle = "Кореляционное поле с функцией регрессии"
        val chart = JFreeChart(plotTitle,
                JFreeChart.DEFAULT_TITLE_FONT, plot, true);
        val chartPanel = ChartPanel(chart, false)
        chartPanel.fillZoomRectangle = true
        chartPanel.isMouseWheelEnabled = true
        chartPanel.preferredSize = Dimension(500, 270)
        val histogram = Chart(plotTitle)
        histogram.contentPane = chartPanel
        return histogram
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