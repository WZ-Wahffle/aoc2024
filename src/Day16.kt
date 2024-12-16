import kotlin.math.abs
import kotlin.math.min

@Suppress("unused")
class Day16 : Day {
    private data class Point(val x: Int, val y: Int) {
        operator fun plus(p: Point): Point {
            return Point(x + p.x, y + p.y)
        }

        fun taxicabDist(p: Point): Int {
            return abs(x - p.x) + abs(y - p.y)
        }
    }

    private enum class Direction {
        UP, DOWN, LEFT, RIGHT;

        fun vec(): Point {
            return when (this) {
                UP -> Point(0, -1)
                DOWN -> Point(0, 1)
                LEFT -> Point(-1, 0)
                RIGHT -> Point(1, 0)
            }
        }

        fun turnLeft(): Direction {
            return when (this) {
                UP -> LEFT
                DOWN -> RIGHT
                LEFT -> DOWN
                RIGHT -> UP
            }
        }

        fun turnRight(): Direction {
            return when (this) {
                UP -> RIGHT
                DOWN -> LEFT
                LEFT -> UP
                RIGHT -> DOWN
            }
        }
    }

    private operator fun <T> List<List<T>>.get(p: Point): T {
        return this[p.y][p.x]
    }

    private fun <T> List<List<T>>.find2D(elem: T): Point {
        return Point(this.first { it.contains(elem) }.indexOf(elem), this.indexOfFirst { it.contains(elem) })
    }

    private fun preProc(input: List<String>): Triple<List<List<Char>>, Point, Point> {
        return Triple(
            input.map { it.toList().map { it2 -> if (it2 == 'S' || it2 == 'E') '.' else it2 } },
            input.map { it.toList() }.find2D('S'),
            input.map { it.toList() }.find2D('E'),
        )
    }

    private val visited = mutableMapOf<Pair<Point, Direction>, Long>()

    private fun step(
        map: List<List<Char>>,
        pos: Point,
        end: Point,
        direction: Direction,
        score: Long,
        remainingSteps: Int,
    ): Long {
        if (pos == end) {
            visited[Pair(pos, direction)] = score
            return score
        }
        if (remainingSteps == 0) return -1L
        if ((visited[Pair(pos, direction)] ?: Long.MAX_VALUE) < score) return -1L
        visited[Pair(pos, direction)] = score

        var minScore = Long.MAX_VALUE
        if (map[pos + direction.vec()] != '#') {
            val new = step(map, pos + direction.vec(), end, direction, score + 1L, remainingSteps - 1)
            if (new != -1L) {
                minScore = min(minScore, new)
            }
        }
        if (map[pos + direction.turnLeft().vec()] != '#') {
            val new = step(map, pos, end, direction.turnLeft(), score + 1000L, remainingSteps - 1)
            if (new != -1L) {
                minScore = min(minScore, new)
            }
        }
        if (map[pos + direction.turnRight().vec()] != '#') {
            val new = step(map, pos, end, direction.turnRight(), score + 1000L, remainingSteps - 1)
            if (new != -1L) {
                minScore = min(minScore, new)
            }
        }

        return minScore
    }

    private fun collectBackwards(
        map: MutableMap<Pair<Point, Direction>, Long>,
        scoreOfTarget: Long,
        pos: Pair<Point, Direction>,
        start: Pair<Point, Direction>
    ) {
        if (pos == start) return
        val straightPoint = map.filterValues { it == scoreOfTarget - 1 }.filterKeys {
            it.second == pos.second && it.first.taxicabDist(
                pos.first
            ) == 1
        }.keys
        val rotationPoints = map.filterValues { it == scoreOfTarget - 1000 }.filterKeys {
            it.second != pos.second.turnRight().turnRight() && it.first == pos.first
        }.keys

        reverseCollectedPoints += rotationPoints.map { it.first }
        reverseCollectedPoints += straightPoint.map { it.first }

        straightPoint.forEach {
            collectBackwards(map, scoreOfTarget - 1, it, start)
        }
        rotationPoints.forEach {
            collectBackwards(map, scoreOfTarget - 1000, it, start)
        }
    }

    private val storeBetweenParts = mutableMapOf<List<String>, MutableMap<Pair<Point, Direction>, Long>>()
    private val reverseCollectedPoints = mutableSetOf<Point>()

    override fun part1(input: List<String>) {
        visited.clear()
        val (map, start, end) = preProc(input)
        val result = step(map, start, end, Direction.RIGHT, 0L, 800)
        println(result)
        storeBetweenParts[input] = visited.filter { it.value <= result }.toMutableMap()
    }

    override fun part2(input: List<String>) {
        visited.clear()
        val map = storeBetweenParts[input]!!
        val result = map.values.max()
        val start = map.filterValues { it == 0L }.keys.first()
        val end = map.filterValues { it == result }.keys.first()

        reverseCollectedPoints.clear()
        reverseCollectedPoints.add(start.first)
        reverseCollectedPoints.add(end.first)
        collectBackwards(map, result, end, start)
        println(reverseCollectedPoints.size)
    }
}