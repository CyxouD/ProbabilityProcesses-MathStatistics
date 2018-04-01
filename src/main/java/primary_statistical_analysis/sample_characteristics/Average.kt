package primary_statistical_analysis.sample_characteristics

class Average(orderedSample: List<Double>) : primary_statistical_analysis.sample_characteristics.SampleCharacteristic(orderedSample) {
    override fun biasedValue() = if (orderedSample.size.toDouble() != 0.0) {
        orderedSample.average()
    } else null

    override fun unBiasedValue() = biasedValue()

    override fun standartDeviation() = if (orderedSample.size.toDouble() != 0.0) {
        StandartDeviation(orderedSample).unBiasedValue()!! / Math.sqrt(orderedSample.size.toDouble())
    } else {
        null
    }

    override fun name() = "Середнє арифметичне"
}