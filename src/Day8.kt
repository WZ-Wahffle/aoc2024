@Suppress("unused")
class Day8 : Day {
    private data class Point(val x: Int, val y: Int) {
        fun getVector(other: Point): Point = Point(other.x - x, other.y - y)
        fun invert(): Point = Point(-x, -y)

        operator fun plus(other: Point): Point = Point(x + other.x, y + other.y)
        operator fun times(other: Int): Point = Point(x * other, y * other)
    }

    private fun <T> List<T>.permute(): List<Pair<T, T>> {
        return this.mapIndexed { idx, it -> List(size - idx) { _ -> it }.zip(this.reversed()) }
            .flatten().filter { it.first != it.second }
    }

    private fun preProc(input: List<String>): Map<Char, List<Point>> {
        val map = mutableMapOf<Char, MutableList<Point>>()
        input.forEachIndexed { y, it ->
            it.forEachIndexed { x, c ->
                if (c != '.') {
                    if (!map.contains(c)) map[c] = mutableListOf()
                    map[c]!!.add(Point(x, y))
                }
            }
        }

        return map
    }

    override fun part1(input: List<String>) {
        println(preProc(input).values.fold(listOf<Point>()) { acc, nodes ->
            acc + nodes.permute().fold(listOf()) { acc2, it ->
                acc2 + listOf(
                    it.first + it.first.getVector(it.second) * 2,
                    it.first + it.first.getVector(it.second).invert()
                )
            }
        }.filter { it.x >= 0 && it.x < input[0].length && it.y >= 0 && it.y < input.size }
            .toSet().size)
    }

    override fun part2(input: List<String>) {
        println(preProc(input).values.fold(listOf<Point>()) { acc, nodes ->
            acc + nodes.permute().fold(listOf()) { acc2, it ->
                val vec = it.first.getVector(it.second)
                acc2 + List(input.size * 2) { idx -> it.first + vec * (idx - input.size) }
            }
        }.filter { it.x >= 0 && it.x < input[0].length && it.y >= 0 && it.y < input.size }
            .toSet().size)
    }

}
