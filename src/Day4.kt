import kotlin.math.max

class Day4: Day {
    private inline fun <reified T> List<List<T>>.transpose(): List<List<T>> {
        return List(this.size) { y ->
            List(this[0].size) { x ->
                this[x][y]
            }
        }
    }

    private inline fun <reified T> List<List<T>>.diagonals(): List<List<T>> {
        return List(this.size * 2 - 1) { xOffset ->
            List(if (xOffset < this.size) xOffset + 1 else 2 * this.size - xOffset - 1) { yOffset ->
                this[max(
                    (this.size - 1) - xOffset, 0
                ) + yOffset][max(
                    xOffset - (this.size - 1),
                    0
                ) + yOffset]
            }
        }
    }

    override fun part1(input: List<String>) {
        println(
            input
                .sumOf {
                    it.windowed(4).map { it2 -> it2 == "XMAS" || it2 == "SAMX" }
                        .count { it2 -> it2 }
                } + input.map { it.toList() }.transpose().map { it.joinToString("") }
                .sumOf {
                    it.windowed(4).map { it2 -> it2 == "XMAS" || it2 == "SAMX" }
                        .count { it2 -> it2 }
                } + input.map { it.toList() }.diagonals().map { it.joinToString("") }
                .sumOf {
                    it.windowed(4).map { it2 -> it2 == "XMAS" || it2 == "SAMX" }
                        .count { it2 -> it2 }
                } + input.map { it.toList().reversed() }.diagonals().map { it.joinToString("") }
                .sumOf {
                    it.windowed(4).map { it2 -> it2 == "XMAS" || it2 == "SAMX" }
                        .count { it2 -> it2 }
                }
        )

    }

    override fun part2(input: List<String>) {
        var count = 0
        for (y in input.indices.drop(1).dropLast(1)) {
            for (x in input[y].indices.drop(1).dropLast(1)) {
                if (input[y][x] == 'A') {
                    if (listOf(
                            input[y - 1][x - 1],
                            input[y + 1][x + 1],
                            input[y + 1][x - 1],
                            input[y - 1][x + 1]
                        ).sorted().joinToString("") == "MMSS"
                        && input[y-1][x-1] != input[y+1][x+1]
                    ) {
                        count++
                    }
                }
            }
        }

        println(count)
    }
}