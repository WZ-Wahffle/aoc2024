class Day11 : Day {
    private fun <T> MutableMap<T, ULong>.addOrInc(elem: T, value: ULong) {
        if(!contains(elem)) this[elem] = 0UL
        this[elem] = this[elem]!! + value
    }

    private fun step(input: MutableMap<ULong, ULong>): MutableMap<ULong, ULong> {
        val out = mutableMapOf<ULong, ULong>()
        for(i in input) {
            if(i.key == 0UL) out.addOrInc(1UL, i.value)
            else if(i.key.toString().length % 2 == 0) {
                out.addOrInc(i.key.toString().substring(0, i.key.toString().length / 2).toULong(), i.value)
                out.addOrInc(i.key.toString().substring(i.key.toString().length / 2).toULong(), i.value)
            } else {
                out.addOrInc(i.key * 2024UL, i.value)
            }
        }

        return out
    }

    private fun preProc(input: List<String>): MutableMap<ULong, ULong> {
        val ret = mutableMapOf<ULong, ULong>()
        input.joinToString("").split(' ').map { it.toULong() }.forEach {
            if(!ret.contains(it)) ret[it] = 0UL
            ret[it] = ret[it]!! + 1UL
        }

        return ret
    }

    override fun part1(input: List<String>) {
        var map = preProc(input)
        for(i in 0..<25) {
            map = step(map)
        }

        println(map.map { it.value }.sum())
    }

    override fun part2(input: List<String>) {
        var map = preProc(input)
        for(i in 0..<75) {
            map = step(map)
        }

        println(map.map { it.value }.sum())
    }
}