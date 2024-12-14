@Suppress("unused")
class Day6: Day {
    private data class Point(val x: Int, val y: Int)

    private enum class Direction {
        UP, DOWN, LEFT, RIGHT;

        fun turnRight(): Direction {
            return when (this) {
                UP -> RIGHT
                DOWN -> LEFT
                LEFT -> UP
                RIGHT -> DOWN
            }
        }
    }

    private fun String.indexOfAll(char: Char): List<Int> {
        return this.foldIndexed(listOf()) { idx, acc, next ->
            if (next == char) acc + idx else acc
        }
    }

    private fun preProc(input: List<String>): Triple<Set<Point>, Point, Direction> {
        val start = Point(input.first { it.contains("""[<>^v]""".toRegex()) }.indexOfAny(
            listOf(
                "v", "^", "<", ">"
            )
        ), input.indexOfFirst { it.contains("""[<>^v]""".toRegex()) })

        return Triple(
            input.foldIndexed(setOf()) { idx, acc, next ->
                acc + next.indexOfAll('#').map { Point(it, idx) }
            }, start, when (input[start.y][start.x]) {
                '<' -> Direction.LEFT
                '>' -> Direction.RIGHT
                '^' -> Direction.UP
                'v' -> Direction.DOWN
                else -> throw RuntimeException("unreachable")
            }
        )
    }

    private fun getPath(
        input: List<String>,
        obstacles: Set<Point>,
        start: Point,
        initialDirection: Direction
    ): Set<Pair<Point, Direction>> {
        var pos = start
        var direction = initialDirection
        val reached = mutableSetOf(Pair(start, initialDirection))

        while (pos.x >= 0 && pos.x < input[0].length && pos.y >= 0 && pos.y < input.size) {
            val nextPoint = when (direction) {
                Direction.UP -> Point(pos.x, pos.y - 1)
                Direction.DOWN -> Point(pos.x, pos.y + 1)
                Direction.LEFT -> Point(pos.x - 1, pos.y)
                Direction.RIGHT -> Point(pos.x + 1, pos.y)
            }

            if (obstacles.contains(nextPoint)) {
                direction = direction.turnRight()
            } else {
                pos = nextPoint
                if (reached.contains(Pair(pos, direction))) throw RuntimeException("loop found")
                reached += Pair(pos, direction)
            }
        }

        return reached
    }

    override fun part1(input: List<String>) {
        val (obstacles, start, initialDirection) = preProc(input)

        val reached = getPath(input, obstacles, start, initialDirection).map { it.first }

        println(reached.filter { it.x >= 0 && it.y >= 0 && it.x < input[0].length && it.y < input.size }.size)
    }

    override fun part2(input: List<String>) {
        val (obstacles, start, initialDirection) = preProc(input)

        println(getPath(input, obstacles, start, initialDirection).map { it.first }.toSet()
            .count {
                try {
                    getPath(input, obstacles + it, start, initialDirection)
                    false
                } catch (_: RuntimeException) {
                    true
                }
            })
    }
}