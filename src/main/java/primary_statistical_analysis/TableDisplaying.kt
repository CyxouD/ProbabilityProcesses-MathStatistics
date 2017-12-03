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
                    arrayOf("Середнє арифметичне",
                            variationalSeries.average().toPreciseFloatingPoints(preciseFloatingPoints).toDouble(),
                            variationalSeries.averageStandartDeviation().toPreciseFloatingPoints(preciseFloatingPoints),
                            variationalSeries.sampleCharacteristicsConfidenceInterval(variationalSeries.average(),
                                    variationalSeries.averageStandartDeviation())
                    ),
                    arrayOf("Медіана",
                            variationalSeries.average().toPreciseFloatingPoints(preciseFloatingPoints).toDouble(), "-", "-"),
                    arrayOf("Середньоквадратичне відхилення",
                            variationalSeries.standartDeviation().toPreciseFloatingPoints(preciseFloatingPoints).toDouble(),
                            variationalSeries.standartDeviationStandartDeviation().toPreciseFloatingPoints(preciseFloatingPoints),
                            variationalSeries.sampleCharacteristicsConfidenceInterval(variationalSeries.standartDeviation(),
                                    variationalSeries.standartDeviationStandartDeviation())

                    ),
                    arrayOf("Коефіцієнт асиметрії",
                            variationalSeries.unbiasedSkewness().toPreciseFloatingPoints(preciseFloatingPoints).toDouble(),
                            variationalSeries.skewnessStandartDeviation().toPreciseFloatingPoints(preciseFloatingPoints),
                            variationalSeries.sampleCharacteristicsConfidenceInterval(variationalSeries.unbiasedSkewness(),
                                    variationalSeries.skewnessStandartDeviation())
                    ),
                    arrayOf("Коефіцієнт ексцесу",
                            variationalSeries.unbiasedKurtosis().toPreciseFloatingPoints(preciseFloatingPoints).toDouble(),
                            variationalSeries.kurtosisStandartDeviation().toPreciseFloatingPoints(preciseFloatingPoints),
                            variationalSeries.sampleCharacteristicsConfidenceInterval(variationalSeries.unbiasedKurtosis(),
                                    variationalSeries.kurtosisStandartDeviation())
                    ),
                    arrayOf("Коефіцієнт контрексцесу",
                            variationalSeries.antiKurtosis().toPreciseFloatingPoints(preciseFloatingPoints).toDouble(),
                            variationalSeries.antiKurtosisStandartDeviation().toPreciseFloatingPoints(preciseFloatingPoints),
                            variationalSeries.sampleCharacteristicsConfidenceInterval(variationalSeries.antiKurtosis(),
                                    variationalSeries.antiKurtosisStandartDeviation())
                    ),
                    arrayOf("Коефіцієнт варіації",
                            variationalSeries.cv().toPreciseFloatingPoints(preciseFloatingPoints).toDouble(),
                            variationalSeries.cvStandartDeviation().toPreciseFloatingPoints(preciseFloatingPoints),
                            variationalSeries.sampleCharacteristicsConfidenceInterval(variationalSeries.cv(),
                                    variationalSeries.cvStandartDeviation())

                    )
            )
            return addTable(tableRows, columnNames)
        }


        fun variationSeriesByClasses(variationalSeriesDividedByClasses: VariationalSeries.VariationalSeriesDividedByClasses): TableDisplaying {
            val columnNames = arrayOf("№ класу", "Межі класу", "Частота", "Відносна частота", "Значення емпіричної функції розподілу")

            val tableRows: Array<Array<Any>> = variationalSeriesDividedByClasses.variationalSeriesDividedByClasses.mapIndexed {
                classIndex, variationalClass ->
                arrayOf(classIndex.inc(),
                        variationalClass.range,
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