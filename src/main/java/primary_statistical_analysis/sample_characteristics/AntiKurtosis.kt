package primary_statistical_analysis.sample_characteristics

import primary_statistical_analysis.square

class AntiKurtosis(orderedSample: List<Double>) : SampleCharacteristic(orderedSample) {
    override fun biasedValue() = if (orderedSample.size.toDouble() > 1) {
        1.div(Math.sqrt(Math.abs(Kurtosis(orderedSample).biasedValue()!!)))
    } else {
        null
    }


    override fun unBiasedValue() = biasedValue()

    override fun standartDeviation() = if (orderedSample.size.toDouble() > 1) {
        Math.sqrt(Kurtosis(orderedSample).biasedValue()!!.div(29 * orderedSample.size.toDouble())) *
                Math.pow(Math.pow(Math.abs(Kurtosis(orderedSample).biasedValue()!!.square().dec()), 3.0), 1 / 4.0)
    } else {
        null
    }

    override fun name() = "Коефіцієнт контрексцесу"
}