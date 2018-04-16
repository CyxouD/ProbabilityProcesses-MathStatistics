package sample_uniformity

import primary_statistical_analysis.toPreciseFloatingPoints
import java.awt.Dimension
import javax.swing.JFrame
import javax.swing.JPanel
import javax.swing.JScrollPane
import javax.swing.JTable

/**
 * Created by Cyxou on 4/16/18.
 */
class TableDisplaying : JPanel() {

    companion object {
        fun sampleUniformityCriterions(firstSample: List<Double>, secondSample: List<Double>, mistakeProbability: Double)
                : TableDisplaying {
            val columnNames = arrayOf("Название критерия", "Статистика", "Что проверяет", "Результат", "Вероятность ошибки")

            val varianceEqualityFTest = VarianceEqualityFTest(firstSample, secondSample)
            val meansEqualityPairedTTest = MeansEqualityPairedTTest(firstSample, secondSample)
            val meansEqualityTwoSampleTTest = MeansEqualityTwoSampleTTest(firstSample, secondSample)
            val wilcoxonSignedRankPairedTest = WilcoxonSignedRankPairedTest(firstSample, secondSample)
            val wilcoxonUnpairedTest = WilcoxonUnpairedTest(firstSample, secondSample)
            val mannWhitneyTest = MannWhitneyTest(firstSample, secondSample)
            val tableRows = arrayOf(
                    arrayOf(varianceEqualityFTest::class.java.simpleName,
                            varianceEqualityFTest.statistics.toPreciseFloatingPoints(3),
                            "Равенство дисперсий",
                            if (varianceEqualityFTest.isCriterionTrue(mistakeProbability)!!) "Равны" else "Не равны",
                            mistakeProbability
                    ),
                    arrayOf(meansEqualityPairedTTest::class.java.simpleName,
                            meansEqualityPairedTTest.statistics.toPreciseFloatingPoints(3),
                            "Равенство мат ожиданий",
                            if (meansEqualityPairedTTest.isCriterionTrue(mistakeProbability)) "Равны" else "Не равны",
                            mistakeProbability
                    ),
                    arrayOf(meansEqualityTwoSampleTTest::class.java.simpleName,
                            meansEqualityTwoSampleTTest.statistics.toPreciseFloatingPoints(3),
                            "Равенство мат ожиданий",
                            if (meansEqualityTwoSampleTTest.isCriterionTrue(mistakeProbability)!!) "Равны" else "Не равны",
                            mistakeProbability
                    ),
                    arrayOf(wilcoxonSignedRankPairedTest::class.java.simpleName,
                            wilcoxonSignedRankPairedTest.statistics.toPreciseFloatingPoints(3),
                            "Равенство смещений",
                            if (wilcoxonSignedRankPairedTest.isCriterionTrue(mistakeProbability)) "Равны" else "Не равны",
                            mistakeProbability
                    ),
                    arrayOf(wilcoxonUnpairedTest::class.java.simpleName,
                            wilcoxonUnpairedTest.statistics.toPreciseFloatingPoints(3),
                            "Равенство смещений",
                            if (wilcoxonUnpairedTest.isCriterionTrue(mistakeProbability)) "Равны" else "Не равны",
                            mistakeProbability
                    ),
                    arrayOf(mannWhitneyTest::class.java.simpleName,
                            mannWhitneyTest.statistics.toPreciseFloatingPoints(3),
                            "Равенство смещений",
                            if (mannWhitneyTest.isCriterionTrue(mistakeProbability)) "Равны" else "Не равны",
                            mistakeProbability
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