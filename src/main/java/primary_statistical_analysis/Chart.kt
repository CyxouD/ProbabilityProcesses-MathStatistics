package primary_statistical_analysis

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
import org.jfree.chart.axis.CategoryAxis
import org.jfree.chart.plot.CategoryPlot
import org.jfree.chart.renderer.category.GroupedStackedBarRenderer
import org.jfree.data.KeyToGroupMap
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator
import org.jfree.chart.renderer.category.StandardBarPainter
import org.jfree.chart.renderer.category.StackedBarRenderer
import org.jfree.chart.axis.CategoryLabelPositions
import org.jfree.chart.axis.NumberAxis
import org.jfree.chart.renderer.xy.StackedXYBarRenderer


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
            val xaxis = "частота"
            val yaxis = "x"
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

        fun empericalDistributionFunctionSeriesByClasses(variationalSeriesDividedByClasses: VariationalSeries.VariationalSeriesDividedByClasses): Chart {
            val dataset = DefaultCategoryDataset().apply {
                variationalSeriesDividedByClasses.variationalSeriesDividedByClasses
                        .sortedBy { it.empiricalDistributionFunction }
                        .forEach { `class` ->
                            addValue(`class`.relativeFrequency, `class`.range, `class`.range)
                        }
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
                    true)
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