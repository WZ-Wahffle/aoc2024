@Suppress("unused")
class Day23 : Day {
    private fun preProc(input: List<String>): Map<String, List<String>> {
        val ret = mutableMapOf<String, MutableList<String>>()

        input.forEach {
            val (first, second) = it.split("-")
            if (!ret.contains(first)) ret[first] = mutableListOf()
            if (!ret.contains(second)) ret[second] = mutableListOf()
            ret[first]!!.add(second)
            ret[second]!!.add(first)
        }

        return ret
    }

    private fun <T> List<T>.permute(): List<Pair<T, T>> {
        return this.mapIndexed { idx, it -> List(size - idx) { _ -> it }.zip(this.reversed()) }
            .flatten().filter { it.first != it.second }
    }

    override fun part1(input: List<String>) {
        val map = preProc(input)
        println(
            map.asSequence().filter { it.key.first() == 't' }
                .map {
                    it.value.permute().filter { it2 ->
                        map[it2.first]!!.contains(it2.second) && map[it2.second]!!.contains(it2.first)
                    }.map { it2 ->
                        listOf(it2.first, it2.second, it.key).sorted()
                    }
                }.flatten().toSet().toList().count()
        )
    }

    // observation: all nodes have the same number of connections
    override fun part2(input: List<String>) {
        val map = preProc(input)
        val connections = map[map.keys.first()]!!.size

        for (i in (0..connections).reversed()) {
            map.forEach {
                var prev = setOf<String>()
                if(it.value.count { it2 ->
                    val temp = map[it2]!!.intersect(it.value.filter { it3 -> it3 != it2 }.toSet() + it.key)
                        if(temp.size >= i) {
                            prev = temp + it2
                            true
                        } else false
                } >= i) {
                    println(prev.sorted().joinToString(","))
                    return
                }
            }

        }
    }
}