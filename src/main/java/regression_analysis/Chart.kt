package regression_analysis

import javafx.geometry.Point2D
import org.jfree.chart.ChartFactory
import org.jfree.chart.ChartPanel
import org.jfree.chart.JFreeChart
import org.jfree.chart.axis.NumberAxis
import org.jfree.chart.plot.DatasetRenderingOrder
import org.jfree.chart.plot.XYPlot
import org.jfree.chart.renderer.category.ScatterRenderer
import org.jfree.chart.renderer.xy.*
import org.jfree.chart.ui.ApplicationFrame
import org.jfree.chart.ui.UIUtils
import org.jfree.data.xy.XYSeries
import org.jfree.data.xy.XYSeriesCollection
import java.awt.Color
import java.awt.Dimension
import java.awt.geom.Ellipse2D
import java.awt.image.ColorModel

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
        val categoryAxis = NumberAxis("x")
        val valueAxis = NumberAxis("y")
        val renderer1 = XYShapeRenderer().apply {
            setSeriesPaint(0, Color.BLACK)
        }
        val renderer2 = XYLineAndShapeRenderer().apply {
            setSeriesShape(0, Ellipse2D.Double(0.0, 0.0, 0.0, 0.0))
            setSeriesPaint(0, Color.GREEN)
        }
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

    fun regressionWithPredictingValueConfidenceIntervalAndRegressionConfidenceInterval(regressionPoints: Array<Point2D>,
                                                                                       regressionConfidenceIntervalPoints: ConfidenceIntervalPoints,
                                                                                       predictingValueConfidenceIntervalPoints: ConfidenceIntervalPoints)
            : Chart {

        val regressionConfidenceIntervalPointsDataSet = XYSeriesCollection().apply {
            addSeries(XYSeries("f0(x)").apply {
                regressionConfidenceIntervalPoints.firstPart.forEach { add(it.x, it.y) }
            })
            addSeries(XYSeries("f1(x)").apply {
                regressionConfidenceIntervalPoints.secondPart.forEach { add(it.x, it.y) }
            })
        }
        val categoryAxis = NumberAxis("x")
        val valueAxis = NumberAxis("y")
        val renderer1 = XYLineAndShapeRenderer().apply {
            setSeriesShape(0, Ellipse2D.Double(0.0, 0.0, 0.0, 0.0))
            setSeriesShape(1, Ellipse2D.Double(0.0, 0.0, 0.0, 0.0))
            setSeriesPaint(0, Color.RED)
        }
        val renderer2 = XYLineAndShapeRenderer().apply {
            setSeriesShape(0, Ellipse2D.Double(0.0, 0.0, 0.0, 0.0))
            setSeriesPaint(0, Color.GREEN)
        }
        val renderer3 = XYLineAndShapeRenderer().apply {
            setSeriesShape(0, Ellipse2D.Double(0.0, 0.0, 0.0, 0.0))
            setSeriesShape(1, Ellipse2D.Double(0.0, 0.0, 0.0, 0.0))
            setSeriesPaint(0, Color.BLUE)
        }
        val plot = XYPlot(regressionConfidenceIntervalPointsDataSet, categoryAxis, valueAxis, renderer1)

        val regressionPointsDataSet = XYSeriesCollection().apply {
            addSeries(XYSeries("f(x)").apply {
                regressionPoints.forEach { add(it.x, it.y) }
            })
        }

        plot.setDataset(1, regressionPointsDataSet)
        plot.setRenderer(1, renderer2)

        val predictingValueConfidenceIntervalPointsDataSet = XYSeriesCollection().apply {
            addSeries(XYSeries("y0(x)").apply {
                predictingValueConfidenceIntervalPoints.firstPart.forEach { add(it.x, it.y) }
            })
            addSeries(XYSeries("y1(x)").apply {
                predictingValueConfidenceIntervalPoints.secondPart.forEach { add(it.x, it.y) }
            })
        }

        plot.setDataset(2, predictingValueConfidenceIntervalPointsDataSet)
        plot.setRenderer(2, renderer3)

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

    fun residualPlot(points2d: Array<List<Double>>): Chart {
        assert(points2d.map { it.size }.all { it == 2 })
        val points = points2d.map { Point2D(it[0], it[1]) }.toTypedArray()
        return residualPlot(points)
    }

    fun residualPlot(points: Array<Point2D>): Chart {
        val dataset = XYSeriesCollection().apply {
            addSeries(XYSeries("Coordinates").apply {
                points.forEach { add(it.x, it.y) }
            })
        }
        val scatterPlotTitle = "діагностична діаграма, residual plot"
        val chart = ChartFactory.createScatterPlot(scatterPlotTitle, "Прогнозоване значення", "Залишки E", dataset)
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

    data class ConfidenceIntervalPoints(val firstPart: List<Point2D>, val secondPart: List<Point2D>)

}