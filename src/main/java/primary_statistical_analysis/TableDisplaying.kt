package primary_statistical_analysis

/*
 * TableDisplaying.java requires no other files.
 */

import primary_statistical_analysis.Main.Companion.preciseFloatingPoints
import javax.swing.JFrame
import javax.swing.JPanel
import javax.swing.JScrollPane
import javax.swing.JTable
import java.awt.Dimension

class TableDisplaying : JPanel() {

    companion object {
        private const val VALUE_NOT_EXIST = "-"

        fun variationSeries(variationalSeries: VariationalSeries): TableDisplaying {
            val columnNames = arrayOf("№ варіанти", "Значення варіанти", "Частота", "Відносна частота", "Значення емпіричної функції розподілу")

            val tableRows: Array<Array<Any>> = variationalSeries.variationalSeriesRows.mapIndexed {
                classIndex, variationalClass ->
                arrayOf(classIndex.inc(),
                        variationalClass.result,
                        variationalClass.frequency,
                        variationalClass.relativeFrequency
                                .toPreciseFloatingPoints(preciseFloatingPoints),
                        variationalSeries.getEmpiricalDistributionFunction(variationalClass.result)
                                .toPreciseFloatingPoints(preciseFloatingPoints))
            }.toTypedArray()
            return addTable(tableRows, columnNames)
        }

        fun samplingCharacteristics(variationalSeries: VariationalSeries): TableDisplaying {
            val columnNames = arrayOf("", "Значення", "Середньоквадратичне відхилення", "Довірчий інтервал")

            val tableRows = arrayOf(
                    with(variationalSeries.Average()) {
                        arrayOf("Середнє арифметичне",
                                unBiasedValue()?.toPreciseFloatingPoints(preciseFloatingPoints)?.toDouble() ?: VALUE_NOT_EXIST,
                                standartDeviation()?.toPreciseFloatingPoints(preciseFloatingPoints) ?: VALUE_NOT_EXIST,
                                confidenceInterval() ?: VALUE_NOT_EXIST
                        )
                    },
                    arrayOf("Медіана",
                            variationalSeries.median()?.toPreciseFloatingPoints(preciseFloatingPoints)?.toDouble() ?: VALUE_NOT_EXIST,
                            VALUE_NOT_EXIST,
                            VALUE_NOT_EXIST),
                    with(variationalSeries.StandartDeviation()) {
                        arrayOf("Середньоквадратичне відхилення",
                                unBiasedValue()?.toPreciseFloatingPoints(preciseFloatingPoints)?.toDouble() ?: VALUE_NOT_EXIST,
                                standartDeviation()?.toPreciseFloatingPoints(preciseFloatingPoints) ?: VALUE_NOT_EXIST,
                                confidenceInterval() ?: VALUE_NOT_EXIST

                        )
                    },
                    with(variationalSeries.Skewness()) {
                        arrayOf("Коефіцієнт асиметрії",
                                unBiasedValue()?.toPreciseFloatingPoints(preciseFloatingPoints)?.toDouble() ?: VALUE_NOT_EXIST,
                                standartDeviation()?.toPreciseFloatingPoints(preciseFloatingPoints) ?: VALUE_NOT_EXIST,
                                confidenceInterval() ?: VALUE_NOT_EXIST
                        )
                    },
                    with(variationalSeries.Kurtosis()) {
                        arrayOf("Коефіцієнт ексцесу",
                                unBiasedValue()?.toPreciseFloatingPoints(preciseFloatingPoints)?.toDouble() ?: VALUE_NOT_EXIST,
                                standartDeviation()?.toPreciseFloatingPoints(preciseFloatingPoints) ?: VALUE_NOT_EXIST,
                                confidenceInterval() ?: VALUE_NOT_EXIST
                        )
                    },
                    with(variationalSeries.AntiKurtosis()) {
                        arrayOf("Коефіцієнт контрексцесу",
                                unBiasedValue()?.toPreciseFloatingPoints(preciseFloatingPoints)?.toDouble() ?: VALUE_NOT_EXIST,
                                standartDeviation()?.toPreciseFloatingPoints(preciseFloatingPoints) ?: VALUE_NOT_EXIST,
                                confidenceInterval() ?: VALUE_NOT_EXIST
                        )
                    },
                    with(variationalSeries.CV()) {
                        arrayOf("Коефіцієнт варіації",
                                biasedValue()?.toPreciseFloatingPoints(preciseFloatingPoints)?.toDouble() ?: VALUE_NOT_EXIST,
                                standartDeviation()?.toPreciseFloatingPoints(preciseFloatingPoints) ?: VALUE_NOT_EXIST,
                                confidenceInterval() ?: VALUE_NOT_EXIST

                        )
                    }
            )
            return addTable(tableRows, columnNames)
        }


        fun variationSeriesByClasses(variationalSeriesDividedByClasses: VariationalSeries.VariationalSeriesDividedByClasses): TableDisplaying {
            val columnNames = arrayOf("№ класу", "Межі класу", "Частота", "Відносна частота", "Значення емпіричної функції розподілу")

            val tableRows: Array<Array<Any>> = variationalSeriesDividedByClasses.variationalSeriesDividedByClasses.mapIndexed {
                classIndex, variationalClass ->
                arrayOf(classIndex.inc(),
                        variationalClass.range.toString(),
                        variationalClass.frequency,
                        variationalClass.relativeFrequency
                                .toPreciseFloatingPoints(preciseFloatingPoints),
                        variationalClass.empiricalDistributionFunction
                                .toPreciseFloatingPoints(preciseFloatingPoints))
            }.toTypedArray()
            return addTable(tableRows, columnNames)
        }

        private fun addTable(tableRows: Array<Array<Any>>, columnNames: Array<String>): TableDisplaying {
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
    fun createAndShowGUI() {
        //Create and set up the window.
        val frame = JFrame("TableDisplaying")
        frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE

        isOpaque = true //content panes must be opaque
        frame.contentPane = this

        //Display the window.
        frame.pack()
        frame.isVisible = true
    }


}