import kotlin.math.abs

@Suppress("unused")
class Day20 : Day {
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

        fun taxicabDist(p: Point): Int {
            return abs(x - p.x) + abs(y - p.y)
        }
    }

    private fun <T> List<List<T>>.find2D(elem: T): Point {
        return Point(this.first { it.contains(elem) }.indexOf(elem), this.indexOfFirst { it.contains(elem) })
    }

    private operator fun <T> List<List<T>>.get(p: Point): T {
        return this[p.y][p.x]
    }

    private fun <T> List<T>.counts(): Map<T, Int> {
        val ret = mutableMapOf<T, Int>()

        for (i in this) {
            if (!ret.contains(i)) ret[i] = 0
            ret[i] = ret[i]!! + 1
        }

        return ret
    }

    private fun <T> List<T>.permute(): List<Pair<T, T>> {
        return this.map { List(size) { _ -> it }.zip(this) }
            .flatten().filter { it.first != it.second }
    }

    private fun preProc(input: List<List<Char>>): List<Point> {
        val start = input.find2D('S')
        val end = input.find2D('E')
        val map = mutableListOf(start)
        var pos = start

        do {
            if ((input[pos + Point.UP] == '.' || input[pos + Point.UP] == 'E') && !map.contains(pos + Point.UP)) {
                pos += Point.UP
            } else if ((input[pos + Point.DOWN] == '.' || input[pos + Point.DOWN] == 'E') && !map.contains(pos + Point.DOWN)) {
                pos += Point.DOWN
            } else if ((input[pos + Point.LEFT] == '.' || input[pos + Point.LEFT] == 'E') && !map.contains(pos + Point.LEFT)) {
                pos += Point.LEFT
            } else if ((input[pos + Point.RIGHT] == '.' || input[pos + Point.RIGHT] == 'E') && !map.contains(pos + Point.RIGHT)) {
                pos += Point.RIGHT
            }
            map += pos
        } while (pos != end)
        return map
    }

    private fun getSavedTime(path: List<Point>, pos: Point, dist: Int): List<Int> {

        val ret = mutableListOf<Int>()
        val l = path.drop(path.indexOf(pos)+1)
        l.forEach {
            if(pos.taxicabDist(it) <= dist) {
                val toAdd = path.indexOf(it) - path.indexOf(pos) - pos.taxicabDist(it)
                if(toAdd > 0) ret += toAdd
            }
        }

        return ret
    }

    override fun part1(input: List<String>) {
        val list = preProc(input.map { it.toList() })

        println(list.map {
            getSavedTime(list, it, 2)
        }.flatten().filter {
            it > 0
        }.count {
            it >= 100
        })
    }



    override fun part2(input: List<String>) {
        val list = preProc(input.map { it.toList() })

        println(list.map {
            getSavedTime(list, it, 20)
        }.flatten().filter {
            it >= 50
        }.count {
            it >= 100
        })
    }
}