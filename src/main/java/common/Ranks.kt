package common

/**
 * Created by Cyxou on 4/15/18.
 */
object Ranks {
    fun ranks(sample: List<Double>): List<Double> {
        val sortedRanks = sortedRanks(sample)

        return sample.map { value -> sortedRanks[value]!! }
    }

    private fun sortedRanks(values: List<Double>): Map<Double, Double> {
        val sorted = values.sorted()
        val sortedRanks = sorted.map { number ->
            val indexNumberOfFirstNumber = sorted.indexOfFirst { it == number } + 1
            val indexNumberOfLastNumber = sorted.indexOfLast { it == number } + 1
            val identicalNumbers = indexNumberOfLastNumber - indexNumberOfFirstNumber + 1
            val rank = (indexNumberOfFirstNumber..indexNumberOfLastNumber).fold(0, { acc, next -> acc + next }) / identicalNumbers.toDouble()
            Pair(number, rank)
        }.toMap()
        return sortedRanks
    }
}
