package correlation_analysis

import javafx.geometry.Point2D
import primary_statistical_analysis.DoubleRange

/**
 * Created by Cyxou on 4/2/18.
 */
abstract class CorrelationCoefficient(val points: Array<Point2D>) {
    operator fun Point2D.component1() = this.x
    operator fun Point2D.component2() = this.y

    protected val N = points.size.toDouble()

    constructor(points2d: Array<List<Double>>) : this(points2d.map { Point2D(it[0], it[1]) }.toTypedArray())

    abstract val coefficient: Double?
    abstract val statistics: Double?

    abstract fun coefficientConfidenceInterval(mistakeProbability: Double): DoubleRange?

    abstract fun isSignificant(mistakeProbability: Double): Boolean?
}