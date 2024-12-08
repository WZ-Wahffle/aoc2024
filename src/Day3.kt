class Day3: Day {
    override fun part1(input: List<String>) {
        println("""mul\((?<first>\d{1,3}),(?<second>\d{1,3})\)""".toRegex()
            .findAll(input.joinToString(""))
            .fold(0) { acc, next ->
                acc + (next.groups["first"]!!.value.toInt() * next.groups["second"]!!.value.toInt())
            }
        )
    }

    override fun part2(input: List<String>) {
        var doAdd = true
        println("""mul\((?<first>\d{1,3}),(?<second>\d{1,3})\)|do\(\)|don't\(\)""".toRegex()
            .findAll(input.joinToString(""))
            .fold(0) { acc, next ->
                when (next.value) {
                    "do()" -> {
                        doAdd = true
                        acc
                    }

                    "don't()" -> {
                        doAdd = false
                        acc
                    }

                    else -> {
                        acc + if (doAdd) (next.groups["first"]!!.value.toInt() * next.groups["second"]!!.value.toInt()) else 0
                    }
                }
            })
    }
}