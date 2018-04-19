package correlation_analysis

import primary_statistical_analysis.toPreciseFloatingPoints
import java.awt.Dimension
import javax.swing.JFrame
import javax.swing.JPanel
import javax.swing.JScrollPane
import javax.swing.JTable

/**
 * Created by Cyxou on 4/8/18.
 */
class TableDisplaying : JPanel() {
    companion object {
        fun correlationCoefficients(points2d: Array<List<Double>>, mistakeProbability: Double)
                : TableDisplaying {
            val columnNames = arrayOf("Коефіцієнт", "Значення коєфіцієнту", "Статистика", "Значущість", "Зв'язок", "${1 - mistakeProbability}% довірчий інтервал")

            val pirsonRCorrelationCoefficient = PirsonRCorrelationCoefficient(points2d)
            val spearmanRankCorrelationCoefficient = SpearmanRankCorrelationCoefficient(points2d)
            val kendallRankCorrelationCoefficient = KendallRankCorrelationCoefficient(points2d)
            val correlationRatio = CorrelationRatio(points2d)
            val notExististingConfidenceInterval = "Не існує"
            val tableRows = arrayOf(
                    arrayOf(pirsonRCorrelationCoefficient::class.java.simpleName,
                            pirsonRCorrelationCoefficient.coefficient?.toPreciseFloatingPoints(3),
                            pirsonRCorrelationCoefficient.statistics?.toPreciseFloatingPoints(3),
                            if (pirsonRCorrelationCoefficient.isSignificant(mistakeProbability)!!) "Значущій" else "Незначущій",
                            if (pirsonRCorrelationCoefficient.isSignificant(mistakeProbability)!!) "Лінійний" else "Нелінійний",
                            if (pirsonRCorrelationCoefficient.isSignificant(mistakeProbability)!!)
                                pirsonRCorrelationCoefficient.coefficientConfidenceInterval(mistakeProbability)
                            else
                                "-"
                    ),
                    arrayOf(spearmanRankCorrelationCoefficient::class.java.simpleName,
                            spearmanRankCorrelationCoefficient.coefficient?.toPreciseFloatingPoints(3),
                            spearmanRankCorrelationCoefficient.statistics?.toPreciseFloatingPoints(3),
                            if (spearmanRankCorrelationCoefficient.isSignificant(mistakeProbability)!!) "Значущій" else "Незначущій",
                            if (spearmanRankCorrelationCoefficient.isSignificant(mistakeProbability)!!) "Монотонний" else "Не монотонний",
                            notExististingConfidenceInterval
                    ),
                    arrayOf(kendallRankCorrelationCoefficient::class.java.simpleName,
                            kendallRankCorrelationCoefficient.coefficient?.toPreciseFloatingPoints(3),
                            kendallRankCorrelationCoefficient.statistics?.toPreciseFloatingPoints(3),
                            if (kendallRankCorrelationCoefficient.isSignificant(mistakeProbability)!!) "Значущій" else "Незначущій",
                            if (kendallRankCorrelationCoefficient.isSignificant(mistakeProbability)!!) "Монотонний" else "Не монотонний",
                            notExististingConfidenceInterval)
                    ,
                    arrayOf(correlationRatio::class.java.simpleName,
                            correlationRatio.coefficient.toPreciseFloatingPoints(3),
                            correlationRatio.statistics.toPreciseFloatingPoints(3),
                            if (correlationRatio.isSignificant(mistakeProbability)!!) "Значущій" else "Незначущій",
                            if (correlationRatio.isSignificant(mistakeProbability)!!) "Нелінійний" else "Лінійний",
                            notExististingConfidenceInterval
                    )).map { it.map { it.toString() }.toTypedArray() }.toTypedArray()
            return addTable(tableRows, columnNames)
        }

        private fun addTable(tableRows: Array<Array<String>>, columnNames: Array<String>): TableDisplaying {
            val table = JTable(tableRows, columnNames)
            table.preferredScrollableViewportSize = Dimension(1000, 400)
            table.fillsViewportHeight = true
            //Create the scroll pane and add the table to it.
            val scrollPane = JScrollPane(table)

            val tableContainer = TableDisplaying()
            //Add the scroll pane to this panel.
            tableContainer.add(scrollPane)
            return tableContainer
        }
    }


    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event-dispatching thread.
     */
    fun createAndShowGUI(jframeTitle: String = "TableDisplaying") {
        //Create and set up the window.
        val frame = JFrame(jframeTitle)
        frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE

        isOpaque = true //content panes must be opaque
        frame.contentPane = this

        //Display the window.
        frame.pack()
        frame.isVisible = true
    }

}