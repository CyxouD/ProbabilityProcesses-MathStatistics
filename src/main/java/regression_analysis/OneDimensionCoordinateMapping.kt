package regression_analysis

import javafx.geometry.Point2D
import java.awt.Point

/**
 * Created by Cyxou on 4/19/18.
 */
object OneDimensionCoordinateMapping {
    fun map(points: Array<Point2D>, xToT: (Double) -> Double, yToZ: (Double) -> Double) =
            points.map { Point2D(xToT(it.x), yToZ(it.y)) }.toTypedArray()

}