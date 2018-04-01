package primary_statistical_analysis.sample_characteristics

class Median(orderedSample: List<Double>) : SampleCharacteristic(orderedSample) {
    companion object {
        fun findMedian(results: List<Double>): Double {
            return if (results.size % 2 == 0) {
                results[results.size / 2].plus(results[(results.size / 2).dec()]).div(2)
            } else {
                results[results.size / 2]
            }
        }

    }

    override fun unBiasedValue() = orderedSample.let { results ->
        if (orderedSample.size.toDouble() != 0.0) {
            Median.Companion.findMedian(results)
        } else {
            null
        }
    }

    override fun biasedValue() = null

    override fun standartDeviation() = null

    override fun confidenceInterval() = null

    override fun name() = "Медіана"
}