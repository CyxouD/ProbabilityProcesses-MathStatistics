package correlation_analysis

import javafx.geometry.Point2D
import org.apache.commons.math3.exception.OutOfRangeException
import primary_statistical_analysis.sample_characteristics.Average
import primary_statistical_analysis.sample_characteristics.StandartDeviation

/**
 * Created by Cyxou on 4/2/18.
 */
interface CorrelationCoefficient {
    operator fun Point2D.component1() = this.x
    operator fun Point2D.component2() = this.y

    val points: Array<Point2D>

    fun coefficient(): Double?

    fun statistics(): Double?

    fun significance(probability: Double): Boolean?
}