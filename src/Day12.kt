import kotlin.math.max

@Suppress("unused")
class Day12 : Day {
    private data class Point(val x: Int, val y: Int) {
        constructor(xy: Pair<Int, Int>) : this(xy.first, xy.second)

        override fun equals(other: Any?): Boolean {
            if (other !is Point) return false
            return x == other.x && y == other.y
        }

        override fun hashCode(): Int {
            var result = x
            result = 31 * result + y
            return result
        }

        operator fun minus(point: Point): Point {
            return Point(x - point.x, y - point.y)
        }

        fun up(d: Int): Point = Point(x, y - d)
        fun down(d: Int): Point = Point(x, y + d)
        fun left(d: Int): Point = Point(x - d, y)
        fun right(d: Int): Point = Point(x + d, y)

    }

    private fun <T, U> List<T>.permute(other: List<U>): List<Pair<T, U>> {
        return this.map { List(size) { _ -> it }.zip(other) }.flatten()
    }

    private fun preProc(input: List<String>): List<List<Char>> {
        return input.map { it.toList() }
    }

    private fun step(start: Point, available: MutableList<Point>, map: List<List<Char>>): List<Point> {
        if (start.x < 0 || start.y < 0 || start.x > map[0].size || start.y > map.size) {
            return listOf()
        }

        val ret = mutableListOf<Point>()
        ret += start
        available.remove(ret.last())

        if (available.contains(Point(start.x, start.y - 1)) && map[start.y - 1][start.x] == map[start.y][start.x]) {
            ret += Point(start.x, start.y - 1)
            ret += step(Point(start.x, start.y - 1), available, map)
            available.remove(ret.last())
        }

        if (available.contains(Point(start.x, start.y + 1)) && map[start.y + 1][start.x] == map[start.y][start.x]) {
            ret += Point(start.x, start.y + 1)
            ret += step(Point(start.x, start.y + 1), available, map)
            available.remove(ret.last())
        }

        if (available.contains(Point(start.x - 1, start.y)) && map[start.y][start.x - 1] == map[start.y][start.x]) {
            ret += Point(start.x - 1, start.y)
            ret += step(Point(start.x - 1, start.y), available, map)
            available.remove(ret.last())
        }

        if (available.contains(Point(start.x + 1, start.y)) && map[start.y][start.x + 1] == map[start.y][start.x]) {
            ret += Point(start.x + 1, start.y)
            ret += step(Point(start.x + 1, start.y), available, map)
            available.remove(ret.last())
        }

        return ret
    }

    private fun generateMaps(map: List<List<Char>>): List<List<Point>> {
        val points = map.indices.toList().permute(map[0].indices.toList()).map { Point(it) }.toMutableList()
        val maps = mutableMapOf<Char, MutableList<List<Point>>>()

        while (points.isNotEmpty()) {
            val seed = points.random()
            val out = step(seed, points, map).toSet().toList()
            if (!maps.contains(map[seed.y][seed.x])) maps[map[seed.y][seed.x]] = mutableListOf()
            maps[map[seed.y][seed.x]]!! += out
        }

        return maps.values.flatten()
    }

    override fun part1(input: List<String>) {
        val map = preProc(input)
        val maps = generateMaps(map)

        println(maps.sumOf {
            it.sumOf { it2 ->
                var peri = 4
                if (it.contains(it2.up(1))) peri--
                if (it.contains(it2.down(1))) peri--
                if (it.contains(it2.left(1))) peri--
                if (it.contains(it2.right(1))) peri--

                peri
            } * it.size
        })
    }

    override fun part2(input: List<String>) {
        val map = preProc(input)
        val maps = generateMaps(map)

        println(maps.sumOf {
            it.sumOf { it2 ->
                var peri = 4
                if (it.contains(it2.up(1))) peri--
                if (it.contains(it2.down(1))) peri--
                if (it.contains(it2.left(1))) peri--
                if (it.contains(it2.right(1))) peri--

                if (!it.contains(it2.up(1)) && it.contains(it2.left(1)) && !it.contains(it2.up(1).left(1))) peri--
                if (!it.contains(it2.down(1)) && it.contains(it2.right(1)) && !it.contains(it2.down(1).right(1))) peri--
                if (!it.contains(it2.left(1)) && it.contains(it2.down(1)) && !it.contains(it2.left(1).down(1))) peri--
                if (!it.contains(it2.right(1)) && it.contains(it2.up(1)) && !it.contains(it2.right(1).up(1))) peri--

                max(0, peri)
            } * it.size
        })
    }
}