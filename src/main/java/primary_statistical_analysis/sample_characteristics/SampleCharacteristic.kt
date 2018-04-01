package primary_statistical_analysis.sample_characteristics

import primary_statistical_analysis.VariationalSeries

abstract class SampleCharacteristic(val orderedSample: List<Double>) {
    abstract fun biasedValue(): Double?
    abstract fun unBiasedValue(): Double?
    abstract fun standartDeviation(): Double?
    abstract fun name(): String
    open fun confidenceInterval() = unBiasedValue()?.let { value ->
        val difference = 1.96 * standartDeviation()!!
        VariationalSeries.DoubleRange(value - difference, value + difference)
    }

}