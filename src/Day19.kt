@Suppress("unused")
class Day19 : Day {
    private val possible = mutableMapOf<String, Long>()

    private fun step(target: String, parts: List<String>): Long {
        if (possible.contains(target)) return possible[target]!!
        if (target.isEmpty()) {
            return 1
        }
        val ret = parts.sumOf {
            if (target.startsWith(it)) {
                step(target.removePrefix(it), parts)
            } else 0
        }
        possible[target] = ret
        return ret
    }

    private fun preProc(input: List<String>): Pair<List<String>, List<String>> {
        val availableTowels = input.first().split(", ")
        val targets = input.drop(2)
        return Pair(availableTowels, targets)
    }

    override fun part1(input: List<String>) {
        val (available, targets) = preProc(input)
        println(targets.count {
            val ret = step(it, available)
            ret > 0
        })
        possible.clear()
    }

    override fun part2(input: List<String>) {
        val (available, targets) = preProc(input)
        println(targets.sumOf {
            step(it, available)
        })
        possible.clear()
    }
}