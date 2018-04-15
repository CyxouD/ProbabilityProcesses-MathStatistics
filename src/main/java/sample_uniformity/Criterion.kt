package sample_uniformity

import javafx.geometry.Point2D

/**
 * Created by Cyxou on 4/15/18.
 */
abstract class Criterion(protected val firstSample: List<Double>,
                         protected val secondSample: List<Double>) {

    protected val N1 = firstSample.size.toDouble()
    protected val N2 = secondSample.size.toDouble()

    abstract val statistics: Double?

    abstract fun isCriterionTrue(mistakeProbability: Double): Boolean?
}