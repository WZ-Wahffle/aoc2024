class Day10 : Day {
    private data class Point(val x: Int, val y: Int)

    private fun String.indexOfAll(char: Char): List<Int> {
        return this.foldIndexed(listOf()) { idx, acc, next ->
            if (next == char) acc + idx else acc
        }
    }

    private fun preProc(input: List<String>): Pair<List<List<Int>>, List<Point>> {
        val map = input.map {
            it.map { it2 ->
                it2.digitToInt()
            }
        }

        val points = input.mapIndexed { idx, it ->
            it.indexOfAll('0').map { it2 ->
                Point(it2, idx)
            }
        }.flatten()

        return Pair(map, points)
    }

    private fun step(map: List<List<Int>>, pos: Point): Pair<Set<Point>, Int> {
        if (map[pos.y][pos.x] == 9) {
            return Pair(setOf(pos), 1)
        }

        val partOne = mutableSetOf<Point>()
        var partTwo = 0

        if (pos.y > 0 && map[pos.y][pos.x] - map[pos.y - 1][pos.x] == -1) {
            step(map, Point(pos.x, pos.y - 1)).apply {
                partOne += first
                partTwo += second
            }
        }
        if (pos.y < map.size - 1 && map[pos.y][pos.x] - map[pos.y + 1][pos.x] == -1) {
            step(map, Point(pos.x, pos.y + 1)).apply {
                partOne += first
                partTwo += second
            }
        }
        if (pos.x > 0 && map[pos.y][pos.x] - map[pos.y][pos.x - 1] == -1) {
            step(map, Point(pos.x - 1, pos.y)).apply {
                partOne += first
                partTwo += second
            }
        }
        if (pos.x < map[0].size - 1 && map[pos.y][pos.x] - map[pos.y][pos.x + 1] == -1) {
            step(map, Point(pos.x + 1, pos.y)).apply {
                partOne += first
                partTwo += second
            }
        }

        return Pair(partOne, partTwo)
    }

    override fun part1(input: List<String>) {
        val (map, starts) = preProc(input)
        println(starts.fold(0) { acc, next ->
            val score = step(map, next).first.size
            acc + score
        })
    }

    override fun part2(input: List<String>) {
        val (map, starts) = preProc(input)
        println(starts.fold(0) { acc, next ->
            val score = step(map, next).second
            acc + score
        })
    }
}