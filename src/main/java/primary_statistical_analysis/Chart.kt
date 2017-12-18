package primary_statistical_analysis

import javafx.geometry.Point2D
import java.awt.*

import org.jfree.chart.*
import org.jfree.chart.ui.ApplicationFrame
import org.jfree.chart.ui.UIUtils
import org.jfree.data.statistics.*
import org.jfree.chart.plot.PlotOrientation
import org.jfree.data.statistics.HistogramDataset
import org.jfree.data.category.DefaultCategoryDataset
import org.jfree.chart.ChartPanel
import org.jfree.chart.ChartFactory
import org.jfree.chart.JFreeChart
import org.jfree.chart.axis.*
import org.jfree.chart.plot.CategoryPlot
import org.jfree.chart.renderer.category.GroupedStackedBarRenderer
import org.jfree.data.KeyToGroupMap
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator
import org.jfree.chart.renderer.category.StandardBarPainter
import org.jfree.chart.renderer.category.StackedBarRenderer
import org.jfree.chart.labels.StandardXYToolTipGenerator
import org.jfree.chart.plot.DatasetRenderingOrder
import org.jfree.chart.plot.XYPlot
import org.jfree.chart.renderer.xy.*
import org.jfree.data.xy.DefaultXYDataset
import org.jfree.data.xy.XYSeries
import org.jfree.data.xy.XYSeriesCollection
import java.awt.geom.Ellipse2D
import java.text.DecimalFormat
import java.text.SimpleDateFormat


fun main(args: Array<String>) {
    val demo = Chart("JFreeChart: BarChartDemo1.java")
    demo.pack()
    UIUtils.centerFrameOnScreen(demo)
    demo.isVisible = true

}


class Chart(title: String) : ApplicationFrame(title) {

    companion object {
        fun histogramVariationSeriesByClasses(variationalSeriesDividedByClasses: VariationalSeries.VariationalSeriesDividedByClasses): Chart {
            val dataset = HistogramDataset().apply {
                variationalSeriesDividedByClasses.variationalSeriesDividedByClasses
                        .filter { it.rows.isNotEmpty() }
                        .forEachIndexed { index, variationalClass ->
                            val data = variationalClass.rows.map { it.result }.toDoubleArray()
                            addSeries(index, data, 1, data.min()!!, data.max()!!)
                        }
            }
            dataset.type = HistogramType.FREQUENCY
            val plotTitle = "Гистограмма ряда, разбитого на классы"
            val xaxis = "x"
            val yaxis = "частота"
            val orientation = PlotOrientation.VERTICAL
            val show = false
            val toolTips = true
            val urls = true
            val chart = ChartFactory.createHistogram(plotTitle, xaxis, yaxis,
                    dataset, orientation, show, toolTips, urls)
            val chartPanel = ChartPanel(chart, false)
            chartPanel.fillZoomRectangle = true
            chartPanel.isMouseWheelEnabled = true
            chartPanel.preferredSize = Dimension(500, 270)
            val histogram = Chart(plotTitle)
            histogram.contentPane = chartPanel
            return histogram
        }


        fun histogramVariationSeriesByClassesWithDensityFunction(variationalSeriesDividedByClasses: VariationalSeries.VariationalSeriesDividedByClasses,
                                                                 coordinates: List<Point2D>): Chart {
            val xAxis = NumberAxis("x");
            xAxis.setAutoRangeIncludesZero(true);
            val yAxis = NumberAxis("частота");
            yAxis.setAutoRangeIncludesZero(false);

            val renderer1 = XYBarRenderer(0.0);

            val histoDataSet = HistogramDataset().apply {
                variationalSeriesDividedByClasses.variationalSeriesDividedByClasses
                        .filter { it.rows.isNotEmpty() }
                        .forEachIndexed { index, variationalClass ->
                            val data = variationalClass.rows.map { it.result }.toDoubleArray()
                            addSeries(index, data, 1, data.min()!!, data.max()!!)
                        }
            }

            val plot = XYPlot(histoDataSet, xAxis, yAxis, renderer1);
            val renderer2 = XYSplineRenderer()

            val overlayDataSet = XYSeriesCollection().apply {
                addSeries(XYSeries("Функция плотности").apply {
                    //                    variationalSeriesDividedByClasses.variationalSeriesDividedByClasses
//                            .map {
//                                val rangeCenter = it.range.endInclusive.plus(it.range.start) / 2.0
//                                val heightOfRange = it.frequency.toDouble()
//                                Point2D(rangeCenter, heightOfRange)
//                            }
                    coordinates.forEach { point -> add(point.x, point.y) }
                })
            }
            plot.setDataset(1, overlayDataSet);
            plot.setRenderer(1, renderer2);

            plot.datasetRenderingOrder = DatasetRenderingOrder.FORWARD

            val plotTitle = "Гистограмма ряда, разбитого на классы с функцией плотности"
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


        fun empericalDistributionFunctionSeriesByClasses(variationalSeriesDividedByClasses: VariationalSeries.VariationalSeriesDividedByClasses): Chart {
            val dataset = DefaultCategoryDataset().apply {
                variationalSeriesDividedByClasses.variationalSeriesDividedByClasses
                        .sortedBy { it.empiricalDistributionFunction }
                        .forEachIndexed({ index, `class` ->
                            val roundedRelativeFrequency = `class`.empiricalDistributionFunction.toPreciseFloatingPoints(Main.preciseFloatingPoints)
                            addValue(`class`.empiricalDistributionFunction,
                                    "$index: $roundedRelativeFrequency",
                                    index)
                        })
            }


            val categoryAxis = CategoryAxis("класс")
            categoryAxis.setLowerMargin(0.0)
            categoryAxis.setCategoryMargin(.01)
            categoryAxis.setUpperMargin(.01)
            categoryAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_90)

            val valueAxis = NumberAxis("F(класс)")

            val renderer = StackedBarRenderer()
            renderer.itemMargin = 0.1
            renderer.barPainter = StandardBarPainter()
            renderer.isDrawBarOutline = false
            renderer.setShadowVisible(false)

            val plot = CategoryPlot(dataset,
                    categoryAxis,
                    valueAxis,
                    renderer)

            plot.orientation = PlotOrientation.VERTICAL

            val plotTitle = "График эмпирической функции распределения ряда, разбитого на классы"
            val chart = JFreeChart(plotTitle,
                    JFreeChart.DEFAULT_TITLE_FONT,
                    plot,
                    true)
            val chartPanel = ChartPanel(chart)
            chartPanel.preferredSize = java.awt.Dimension(560, 367)
            val barChart = Chart(plotTitle)
            barChart.contentPane = chartPanel
            return barChart
        }

        fun probabilityPaper(coordinates: List<Point2D>): Chart {

            val dataset = XYSeriesCollection().apply {
                addSeries(XYSeries("New coordinates").apply {
                    coordinates.forEach { point -> add(point.x, point.y) }
                })
            }


            val title = "Імовірнісної сітка"
            val chart = ChartFactory.createScatterPlot(title, "ti(x)", "zi(x)", dataset)
            val chartPanel = ChartPanel(chart)
            // 5x5 red pixel circle
            val shape = Ellipse2D.Double(0.0, 0.0, 2.5, 2.5)
            val xyPlot = chart.plot as XYPlot
            val renderer = xyPlot.renderer
            renderer.setSeriesShape(0, shape)
            renderer.setSeriesPaint(0, Color.BLACK)
            chartPanel.preferredSize = java.awt.Dimension(560, 367)
            val xyChart = Chart(title)
            xyChart.contentPane = chartPanel

            return xyChart
        }


        fun empericalDistributionFunctionVariationSeries(variationalSeries: VariationalSeries): Chart {
            val dataset = DefaultCategoryDataset().apply {
                variationalSeries.variationalSeriesRows.forEach {
                    val empiricalDistributionFunction = variationalSeries.getEmpiricalDistributionFunction(it.result)
                    addValue(empiricalDistributionFunction, it.result, it.result)
                }
            }


            val categoryAxis = CategoryAxis("x")
            categoryAxis.setLowerMargin(0.0)
            categoryAxis.setCategoryMargin(.01)
            categoryAxis.setUpperMargin(.01)
            categoryAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_90)
            categoryAxis.isTickLabelsVisible = variationalSeries.variationalSeriesRows.size < 50

            val valueAxis = NumberAxis("F(x)")

            val renderer = StackedBarRenderer()
            renderer.itemMargin = 0.1
            renderer.barPainter = StandardBarPainter()
            renderer.isDrawBarOutline = false
            renderer.setShadowVisible(false)

            val plot = CategoryPlot(dataset,
                    categoryAxis,
                    valueAxis,
                    renderer)

            plot.orientation = PlotOrientation.VERTICAL

            val plotTitle = "График эмпирической функции распределения ряда, разбитого на классы"
            val chart = JFreeChart(plotTitle,
                    JFreeChart.DEFAULT_TITLE_FONT,
                    plot,
                    false)
            val chartPanel = ChartPanel(chart)
            chartPanel.preferredSize = java.awt.Dimension(560, 367)
            val barChart = Chart(plotTitle)
            barChart.contentPane = chartPanel
            return barChart

        }

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