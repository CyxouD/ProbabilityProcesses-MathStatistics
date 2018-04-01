package primary_statistical_analysis.sample_characteristics

import primary_statistical_analysis.square

class CV(orderedSample: List<Double>) : SampleCharacteristic(orderedSample) {
    override fun biasedValue() =
            if (orderedSample.size.toDouble() != 0.0) {
                StandartDeviation(orderedSample).unBiasedValue()!! / Average(orderedSample).unBiasedValue()!!
            } else {
                null
            }

    override fun unBiasedValue() = biasedValue()

    override fun standartDeviation() =
            if (orderedSample.size.toDouble() != 0.0) {
                unBiasedValue()!! * Math.sqrt((2 * unBiasedValue()!!.square() + 1).div(2 * orderedSample.size.toDouble()))
            } else {
                null
            }

    override fun name() = "Коефіцієнт варіації"
}