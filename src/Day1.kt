import kotlin.math.abs

@Suppress("unused")
class Day1: Day {
    override fun part1(input: List<String>) {
        val first = mutableListOf<Int>()
        val second = mutableListOf<Int>()
        input.forEach {
            first += it.split("   ")[0].toInt()
            second += it.split("   ")[1].toInt()
        }

        println(first.sorted().zip(second.sorted()).fold(0) {
            acc, it -> acc + abs(it.first - it.second)
        })
    }

    override fun part2(input: List<String>) {
        val first = mutableListOf<Int>()
        val second = mutableListOf<Int>()
        input.forEach {
            first += it.split("   ")[0].toInt()
            second += it.split("   ")[1].toInt()
        }

        println(first.fold(0) {
            acc, it -> acc + it * second.count { it2 -> it == it2 }
        })
    }
}