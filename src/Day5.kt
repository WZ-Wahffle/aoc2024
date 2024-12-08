class Day5: Day {
    private fun preProc(input: List<String>): Pair<Map<Int, List<Int>>, List<List<Int>>> {
        return input.filter { it.isNotEmpty() }.partition {
            it.contains('|')
        }.run {
            Pair(
                first.map { Pair(it.split('|')[0].toInt(), it.split('|')[1].toInt()) }
                    .groupBy { it.first }.map { Pair(it.key, it.value.map { it2 -> it2.second }) }
                    .toMap(),
                second.map { it.split(',').map { it2 -> it2.toInt() } })
        }
    }

    private fun isValidUpdate(update: List<Int>, rules: Map<Int, List<Int>>): Boolean {
        update.forEach {
            rules[it]?.forEach { it2 ->
                if (update.indexOf(it2) != -1 && update.indexOf(it2) < update.indexOf(it)) return false
            }
        }

        return true
    }

    private fun List<Int>.reorder(rules: Map<Int, List<Int>>): List<Int> {

        val applyingRules = rules.filter { this.contains(it.key) }
            .map { Pair(it.key, it.value.filter { it2 -> this.contains(it2) }) }
            .filter { it.second.isNotEmpty() }.toMap().toMutableMap()
        val remainingElements = this.toMutableList()
        val sorted = mutableListOf<Int>()

        for (i in remainingElements) {
            if (applyingRules[i] == null) {
                sorted.add(i)
            }
        }
        remainingElements.removeAll(sorted)

        while (remainingElements.isNotEmpty()) {
            val toRemove = applyingRules.firstNotNullOfOrNull {
                if (sorted.containsAll(it.value)) it.key else null
            }

            if (toRemove != null) {
                remainingElements.remove(toRemove)
                applyingRules.remove(toRemove)
                sorted.add(toRemove)
            }
        }


        return sorted.toList()
    }

    override fun part1(input: List<String>) {
        val (rules, updates) = preProc(input)

        println(updates.fold(0) { acc, next ->
            if (isValidUpdate(next, rules)) acc + next[next.size / 2] else acc
        })
    }

    override fun part2(input: List<String>) {
        val (rules, updates) = preProc(input)

        println(updates.fold(0) { acc, next ->
            acc + if (isValidUpdate(
                    next,
                    rules
                )
            ) 0 else next.reorder(rules)[next.size / 2]
        })
    }
}