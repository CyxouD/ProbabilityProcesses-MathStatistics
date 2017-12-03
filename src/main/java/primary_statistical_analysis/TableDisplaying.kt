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

            val average = variationalSeries.average()
            val standartDeviation = variationalSeries.standartDeviation()
            val unbiasedSkewness = variationalSeries.unbiasedSkewness()
            val unbiasedKurtosis = variationalSeries.unbiasedKurtosis()
            val antiKurtosis = variationalSeries.antiKurtosis()
            val cv = variationalSeries.cv()
            val tableRows = arrayOf(
                    arrayOf("Середнє арифметичне",
                            average?.toPreciseFloatingPoints(preciseFloatingPoints)?.toDouble() ?: VALUE_NOT_EXIST,
                            variationalSeries.averageStandartDeviation()?.toPreciseFloatingPoints(preciseFloatingPoints) ?: VALUE_NOT_EXIST,
                            average?.let {
                                variationalSeries.sampleCharacteristicsConfidenceInterval(average,
                                        variationalSeries.averageStandartDeviation()!!)
                            } ?: VALUE_NOT_EXIST
                    ),
                    arrayOf("Медіана",
                            variationalSeries.median()?.toPreciseFloatingPoints(preciseFloatingPoints)?.toDouble() ?: VALUE_NOT_EXIST, VALUE_NOT_EXIST, VALUE_NOT_EXIST),
                    arrayOf("Середньоквадратичне відхилення",
                            standartDeviation?.toPreciseFloatingPoints(preciseFloatingPoints)?.toDouble() ?: VALUE_NOT_EXIST,
                            variationalSeries.standartDeviationStandartDeviation()?.toPreciseFloatingPoints(preciseFloatingPoints) ?: VALUE_NOT_EXIST,
                            standartDeviation?.let {
                                variationalSeries.sampleCharacteristicsConfidenceInterval(standartDeviation,
                                        variationalSeries.standartDeviationStandartDeviation()!!)
                            } ?: VALUE_NOT_EXIST

                    ),
                    arrayOf("Коефіцієнт асиметрії",
                            unbiasedSkewness?.toPreciseFloatingPoints(preciseFloatingPoints)?.toDouble() ?: VALUE_NOT_EXIST,
                            variationalSeries.skewnessStandartDeviation()?.toPreciseFloatingPoints(preciseFloatingPoints) ?: VALUE_NOT_EXIST,
                            unbiasedSkewness?.let {
                                variationalSeries.sampleCharacteristicsConfidenceInterval(unbiasedSkewness,
                                        variationalSeries.skewnessStandartDeviation()!!)
                            } ?: VALUE_NOT_EXIST
                    ),
                    arrayOf("Коефіцієнт ексцесу",
                            unbiasedKurtosis?.toPreciseFloatingPoints(preciseFloatingPoints)?.toDouble() ?: VALUE_NOT_EXIST,
                            variationalSeries.kurtosisStandartDeviation()?.toPreciseFloatingPoints(preciseFloatingPoints)?: VALUE_NOT_EXIST,
                            unbiasedKurtosis?.let {
                                variationalSeries.sampleCharacteristicsConfidenceInterval(unbiasedKurtosis,
                                        variationalSeries.kurtosisStandartDeviation()!!)
                            } ?: VALUE_NOT_EXIST
                    ),
                    arrayOf("Коефіцієнт контрексцесу",
                            antiKurtosis?.toPreciseFloatingPoints(preciseFloatingPoints)?.toDouble() ?: VALUE_NOT_EXIST,
                            variationalSeries.antiKurtosisStandartDeviation()?.toPreciseFloatingPoints(preciseFloatingPoints) ?: VALUE_NOT_EXIST,
                            antiKurtosis?.let {
                                variationalSeries.sampleCharacteristicsConfidenceInterval(antiKurtosis,
                                        variationalSeries.antiKurtosisStandartDeviation()!!)
                            } ?: VALUE_NOT_EXIST
                    ),
                    arrayOf("Коефіцієнт варіації",
                            cv?.toPreciseFloatingPoints(preciseFloatingPoints)?.toDouble() ?: VALUE_NOT_EXIST,
                            variationalSeries.cvStandartDeviation()?.toPreciseFloatingPoints(preciseFloatingPoints) ?: VALUE_NOT_EXIST,
                            cv?.let {
                                variationalSeries.sampleCharacteristicsConfidenceInterval(cv,
                                        variationalSeries.cvStandartDeviation()!!)
                            } ?: VALUE_NOT_EXIST

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