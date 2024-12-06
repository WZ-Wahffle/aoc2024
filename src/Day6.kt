class Day6 {
    private data class Point(val x: Int, val y: Int)
    private data class Line(val p1: Point, val p2: Point) {
        override fun equals(other: Any?): Boolean {
            if (other == null || other !is Line) return false
            return (this.p1 == other.p1 && this.p2 == other.p2) || (this.p1 == other.p2 && this.p2 == other.p1)
        }

        override fun hashCode(): Int {
            var result = p1.hashCode()
            result = 31 * result + p2.hashCode()
            return result
        }
    }

    private data class Rectangle(val top: Int, val bottom: Int, val left: Int, val right: Int) {
        fun generateBorderPoints(): Set<Pair<Point, Direction>> {
            return ((left..right).fold(setOf<Pair<Point, Direction>>()) { acc, next ->
                acc + setOf(
                    Pair(Point(next, top), Direction.RIGHT),
                    Pair(Point(next, bottom), Direction.LEFT)
                )
            }) + ((top..bottom).fold(setOf()) { acc, next ->
                acc + setOf(
                    Pair(Point(left, next), Direction.UP),
                    Pair(Point(right, next), Direction.DOWN)
                )
            })
        }
    }

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

    private enum class Orientation {
        HORIZONTAL, VERTICAL
    }

    private fun String.indexOfAll(char: Char): List<Int> {
        return this.foldIndexed(listOf()) { idx, acc, next ->
            if (next == char) acc + idx else acc
        }
    }

    private inline fun <reified T> List<List<T>>.transpose(): List<List<T>> {
        return List(this.size) { y ->
            List(this[0].size) { x ->
                this[x][y]
            }
        }
    }

    private inline fun <reified T, reified U> List<T>.permute(other: List<U>): List<Pair<T, U>> =
        this.flatMap { List(other.size) { _ -> it }.zip(other) }

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

    private fun generatePossibleSides(
        inp: List<String>,
        obstacles: Set<Point>,
        orientation: Orientation
    ): List<Line> {
        val input = when (orientation) {
            Orientation.VERTICAL -> inp
            else -> inp.map { it.toList() }.transpose().map { it.joinToString("") }
        }

        return obstacles.filter {
            when (orientation) {
                Orientation.VERTICAL -> it.y < input.size - 1 && input[it.y + 1].contains('#') && !input[it.y + 1].indexOfAll(
                    '#'
                ).all { it2 -> it2 <= it.x }

                Orientation.HORIZONTAL -> it.x < input.size - 1 && input[it.x + 1].contains('#') && !input[it.x + 1].indexOfAll(
                    '#'
                ).all { it2 -> it2 >= it.y }
            }
        }.map {
            when (orientation) {
                Orientation.VERTICAL -> input[it.y + 1].indexOfAll('#').filter { it2 -> it2 > it.x }
                    .map { it2 -> Line(it, Point(it2, it.y + 1)) }[0]

                Orientation.HORIZONTAL -> input[it.x + 1].indexOfAll('#')
                    .filter { it2 -> it2 < it.y }.map { it2 -> Line(it, Point(it.x + 1, it2)) }[0]
            }
        }
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
                reached += Pair(pos, direction)
            }
        }

        return reached
    }

    fun part1(input: List<String>) {
        val (obstacles, start, initialDirection) = preProc(input)

        val reached = getPath(input, obstacles, start, initialDirection).map { it.first }

        println(reached.filter { it.x >= 0 && it.y >= 0 && it.x < input[0].length && it.y < input.size }.size)
    }

    fun part2(input: List<String>) {
        val (obstacles, start, initialDirection) = preProc(input)

        val v = generatePossibleSides(input, obstacles, Orientation.HORIZONTAL)
        val h = generatePossibleSides(input, obstacles, Orientation.VERTICAL)
        val path = getPath(input, obstacles, start, initialDirection)

        val valid = h.permute(v).filter {
            setOf(it.first.p1, it.first.p2, it.second.p1, it.second.p2).size < 4
        }.map {
            if (it.second.p2 == it.first.p1) {
                // bottom right point missing
                Rectangle(it.first.p2.y, it.second.p1.y, it.first.p1.x, it.first.p2.x - 1)
            } else if (it.first.p2 == it.second.p1) {
                // top left point missing
                Rectangle(it.second.p2.y, it.first.p1.y, it.first.p1.x + 1, it.first.p2.x)
            } else if (it.first.p1 == it.second.p1) {
                // top right point missing
                Rectangle(it.second.p2.y + 1, it.first.p1.y, it.second.p2.x, it.first.p2.x)
            } else if (it.first.p2 == it.second.p2) {
                // bottom left point missing
                Rectangle(it.first.p2.y, it.second.p1.y - 1, it.first.p1.x, it.second.p1.x)
            } else {
                throw RuntimeException("unreachable")
            }
        }.filter {
            val borderPoints = it.generateBorderPoints()
            val notObstructed = borderPoints.map { it2 -> it2.first }.intersect(obstacles).isEmpty()
            val crossed = it.generateBorderPoints().intersect(path).isNotEmpty()
            notObstructed && crossed
        }

        println(valid.size)
    }
}