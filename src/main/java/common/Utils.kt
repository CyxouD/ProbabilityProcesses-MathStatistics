package common

import primary_statistical_analysis.DoubleRange

/**
 * Created by Cyxou on 4/8/18.
 */
object Utils {
     fun rangesByClassNumber(classNumber: Int, values: List<Double>): Pair<Double, List<DoubleRange>> {
        val minValue = values.min()!!
        val maxValue = values.max()!!
        val classWidth = maxValue.minus(minValue).div(classNumber)
        val ranges = (1..classNumber).map { index ->
            val startClassValue = minValue + classWidth * (index - 1)
            val endClassValue = if (index != classNumber) {
                minValue + classWidth * index
            } else maxValue //because of classWidth rounding
            DoubleRange(startClassValue, endClassValue)
        }
        return Pair(classWidth, ranges)
    }

}