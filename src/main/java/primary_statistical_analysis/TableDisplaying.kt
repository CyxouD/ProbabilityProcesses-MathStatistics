package primary_statistical_analysis

/*
 * TableDisplaying.java requires no other files.
 */

import javax.swing.JFrame
import javax.swing.JPanel
import javax.swing.JScrollPane
import javax.swing.JTable
import java.awt.Dimension

class TableDisplaying : JPanel {
    private val DEBUG = false

    private val preciseFloatingPoints = 3
    fun Double.toPreciseFloatingPoints(p: Int) = "%.${preciseFloatingPoints}f".format(this)

    constructor(variationalSeries: VariationalSeries) {
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
        addTable(tableRows, columnNames)
    }

    constructor(variationalSeriesDividedByClasses: VariationalSeries.VariationalSeriesDividedByClasses) {
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
        addTable(tableRows, columnNames)
    }

    private fun addTable(tableRows: Array<Array<Any>>, columnNames: Array<String>) {
        val table = JTable(tableRows, columnNames)
        table.preferredScrollableViewportSize = Dimension(500, 400)
        table.fillsViewportHeight = true
        //Create the scroll pane and add the table to it.
        val scrollPane = JScrollPane(table)

        //Add the scroll pane to this panel.
        add(scrollPane)
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