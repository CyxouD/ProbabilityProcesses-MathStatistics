package primary_statistical_analysis.sample_characteristics

import primary_statistical_analysis.square

class Kurtosis(orderedSample: List<Double>) : SampleCharacteristic(orderedSample) {
    override fun biasedValue() = if (orderedSample.size.toDouble() > 1.0) {
        orderedSample.let { results ->
            results.map { result ->
                Math.pow(result - Average(orderedSample).unBiasedValue()!!, 4.0)
            }.sum().div(results.size * Math.pow(StandartDeviation(orderedSample).biasedValue()!!, 4.0))
        }
    } else {
        null
    }


    override fun unBiasedValue() = if (orderedSample.size.toDouble() > 3) {
        orderedSample.size.toDouble().square().dec().div((orderedSample.size.toDouble() - 2) * (orderedSample.size.toDouble() - 3)) * ((biasedValue()!! - 3) + (6 / orderedSample.size.toDouble().inc()))
    } else {
        null
    }

    override fun standartDeviation() = if (orderedSample.size.toDouble() > 3) {
        Math.sqrt(24.div(orderedSample.size.toDouble()) * (1 - (225 / (15 * orderedSample.size.toDouble() + 124))))
    } else {
        null
    }

    override fun name() = "Коефіцієнт ексцесу"
}