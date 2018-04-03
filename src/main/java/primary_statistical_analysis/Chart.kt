package primary_statistical_analysis

import javafx.geometry.Point2D
import java.awt.*

import org.jfree.chart.ui.ApplicationFrame
import org.jfree.chart.ui.UIUtils
import org.jfree.data.statistics.*
import org.jfree.chart.plot.PlotOrientation
import org.jfree.data.statistics.HistogramDataset
import org.jfree.chart.ChartPanel
import org.jfree.chart.ChartFactory
import org.jfree.chart.JFreeChart
import org.jfree.chart.axis.*
import org.jfree.chart.plot.DatasetRenderingOrder
import org.jfree.chart.plot.XYPlot
import org.jfree.chart.renderer.xy.*
import org.jfree.data.xy.XYSeries
import org.jfree.data.xy.XYSeriesCollection
import java.awt.geom.Ellipse2D


fun main(args: Array<String>) {
    val demo = Chart("JFreeChart: BarChartDemo1.java")
    demo.pack()
    UIUtils.centerFrameOnScreen(demo)
    demo.isVisible = true

}


class Chart(title: String) : ApplicationFrame(title) {

    companion object {
        fun histogramVariationSeriesByClasses(variationalSeriesDividedByClasses: VariationalSeries.VariationalSeriesDividedByClasses): Chart {
            val dataset = histogramDataset(variationalSeriesDividedByClasses)
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
            renderer1.setShadowVisible(false)
            renderer1.barPainter = StandardXYBarPainter();

            val histoDataSet = histogramDataset(variationalSeriesDividedByClasses)

            val plot = XYPlot(histoDataSet, xAxis, yAxis, renderer1);
            val renderer2 = XYSplineRenderer()

            val overlayDataSet = XYSeriesCollection().apply {
                addSeries(XYSeries("Функция плотности").apply {
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

        fun empiricalDistributionFunctionSeriesByClasses(variationalSeriesDividedByClasses: VariationalSeries.VariationalSeriesDividedByClasses): Chart {
            val dataset = empiricalDistributionDataset(variationalSeriesDividedByClasses)


            val categoryAxis = NumberAxis("x")
            categoryAxis.setLowerMargin(0.0)
            categoryAxis.setUpperMargin(.01)

            val valueAxis = NumberAxis("F(класс)")

            val renderer = XYLineAndShapeRenderer()
            val plot = XYPlot(dataset, categoryAxis, valueAxis, renderer)
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

        fun empiricalDistributionFunctionVariationalSeriesWithDistributionFunction(variationalSeries: VariationalSeries,
                                                                                   coordinates: List<Point2D>,
                                                                                   confidenceIntervalOcenkaA: DoubleRange,
                                                                                   confidenceIntervalOcenkaB: DoubleRange): Chart {
            val categoryAxis = NumberAxis("класс")
            categoryAxis.setLowerMargin(0.0)
            categoryAxis.setUpperMargin(.01)

            val valueAxis = NumberAxis("F(класс)")

            val renderer1 = XYLineAndShapeRenderer();

            val dataset = empiricalDistributionDataset(variationalSeries)

            val plot = XYPlot(dataset, categoryAxis, valueAxis, renderer1);
            val renderer2 = XYLineAndShapeRenderer()

            val overlayDataSet = XYSeriesCollection().apply {
                addSeries(XYSeries("Функция распределения").apply {
                    coordinates.forEach { point -> add(point.x, point.y) }
                })
            }
            plot.setDataset(1, overlayDataSet);
            plot.setRenderer(1, renderer2);

            val renderer3 = XYLineAndShapeRenderer()
            val dataset3 = XYSeriesCollection().apply {
                addSeries(XYSeries("доверительный интервал для А = $confidenceIntervalOcenkaA").apply {
                    add(confidenceIntervalOcenkaA.start, 0.0)
                    add(confidenceIntervalOcenkaA.endInclusive, 0.0)
                })

            }
            plot.setDataset(2, dataset3);
            plot.setRenderer(2, renderer3);

            val renderer4 = XYLineAndShapeRenderer()
            val dataset4 = XYSeriesCollection().apply {
                addSeries(XYSeries("доверительный интервал для B = $confidenceIntervalOcenkaB").apply {
                    add(confidenceIntervalOcenkaB.start, 0.0)
                    add(confidenceIntervalOcenkaB.endInclusive, 0.0)
                })

            }
            plot.setDataset(3, dataset4);
            plot.setRenderer(3, renderer4);


            plot.datasetRenderingOrder = DatasetRenderingOrder.FORWARD

            val plotTitle = "Эмперическая функция ряда, разбитого на классы с функцией распределения"
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

        fun empiricalDistributionFunctionVariationSeries(variationalSeries: VariationalSeries): Chart {
            val dataset = empiricalDistributionDataset(variationalSeries)


            val categoryAxis = NumberAxis("x")
            categoryAxis.setLowerMargin(0.0)
            categoryAxis.setUpperMargin(.01)
            categoryAxis.isTickLabelsVisible = variationalSeries.variationalSeriesRows.size < 50

            val valueAxis = NumberAxis("F(x)")

            val renderer = XYLineAndShapeRenderer()

            val plot = XYPlot(dataset,
                    categoryAxis,
                    valueAxis,
                    renderer)

            plot.orientation = PlotOrientation.VERTICAL

            val plotTitle = "График эмпирической функции вариационного ряда"
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

        private fun empiricalDistributionDataset(variationalSeries: VariationalSeries): XYSeriesCollection {
            return XYSeriesCollection().apply {
                addSeries(XYSeries("Функция распределения").apply {
                    val points = mutableListOf<Pair<Point2D, Point2D>>()
                    variationalSeries.variationalSeriesRows.fold(mutableListOf<Point2D>(), { acc, (result) ->
                        acc.lastOrNull()?.let { lastPoint ->
                            points.add(
                                    Pair(lastPoint,
                                            Point2D(result, variationalSeries.getEmpiricalDistributionFunction(result))
                                    )
                            )
                        }
                        acc.add(Point2D(result, variationalSeries.getEmpiricalDistributionFunction(result)))
                        acc
                    })
                    points.map { (firstP, secondP) -> listOf(firstP, Point2D(secondP.x, firstP.y), secondP) }
                            .flatten()
                            .forEach { point ->
                                add(point.x, point.y)
                            }
                    println(points.map { (firstP, secondP) -> listOf(firstP, Point2D(secondP.x, firstP.y), secondP) }
                            .flatten()
                            .size)
                })
            }
        }

        private fun histogramDataset(variationalSeriesDividedByClasses: VariationalSeries.VariationalSeriesDividedByClasses): HistogramDataset {
            return HistogramDataset().apply {
                variationalSeriesDividedByClasses.variationalSeriesDividedByClasses
                        .filter { it.rows.isNotEmpty() }
                        .forEachIndexed { index, variationalClass ->
                            val data = variationalClass.rows.map { row -> List(row.frequency) { row.result } }.flatten().toDoubleArray()
                            addSeries(index + 1, data, 1, variationalClass.range.start, variationalClass.range.endInclusive)
                        }
            }
        }


        private fun empiricalDistributionDataset(variationalSeriesDividedByClasses: VariationalSeries.VariationalSeriesDividedByClasses): XYSeriesCollection {
            return XYSeriesCollection().apply {
                addSeries(XYSeries("Функция распределения").apply {
                    variationalSeriesDividedByClasses.variationalSeriesDividedByClasses
                            .sortedBy { it.empiricalDistributionFunction }
                            .forEachIndexed({ index, `class` ->
                                add(`class`.range.start,
                                        `class`.empiricalDistributionFunction
                                )
                                add(`class`.range.endInclusive,
                                        `class`.empiricalDistributionFunction
                                )
                            })
                })
            }
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
