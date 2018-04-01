package primary_statistical_analysis.sample_characteristics

class StandartDeviation(orderedSample: List<Double>) : SampleCharacteristic(orderedSample) {
    override fun biasedValue() = if (orderedSample.size.toDouble() != 0.0) {
        Math.sqrt(Variance(orderedSample).biasedValue()!!)
    } else {
        null
    }

    override fun unBiasedValue() = if (orderedSample.size.toDouble() != 0.0) {
        Math.sqrt(Variance(orderedSample).unBiasedValue()!!)
    } else {
        null
    }

    override fun standartDeviation() = if (orderedSample.size.toDouble() != 0.0) {
        unBiasedValue()!!.div(Math.sqrt(2 * orderedSample.size.toDouble()))
    } else {
        null
    }

    override fun name() = "Середньоквадратичне відхилення"
}