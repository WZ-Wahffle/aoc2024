import kotlin.math.max

@Suppress("unused")
class Day25 : Day {
    private inline fun <reified T> List<List<T>>.transpose(): List<List<T>> {
        return List(this[0].size) { y ->
            List(this.size) { x ->
                this[x][y]
            }
        }
    }

    private fun <T, U> List<T>.permute(other: List<U>): List<Pair<T, U>> {
        return this.map { List(max(size, other.size)) { _ -> it }.zip(other) }.flatten()
    }

    private fun preProc(input: List<String>): Pair<MutableList<List<Int>>, MutableList<List<Int>>> {
        val locks = mutableListOf<List<Int>>()
        val keys = mutableListOf<List<Int>>()
        input.chunked(8).forEach {

            if(it[0][0] == '#') {
                locks += it.dropLast(1).map { it2 -> it2.toList() }.transpose().map { it2 -> it2.toString() }.map {
                        it2 -> it2.count { it3 -> it3 == '#' }-1
                }
            } else keys += it.dropLast(1).map { it2 -> it2.toList() }.transpose().map { it2 -> it2.toString() }.map {
                    it2 -> 5 - (it2.count { it3 -> it3 == '.' }-1)
            }
        }

        return Pair(locks, keys)
    }

    override fun part1(input: List<String>) {
        val (locks, keys) = preProc(input)
        println(locks.permute(keys)
            .count {
            it.first.zip(it.second).all { it2 -> it2.first + it2.second <= 5 }
        })
    }

    override fun part2(input: List<String>) {
        println()
    }
}