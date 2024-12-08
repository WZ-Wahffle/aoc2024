class Day2: Day {
    private enum class Direction {
        ASC,
        DESC,
        NONE
    }

    private fun preProc(input: List<String>): List<List<Int>> =
        input.map { it.split(" ").map { it2 -> it2.toInt() } }

    private fun isSafe(input: List<Int>, direction: Direction): Boolean {
        return when (direction) {
            Direction.ASC -> input.foldIndexed(true) { idx, acc, next ->
                idx == 0 || (acc && next > input[idx - 1] && next <= input[idx - 1] + 3)
            }

            Direction.DESC -> input.foldIndexed(true) { idx, acc, next ->
                idx == 0 || (acc && next < input[idx - 1] && next >= input[idx - 1] - 3)
            }

            Direction.NONE -> false
        }
    }

    private fun generateReducedLists(input: List<Int>): List<List<Int>> {
        return input.foldIndexed(listOf<List<Int>>()) { idx, acc, _ ->
            listOf(*acc.toTypedArray(), input.filterIndexed { index, _ -> idx != index })
        }
    }

    private fun findDirection(input: List<Int>): Direction {
        for (i in input) {
            if (input.first() < i) {
                return Direction.ASC
            }
            if (input.first() > i) {
                return Direction.DESC
            }
        }

        return Direction.NONE
    }

    override fun part1(input: List<String>) {
        println(preProc(input).map {
            isSafe(it, findDirection(it))
        }.count { it })
    }

    override fun part2(input: List<String>) {
        println(preProc(input).map {
            generateReducedLists(it).any { it2 ->
                isSafe(it2, findDirection(it2))
            }
        }.count { it })
    }
}