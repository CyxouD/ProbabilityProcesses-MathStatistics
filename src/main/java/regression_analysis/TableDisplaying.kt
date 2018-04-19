package regression_analysis

import javafx.geometry.Point2D
import primary_statistical_analysis.toPreciseFloatingPoints
import java.awt.Dimension
import javax.swing.JFrame
import javax.swing.JPanel
import javax.swing.JScrollPane
import javax.swing.JTable

/**
 * Created by Cyxou on 4/19/18.
 */
class TableDisplaying : JPanel() {
    companion object {
        fun ocenkiParametrovLinearOneDimensionalRegression(randomPoints: Array<Point2D>, mistakeProbability: Double)
                : TableDisplaying {
            val columnNames = arrayOf("Параметр",
                    "Значення оцінки",
                    "Дисперсія",
                    "Статистика",
                    "Квантиль",
                    "Значущість",
                    "${1 - mistakeProbability}% довірчий інтервал",
                    "Висновок")

            val linearOneDimensionalRegression = LinearOneDimensionalRegression(randomPoints)

            val tableRows = with(linearOneDimensionalRegression) {
                val parameterSignificant = "=0"
                val parameterNotSignificant = "!=0"
                arrayOf(
                        arrayOf("a1",
                                a1.toPreciseFloatingPoints(3),
                                a1Variance.toPreciseFloatingPoints(3),
                                a1Statistics.toPreciseFloatingPoints(3),
                                tDistributionInverseCumulativeProbability(mistakeProbability).toPreciseFloatingPoints(3),
                                isA1Significant(mistakeProbability), a1ConfidenceInterval(mistakeProbability),
                                if (isA1Significant(mistakeProbability)) parameterSignificant else parameterNotSignificant),
                        arrayOf("a2",
                                a2.toPreciseFloatingPoints(3),
                                a2Variance.toPreciseFloatingPoints(3),
                                a2Statistics.toPreciseFloatingPoints(3),
                                tDistributionInverseCumulativeProbability(mistakeProbability).toPreciseFloatingPoints(3),
                                isA2Significant(mistakeProbability), a2ConfidenceInterval(mistakeProbability),
                                if (isA2Significant(mistakeProbability)) parameterSignificant else parameterNotSignificant)
                ).map { it.map { it.toString() }.toTypedArray() }.toTypedArray()
            }

            return addTable(tableRows, columnNames)
        }

        fun adequacyOfRegressionCriterias(randomPoints: Array<Point2D>, mistakeProbability: Double):
                TableDisplaying {
            val columnNames = arrayOf("Критерій",
                    "Статистика",
                    "Квантиль (${1 - mistakeProbability}% довірчий інтервал)",
                    "Висновок")
            val linearOneDimensionalRegression = LinearOneDimensionalRegression(randomPoints)

            val tableRows = with(linearOneDimensionalRegression) {
                arrayOf(
                        arrayOf("Коефіцієнт детермінації",
                                linearOneDimensionalRegression.coefficientOfDetermination.toPreciseFloatingPoints(3),
                                linearOneDimensionalRegression.fTestDistributionInverseCumulativeProbability(mistakeProbability = mistakeProbability)
                                        .toPreciseFloatingPoints(3),
                                if (linearOneDimensionalRegression.isRegressionSignificantBasedOnCoffOfDetermination(mistakeProbability)) {
                                    "Значущій"
                                } else {
                                    "Не значущій"
                                }

                        )
                ).map { it.map { it }.toTypedArray() }.toTypedArray()
            }

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