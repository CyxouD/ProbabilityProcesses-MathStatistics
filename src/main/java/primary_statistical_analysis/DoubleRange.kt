package primary_statistical_analysis

class DoubleRange(override val start: Double, override val endInclusive: Double) :
        ClosedRange<Double>,
        Comparable<DoubleRange> {
    operator fun component1() = start
    operator fun component2() = endInclusive

    override fun toString(): String {
        return "[${start.toPreciseFloatingPoints(Main.preciseFloatingPoints)};${endInclusive.toPreciseFloatingPoints(3)}]"
    }

    override fun compareTo(other: DoubleRange): Int {
        return this.start.compareTo(other.start).let { startCompare ->
            if (startCompare != 0) startCompare
            else {
                this.endInclusive.compareTo(other.endInclusive)
            }
        }
    }
}