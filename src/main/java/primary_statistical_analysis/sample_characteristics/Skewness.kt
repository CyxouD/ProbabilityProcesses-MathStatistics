package primary_statistical_analysis.sample_characteristics

class Skewness(orderedSample: List<Double>) : SampleCharacteristic(orderedSample) {
    override fun biasedValue() = if (orderedSample.size.toDouble() > 1.0) {
        orderedSample.let { results ->
            results.map { result ->
                Math.pow(result - Average(orderedSample).unBiasedValue()!!, 3.0)
            }.sum().div(results.size * Math.pow(StandartDeviation(orderedSample).biasedValue()!!, 3.0))
        }
    } else {
        null
    }


    override fun unBiasedValue() = when (orderedSample.size.toDouble()) {
        0.0, 1.0 -> null
        2.0 -> 0.0
        else -> biasedValue()!! * Math.sqrt(orderedSample.size.toDouble() * orderedSample.size.toDouble().dec()).div(orderedSample.size.toDouble() - 2)
    }


    override fun standartDeviation() = if (orderedSample.size.toDouble() > 1) {
        Math.sqrt(
                6 * (orderedSample.size.toDouble() - 2) / orderedSample.size.toDouble().inc() / (orderedSample.size.toDouble() + 3)
        )
    } else {
        null
    }

    override fun name() = "Коефіцієнт асиметрії"
}