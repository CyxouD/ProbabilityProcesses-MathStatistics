package primary_statistical_analysis

/*
 * TableDisplaying.java requires no other files.
 */

import identification_and_recovery_of_distributions.UnionDistribution
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

            val tableRows: Array<Array<String>> = variationalSeries.variationalSeriesRows.mapIndexed {
                classIndex, variationalClass ->
                arrayOf(classIndex.inc().toString(),
                        variationalClass.result.toString(),
                        variationalClass.frequency.toString(),
                        variationalClass.relativeFrequency
                                .toPreciseFloatingPoints(preciseFloatingPoints),
                        variationalSeries.getEmpiricalDistributionFunction(variationalClass.result)
                                .toPreciseFloatingPoints(preciseFloatingPoints))
            }.toTypedArray()
            return addTable(tableRows, columnNames)
        }

        fun samplingCharacteristics(variationalSeries: VariationalSeries): TableDisplaying {
            val columnNames = arrayOf("", "Значення", "Середньоквадратичне відхилення", "95% Довірчий інтервал")

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
            ).map { it.map { it.toString() }.toTypedArray() }.toTypedArray()
            return addTable(tableRows, columnNames)
        }

        fun ocenkiParametrov(variationalSeries: VariationalSeries, distribution: UnionDistribution): TableDisplaying {
            val columnNames = arrayOf("Параметр", "Значення оцінки", "Середньоквадратичне відхилення", "95% Довірчий інтервал")

            val tableRows: Array<Array<String>> = distribution.getParametrsOcenki(variationalSeries).map {
                arrayOf(it.param,
                        it.ocenka.toPreciseFloatingPoints(preciseFloatingPoints),
                        it.ocenkaStandartDeviation.toPreciseFloatingPoints(preciseFloatingPoints),
                        it.confidenceInterval.toString())
            }.toTypedArray()
            return addTable(tableRows, columnNames)
        }


        fun variationSeriesByClasses(variationalSeriesDividedByClasses: VariationalSeries.VariationalSeriesDividedByClasses): TableDisplaying {
            val columnNames = arrayOf("№ класу", "Межі класу", "Частота", "Відносна частота", "Значення емпіричної функції розподілу")

            val tableRows: Array<Array<String>> = variationalSeriesDividedByClasses.variationalSeriesDividedByClasses
                    .mapIndexed { classIndex, variationalClass ->
                        arrayOf(classIndex.inc().toString(),
                                variationalClass.range.toString(),
                                variationalClass.frequency.toString(),
                                variationalClass.relativeFrequency
                                        .toPreciseFloatingPoints(preciseFloatingPoints),
                                variationalClass.empiricalDistributionFunction
                                        .toPreciseFloatingPoints(preciseFloatingPoints))
                    }.toTypedArray()
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