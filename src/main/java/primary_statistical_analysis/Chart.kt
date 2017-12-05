package primary_statistical_analysis

import java.awt.*

import org.jfree.chart.*
import org.jfree.chart.ui.ApplicationFrame
import org.jfree.chart.ui.UIUtils
import org.jfree.data.statistics.*
import org.jfree.chart.plot.PlotOrientation
import org.jfree.data.statistics.HistogramDataset


fun main(args: Array<String>) {
    val demo = Chart("JFreeChart: BarChartDemo1.java")
    demo.pack()
    UIUtils.centerFrameOnScreen(demo)
    demo.isVisible = true

}


class Chart(title: String) : ApplicationFrame(title) {

    companion object {
        fun histogramVariationSeriesByClasses(variationalSeriesDividedByClasses: VariationalSeries.VariationalSeriesDividedByClasses): Chart {
            val suggestedWidth = 0.1
            val dataset = HistogramDataset().apply {
                variationalSeriesDividedByClasses.variationalSeriesDividedByClasses
                        .filter { it.rows.isNotEmpty() }
                        .forEachIndexed { index, variationalClass ->
                            val data = variationalClass.rows.map { it.result }.toDoubleArray()
                            val numberOfBins = data.max()!!.minus(data.min()!!).div(suggestedWidth)
                            val bins = Math.max(numberOfBins.toInt(), 1)
                            addSeries(index, data, bins)
                        }
            }
            dataset.type = HistogramType.FREQUENCY
            val plotTitle = "Гистограмма ряда, разбитого на классы"
            val xaxis = "значение"
            val yaxis = "количество"
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