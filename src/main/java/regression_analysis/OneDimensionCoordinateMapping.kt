package regression_analysis

import javafx.geometry.Point2D
import java.awt.Point

/**
 * Created by Cyxou on 4/19/18.
 */
object OneDimensionCoordinateMapping {
    fun map(points: Array<List<Double>>, xToT: (Double) -> Double, yToZ: (Double) -> Double) =
            points.map { (x, y) -> listOf(xToT(x), yToZ(y)) }.toTypedArray()

}