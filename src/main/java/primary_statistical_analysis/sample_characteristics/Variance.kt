package primary_statistical_analysis.sample_characteristics

import primary_statistical_analysis.square

class Variance(orderedSample: List<Double>) : SampleCharacteristic(orderedSample) {
    override fun biasedValue() = orderedSample.let { results ->
        when (orderedSample.size.toDouble()) {
            0.0 -> null
            1.0 -> 0.0
            else -> deviationSum(results).div(results.size)
        }
    }

    override fun unBiasedValue() = orderedSample.let { results ->
        when (orderedSample.size.toDouble()) {
            0.0 -> null
            1.0 -> 0.0
            else -> deviationSum(results).div(results.size.dec())
        }
    }

    override fun standartDeviation() = unBiasedValue()!!.div(Math.sqrt(2 * orderedSample.size.toDouble())).square()

    override fun name() = "Дисперсія"

    private fun deviationSum(results: List<Double>) = results.map { result -> (result - Average(orderedSample).unBiasedValue()!!).square() }.sum()
}