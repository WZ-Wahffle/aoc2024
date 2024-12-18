@Suppress("unused")
class Day18 : Day {
    private data class Point(val x: Int, val y: Int) {
        companion object {
            val UP = Point(0, -1)
            val DOWN = Point(0, 1)
            val LEFT = Point(-1, 0)
            val RIGHT = Point(1, 0)
        }

        operator fun plus(p: Point): Point {
            return Point(p.x + x, p.y + y)
        }
    }

    private enum class Direction {
        UP, DOWN, LEFT, RIGHT
    }

    private fun preProc(input: List<String>, cutoff: Int, firstCount: Int): List<Point> {
        return input.map { Point(it.split(",")[0].toInt(), it.split(",")[1].toInt()) }
            .filter { it.x <= cutoff && it.y <= cutoff }.take(firstCount)
    }

    private fun step(
        pos: Point,
        goal: Point,
        maxCoordinate: Int,
        obstacles: List<Point>,
        visited: MutableMap<Point, Int>,
        stepCount: Int
    ) {
        if (stepCount > maxCoordinate * 20) return
        if (visited.contains(pos) && visited[pos]!! <= stepCount) return
        visited[pos] = stepCount
        if (pos == goal) return

        if (pos.y > 0 && !obstacles.contains(pos + Point.UP)) step(
            pos + Point.UP, goal, maxCoordinate, obstacles, visited, stepCount + 1
        )
        if (pos.y < maxCoordinate && !obstacles.contains(pos + Point.DOWN)) step(
            pos + Point.DOWN, goal, maxCoordinate, obstacles, visited, stepCount + 1
        )
        if (pos.x > 0 && !obstacles.contains(pos + Point.LEFT)) step(
            pos + Point.LEFT, goal, maxCoordinate, obstacles, visited, stepCount + 1
        )
        if (pos.x < maxCoordinate && !obstacles.contains(pos + Point.RIGHT)) step(
            pos + Point.RIGHT, goal, maxCoordinate, obstacles, visited, stepCount + 1
        )
    }

    override fun part1(input: List<String>) {
        val (cutoff, firstCount) = if (input.size > 40) Pair(70, 1024) else Pair(6, 12)

        val obstacles = preProc(input, cutoff, firstCount)
        val start = Point(0, 0)
        val end = Point(cutoff, cutoff)
        val visited = mutableMapOf<Point, Int>()
        step(start, end, cutoff, obstacles, visited, 0)
        println(visited[end])
    }

    override fun part2(input: List<String>) {
        val (cutoff, firstCount) = if (input.size > 40) Pair(70, 1024) else Pair(6, 12)
        val start = Point(0, 0)
        val end = Point(cutoff, cutoff)
        val visited = mutableMapOf<Point, Int>()

        for (i in (firstCount..input.size).reversed()) {
            val obstacles = preProc(input, cutoff, i)
            step(start, end, cutoff, obstacles, visited, 0)
            if (visited.contains(end)) {
                println(input[i])
                break
            }
            visited.clear()
        }


    }
}