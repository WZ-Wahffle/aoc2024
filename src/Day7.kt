class Day7: Day {
    private fun Long.decatenate(tail: Long): Long {
        return this.toString().dropLast(tail.toString().length).toLong()
    }

    private fun eval(input: List<Double>, target: Double): Boolean {
        if (input.size == 1) {
            return input.last() == target
        }

        return eval(input.dropLast(1), target - input.last()) || eval(
            input.dropLast(1), target / input.last()
        )
    }

    private fun eval2(input: List<Long>, target: Long): Boolean {
        if (target < 0) {
            return false
        }
        if (input.isEmpty()) {
            return target == 0L
        }
        val operand = input.last()
        val newInput = input.dropLast(1)

        val sum = eval2(newInput, target - operand)
        val mul = if (target % operand == 0L) eval2(
            newInput, target / operand
        ) else false
        val concat = if (target.toString()
                .endsWith(operand.toString()) && target.toString().length > operand.toString().length
        ) eval2(
            newInput, target.decatenate(operand)
        ) else false


        return sum || mul || concat
    }

    override fun part1(input: List<String>) {
        println(input.fold(0.0) { acc, line ->
            val target = line.split(':')[0].toDouble()
            val numbers =
                line.split(':')[1].split(' ').filter { it.isNotEmpty() }.map { it.toDouble() }

            val add = eval(numbers, target)
            if (add) acc + target else acc
        }.toLong())
    }

    override fun part2(input: List<String>) {
        println(input.fold(0L) { acc, line ->
            val target = line.split(':')[0].toLong()
            val numbers =
                line.split(':')[1].split(' ').filter { it.isNotEmpty() }.map { it.toLong() }

            val valid = eval2(numbers, target)
            if (valid) acc + target else acc
        })
    }
}