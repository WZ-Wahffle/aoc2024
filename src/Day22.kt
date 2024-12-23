@Suppress("unused")
class Day22 : Day {
    private fun advanceSecretNumber(number: Long): Triple<Long, List<Long>, List<Long>> {
        var ret = number
        var previous = number
        val differences = mutableListOf(ret % 10)
        val values = mutableListOf(ret % 10)
        for (i in 0..<2000) {
            var old = ret
            ret *= 64
            ret = ret xor old
            ret %= 16777216

            old = ret
            ret /= 32
            ret = ret xor old
            ret %= 16777216

            old = ret
            ret *= 2048
            ret = ret xor old
            ret %= 16777216
            differences += ret % 10 - previous % 10
            values += ret % 10
            previous = ret
        }
        return Triple(ret, values, differences)
    }

    override fun part1(input: List<String>) {
        println(input.fold(0L) { acc, next ->
            acc + advanceSecretNumber(next.toLong()).first
        })
    }

    override fun part2(input: List<String>) {
        val valuesBySequences = input.map {
            val (_, numbers, differences) = advanceSecretNumber(it.toLong())
            numbers.zip(differences).mapIndexedNotNull { idx, it2 ->
                if (idx > 2) Pair(it2.first, differences.subList(idx - 3, idx + 1)) else null
            }
        }

        println(valuesBySequences.first().maxOf {
            val ret = valuesBySequences.drop(1).sumOf { it2 ->
                val ret = it2.find { it3 ->
                    it3.second == it.second
                }?.first ?: 0L
                ret
            }
            ret + it.first
        })
    }
}