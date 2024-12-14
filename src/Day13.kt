@Suppress("unused")
class Day13 : Day {
    private data class Equation(
        val a11: Double,
        val a12: Double,
        val b1: Double,
        val a21: Double,
        val a22: Double,
        val b2: Double
    ) {
        fun solveForY(): Long? {
            val ret = (b1 * a21 - b2 * a11) / (a21 * a12 - a11 * a22)
            return if (ret % 1 != 0.0) null else ret.toLong()
        }

        fun solveForX(y: Long?): Long? {
            if (y == null) return null
            val ret = (b1 - a12 * y) / a11
            return if (ret % 1 != 0.0) null else ret.toLong()
        }
    }

    private fun preProc(input: List<String>, prizeOffset: Double = 0.0): Equation {
        """X[+=](\d+), Y[+=](\d+)""".toRegex().findAll(input.joinToString("")).toList().apply {
            return Equation(
                this[0].groups[1]?.value!!.toDouble(),
                this[1].groups[1]?.value!!.toDouble(),
                this[2].groups[1]?.value!!.toDouble() + prizeOffset,
                this[0].groups[2]?.value!!.toDouble(),
                this[1].groups[2]?.value!!.toDouble(),
                this[2].groups[2]?.value!!.toDouble() + prizeOffset
            )
        }
    }

    override fun part1(input: List<String>) {
        println(input.chunked(4).fold(0L) { acc, it ->
            val eq = preProc(it)

            val y = eq.solveForY()
            val x = eq.solveForX(y)

            acc + if (x == null || y == null) 0L else 3L * x + y
        })
    }

    override fun part2(input: List<String>) {
        println(input.chunked(4).fold(0L) { acc, it ->
            val eq = preProc(it, 10000000000000.0)

            val y = eq.solveForY()
            val x = eq.solveForX(y)

            acc + if (x == null || y == null) 0L else 3L * x + y
        })
    }
}