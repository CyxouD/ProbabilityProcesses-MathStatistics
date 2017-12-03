package primary_statistical_analysis

/**
 * Created by Cyxou on 12/3/17.
 */
fun Double.toPreciseFloatingPoints(p: Int) = "%.${p}f".format(this)
fun Double.square() = Math.pow(this, 2.0)

