@Suppress("unused")
class Day21 : Day {
    private val firstMap = mapOf(
        Pair(Pair(NumberPad.A, NumberPad.ZERO), DirectionKeypad.from("<A")),
        Pair(Pair(NumberPad.A, NumberPad.A), DirectionKeypad.from("A")),
        Pair(Pair(NumberPad.ZERO, NumberPad.ZERO), DirectionKeypad.from("A")),
        Pair(Pair(NumberPad.ZERO, NumberPad.A), DirectionKeypad.from(">A")),
        Pair(Pair(NumberPad.ZERO, NumberPad.ONE), DirectionKeypad.from("^<A")),
        Pair(Pair(NumberPad.ONE, NumberPad.A), DirectionKeypad.from(">>vA")),
        Pair(Pair(NumberPad.ZERO, NumberPad.TWO), DirectionKeypad.from("^A")),
        Pair(Pair(NumberPad.TWO, NumberPad.A), DirectionKeypad.from("v>A")),
        Pair(Pair(NumberPad.ZERO, NumberPad.THREE), DirectionKeypad.from("^>A")),
        Pair(Pair(NumberPad.THREE, NumberPad.A), DirectionKeypad.from("vA")),
        Pair(Pair(NumberPad.ZERO, NumberPad.FOUR), DirectionKeypad.from("^^<A")),
        Pair(Pair(NumberPad.FOUR, NumberPad.A), DirectionKeypad.from(">>vvA")),
        Pair(Pair(NumberPad.ZERO, NumberPad.FIVE), DirectionKeypad.from("^^A")),
        Pair(Pair(NumberPad.FIVE, NumberPad.A), DirectionKeypad.from("vv>A")),
        Pair(Pair(NumberPad.ZERO, NumberPad.SIX), DirectionKeypad.from("^^>A")),
        Pair(Pair(NumberPad.SIX, NumberPad.A), DirectionKeypad.from("vvA")),
        Pair(Pair(NumberPad.ZERO, NumberPad.SEVEN), DirectionKeypad.from("^^^<A")),
        Pair(Pair(NumberPad.SEVEN, NumberPad.A), DirectionKeypad.from(">>vvvA")),
        Pair(Pair(NumberPad.ZERO, NumberPad.EIGHT), DirectionKeypad.from("^^^A")),
        Pair(Pair(NumberPad.EIGHT, NumberPad.A), DirectionKeypad.from("vvv>A")),
        Pair(Pair(NumberPad.ZERO, NumberPad.NINE), DirectionKeypad.from("^^^>A")),
        Pair(Pair(NumberPad.NINE, NumberPad.A), DirectionKeypad.from("vvvA")),
        Pair(Pair(NumberPad.A, NumberPad.ONE), DirectionKeypad.from("^<<A")),
        Pair(Pair(NumberPad.ONE, NumberPad.ZERO), DirectionKeypad.from(">vA")),
        Pair(Pair(NumberPad.ONE, NumberPad.ONE), DirectionKeypad.from("A")),
        Pair(Pair(NumberPad.ONE, NumberPad.TWO), DirectionKeypad.from(">A")),
        Pair(Pair(NumberPad.ONE, NumberPad.THREE), DirectionKeypad.from(">>A")),
        Pair(Pair(NumberPad.ONE, NumberPad.FOUR), DirectionKeypad.from("^A")),
        Pair(Pair(NumberPad.ONE, NumberPad.FIVE), DirectionKeypad.from("^>A")),
        Pair(Pair(NumberPad.ONE, NumberPad.SIX), DirectionKeypad.from("^>>A")),
        Pair(Pair(NumberPad.ONE, NumberPad.SEVEN), DirectionKeypad.from("^^A")),
        Pair(Pair(NumberPad.ONE, NumberPad.EIGHT), DirectionKeypad.from("^^>A")),
        Pair(Pair(NumberPad.ONE, NumberPad.NINE), DirectionKeypad.from("^^>>A")),
        Pair(Pair(NumberPad.A, NumberPad.TWO), DirectionKeypad.from("<^A")),
        Pair(Pair(NumberPad.TWO, NumberPad.ZERO), DirectionKeypad.from("vA")),
        Pair(Pair(NumberPad.TWO, NumberPad.ONE), DirectionKeypad.from("<A")),
        Pair(Pair(NumberPad.TWO, NumberPad.TWO), DirectionKeypad.from("A")),
        Pair(Pair(NumberPad.TWO, NumberPad.THREE), DirectionKeypad.from(">A")),
        Pair(Pair(NumberPad.TWO, NumberPad.FOUR), DirectionKeypad.from("<^A")),
        Pair(Pair(NumberPad.TWO, NumberPad.FIVE), DirectionKeypad.from("^A")),
        Pair(Pair(NumberPad.TWO, NumberPad.SIX), DirectionKeypad.from("^>A")),
        Pair(Pair(NumberPad.TWO, NumberPad.SEVEN), DirectionKeypad.from("<^^A")),
        Pair(Pair(NumberPad.TWO, NumberPad.EIGHT), DirectionKeypad.from("^^A")),
        Pair(Pair(NumberPad.TWO, NumberPad.NINE), DirectionKeypad.from("^^>A")),
        Pair(Pair(NumberPad.A, NumberPad.THREE), DirectionKeypad.from("^A")),
        Pair(Pair(NumberPad.THREE, NumberPad.ZERO), DirectionKeypad.from("<vA")),
        Pair(Pair(NumberPad.THREE, NumberPad.ONE), DirectionKeypad.from("<<A")),
        Pair(Pair(NumberPad.THREE, NumberPad.TWO), DirectionKeypad.from("<A")),
        Pair(Pair(NumberPad.THREE, NumberPad.THREE), DirectionKeypad.from("A")),
        Pair(Pair(NumberPad.THREE, NumberPad.FOUR), DirectionKeypad.from("<<^A")),
        Pair(Pair(NumberPad.THREE, NumberPad.FIVE), DirectionKeypad.from("<^A")),
        Pair(Pair(NumberPad.THREE, NumberPad.SIX), DirectionKeypad.from("^A")),
        Pair(Pair(NumberPad.THREE, NumberPad.SEVEN), DirectionKeypad.from("<<^^A")),
        Pair(Pair(NumberPad.THREE, NumberPad.EIGHT), DirectionKeypad.from("<^^A")),
        Pair(Pair(NumberPad.THREE, NumberPad.NINE), DirectionKeypad.from("^^A")),
        Pair(Pair(NumberPad.A, NumberPad.FOUR), DirectionKeypad.from("^^<<A")),
        Pair(Pair(NumberPad.FOUR, NumberPad.ZERO), DirectionKeypad.from(">vvA")),
        Pair(Pair(NumberPad.FOUR, NumberPad.ONE), DirectionKeypad.from("vA")),
        Pair(Pair(NumberPad.FOUR, NumberPad.TWO), DirectionKeypad.from("v>A")),
        Pair(Pair(NumberPad.FOUR, NumberPad.THREE), DirectionKeypad.from("v>>A")),
        Pair(Pair(NumberPad.FOUR, NumberPad.FOUR), DirectionKeypad.from("A")),
        Pair(Pair(NumberPad.FOUR, NumberPad.FIVE), DirectionKeypad.from(">A")),
        Pair(Pair(NumberPad.FOUR, NumberPad.SIX), DirectionKeypad.from(">>A")),
        Pair(Pair(NumberPad.FOUR, NumberPad.SEVEN), DirectionKeypad.from("^A")),
        Pair(Pair(NumberPad.FOUR, NumberPad.EIGHT), DirectionKeypad.from("^>A")),
        Pair(Pair(NumberPad.FOUR, NumberPad.NINE), DirectionKeypad.from("^>>A")),
        Pair(Pair(NumberPad.A, NumberPad.FIVE), DirectionKeypad.from("<^^A")),
        Pair(Pair(NumberPad.FIVE, NumberPad.ZERO), DirectionKeypad.from("vvA")),
        Pair(Pair(NumberPad.FIVE, NumberPad.ONE), DirectionKeypad.from("<vA")),
        Pair(Pair(NumberPad.FIVE, NumberPad.TWO), DirectionKeypad.from("vA")),
        Pair(Pair(NumberPad.FIVE, NumberPad.THREE), DirectionKeypad.from("v>A")),
        Pair(Pair(NumberPad.FIVE, NumberPad.FOUR), DirectionKeypad.from("<A")),
        Pair(Pair(NumberPad.FIVE, NumberPad.FIVE), DirectionKeypad.from("A")),
        Pair(Pair(NumberPad.FIVE, NumberPad.SIX), DirectionKeypad.from(">A")),
        Pair(Pair(NumberPad.FIVE, NumberPad.SEVEN), DirectionKeypad.from("<^A")),
        Pair(Pair(NumberPad.FIVE, NumberPad.EIGHT), DirectionKeypad.from("^A")),
        Pair(Pair(NumberPad.FIVE, NumberPad.NINE), DirectionKeypad.from("^>A")),
        Pair(Pair(NumberPad.A, NumberPad.SIX), DirectionKeypad.from("^^A")),
        Pair(Pair(NumberPad.SIX, NumberPad.ZERO), DirectionKeypad.from("<vvA")),
        Pair(Pair(NumberPad.SIX, NumberPad.ONE), DirectionKeypad.from("<<vA")),
        Pair(Pair(NumberPad.SIX, NumberPad.TWO), DirectionKeypad.from("<vA")),
        Pair(Pair(NumberPad.SIX, NumberPad.THREE), DirectionKeypad.from("vA")),
        Pair(Pair(NumberPad.SIX, NumberPad.FOUR), DirectionKeypad.from("<<A")),
        Pair(Pair(NumberPad.SIX, NumberPad.FIVE), DirectionKeypad.from("<A")),
        Pair(Pair(NumberPad.SIX, NumberPad.SIX), DirectionKeypad.from("A")),
        Pair(Pair(NumberPad.SIX, NumberPad.SEVEN), DirectionKeypad.from("<<^A")),
        Pair(Pair(NumberPad.SIX, NumberPad.EIGHT), DirectionKeypad.from("<^A")),
        Pair(Pair(NumberPad.SIX, NumberPad.NINE), DirectionKeypad.from("^A")),
        Pair(Pair(NumberPad.A, NumberPad.SEVEN), DirectionKeypad.from("^^^<<A")),
        Pair(Pair(NumberPad.SEVEN, NumberPad.ZERO), DirectionKeypad.from(">vvvA")),
        Pair(Pair(NumberPad.SEVEN, NumberPad.ONE), DirectionKeypad.from("vvA")),
        Pair(Pair(NumberPad.SEVEN, NumberPad.TWO), DirectionKeypad.from("vv>A")),
        Pair(Pair(NumberPad.SEVEN, NumberPad.THREE), DirectionKeypad.from("vv>>A")),
        Pair(Pair(NumberPad.SEVEN, NumberPad.FOUR), DirectionKeypad.from("vA")),
        Pair(Pair(NumberPad.SEVEN, NumberPad.FIVE), DirectionKeypad.from("v>A")),
        Pair(Pair(NumberPad.SEVEN, NumberPad.SIX), DirectionKeypad.from("v>>A")),
        Pair(Pair(NumberPad.SEVEN, NumberPad.SEVEN), DirectionKeypad.from("A")),
        Pair(Pair(NumberPad.SEVEN, NumberPad.EIGHT), DirectionKeypad.from(">A")),
        Pair(Pair(NumberPad.SEVEN, NumberPad.NINE), DirectionKeypad.from(">>A")),
        Pair(Pair(NumberPad.A, NumberPad.EIGHT), DirectionKeypad.from("<^^^A")),
        Pair(Pair(NumberPad.EIGHT, NumberPad.ZERO), DirectionKeypad.from("vvvA")),
        Pair(Pair(NumberPad.EIGHT, NumberPad.ONE), DirectionKeypad.from("<vvA")),
        Pair(Pair(NumberPad.EIGHT, NumberPad.TWO), DirectionKeypad.from("vvA")),
        Pair(Pair(NumberPad.EIGHT, NumberPad.THREE), DirectionKeypad.from("vv>A")),
        Pair(Pair(NumberPad.EIGHT, NumberPad.FOUR), DirectionKeypad.from("<vA")),
        Pair(Pair(NumberPad.EIGHT, NumberPad.FIVE), DirectionKeypad.from("vA")),
        Pair(Pair(NumberPad.EIGHT, NumberPad.SIX), DirectionKeypad.from("v>A")),
        Pair(Pair(NumberPad.EIGHT, NumberPad.SEVEN), DirectionKeypad.from("<A")),
        Pair(Pair(NumberPad.EIGHT, NumberPad.EIGHT), DirectionKeypad.from("A")),
        Pair(Pair(NumberPad.EIGHT, NumberPad.NINE), DirectionKeypad.from(">A")),
        Pair(Pair(NumberPad.A, NumberPad.NINE), DirectionKeypad.from("^^^A")),
        Pair(Pair(NumberPad.NINE, NumberPad.ZERO), DirectionKeypad.from("<vvvA")),
        Pair(Pair(NumberPad.NINE, NumberPad.ONE), DirectionKeypad.from("<<vvA")),
        Pair(Pair(NumberPad.NINE, NumberPad.TWO), DirectionKeypad.from("<vvA")),
        Pair(Pair(NumberPad.NINE, NumberPad.THREE), DirectionKeypad.from("vvA")),
        Pair(Pair(NumberPad.NINE, NumberPad.FOUR), DirectionKeypad.from("<<vA")),
        Pair(Pair(NumberPad.NINE, NumberPad.FIVE), DirectionKeypad.from("<vA")),
        Pair(Pair(NumberPad.NINE, NumberPad.SIX), DirectionKeypad.from("vA")),
        Pair(Pair(NumberPad.NINE, NumberPad.SEVEN), DirectionKeypad.from("<<A")),
        Pair(Pair(NumberPad.NINE, NumberPad.EIGHT), DirectionKeypad.from("<A")),
        Pair(Pair(NumberPad.NINE, NumberPad.NINE), DirectionKeypad.from("A")),
    )

    private val secondMap = mapOf(
        Pair(Pair(DirectionKeypad.A, DirectionKeypad.DOWN), DirectionKeypad.from("<vA")),
        Pair(Pair(DirectionKeypad.DOWN, DirectionKeypad.LEFT), DirectionKeypad.from("<A")),
        Pair(Pair(DirectionKeypad.LEFT, DirectionKeypad.LEFT), DirectionKeypad.from("A")),
        Pair(Pair(DirectionKeypad.LEFT, DirectionKeypad.A), DirectionKeypad.from(">>^A")),
        Pair(Pair(DirectionKeypad.A, DirectionKeypad.LEFT), DirectionKeypad.from("v<<A")),
        Pair(Pair(DirectionKeypad.LEFT, DirectionKeypad.DOWN), DirectionKeypad.from(">A")),
        Pair(Pair(DirectionKeypad.A, DirectionKeypad.RIGHT), DirectionKeypad.from("vA")),
        Pair(Pair(DirectionKeypad.RIGHT, DirectionKeypad.RIGHT), DirectionKeypad.from("A")),
        Pair(Pair(DirectionKeypad.RIGHT, DirectionKeypad.UP), DirectionKeypad.from("<^A")),
        Pair(Pair(DirectionKeypad.UP, DirectionKeypad.A), DirectionKeypad.from(">A")),
        Pair(Pair(DirectionKeypad.UP, DirectionKeypad.RIGHT), DirectionKeypad.from("v>A")),
        Pair(Pair(DirectionKeypad.RIGHT, DirectionKeypad.A), DirectionKeypad.from("^A")),
        Pair(Pair(DirectionKeypad.A, DirectionKeypad.A), DirectionKeypad.from("A")),
        Pair(Pair(DirectionKeypad.DOWN, DirectionKeypad.A), DirectionKeypad.from("^>A")),
        Pair(Pair(DirectionKeypad.A, DirectionKeypad.UP), DirectionKeypad.from("<A")),
        Pair(Pair(DirectionKeypad.LEFT, DirectionKeypad.UP), DirectionKeypad.from(">^A")),
        Pair(Pair(DirectionKeypad.UP, DirectionKeypad.LEFT), DirectionKeypad.from("v<A")),
        Pair(Pair(DirectionKeypad.RIGHT, DirectionKeypad.DOWN), DirectionKeypad.from("<A")),
        Pair(Pair(DirectionKeypad.DOWN, DirectionKeypad.RIGHT), DirectionKeypad.from(">A")),
        Pair(Pair(DirectionKeypad.UP, DirectionKeypad.UP), DirectionKeypad.from("A")),
        Pair(Pair(DirectionKeypad.DOWN, DirectionKeypad.DOWN), DirectionKeypad.from("A")),
    )

    private enum class NumberPad {
        SEVEN, EIGHT, NINE, FOUR, FIVE, SIX, ONE, TWO, THREE, ZERO, A;

        companion object {
            fun from(c: Char): NumberPad {
                return when (c) {
                    '0' -> ZERO
                    '1' -> ONE
                    '2' -> TWO
                    '3' -> THREE
                    '4' -> FOUR
                    '5' -> FIVE
                    '6' -> SIX
                    '7' -> SEVEN
                    '8' -> EIGHT
                    '9' -> NINE
                    'A' -> A
                    else -> throw RuntimeException("unreachable")
                }
            }
        }
    }

    private enum class DirectionKeypad {
        UP, DOWN, LEFT, RIGHT, A;

        companion object {
            fun from(c: Char): DirectionKeypad {
                return when (c) {
                    '^' -> UP
                    'v' -> DOWN
                    '<' -> LEFT
                    '>' -> RIGHT
                    'A' -> A
                    else -> throw RuntimeException("unreachable")
                }
            }

            fun from(s: String): List<DirectionKeypad> {
                return s.map { from(it) }
            }
        }

    }

    private fun firstIndirection(input: List<NumberPad>): List<DirectionKeypad> {
        var pos = NumberPad.A
        val ret = mutableListOf<DirectionKeypad>()

        input.forEach {
            ret += firstMap[Pair(pos, it)]!!
            pos = it
        }

        return ret
    }

    private val cache = mutableMapOf<Pair<List<DirectionKeypad>, Int>, Long>()

    private fun <T> List<T>.indexOfAll(toFind: T): List<Int> {
        return this.foldIndexed(listOf()) { idx, acc, next ->
            if (next == toFind) acc + idx else acc
        }
    }

    private fun indirectionRec(input: List<DirectionKeypad>, remaining: Int) : Long {
        if(input.isEmpty()) return 0
        if(cache.contains(Pair(input, remaining))) return cache[Pair(input, remaining)]!!
        if(remaining == 0) return input.size.toLong()
        if(input.count { it == DirectionKeypad.A } == 1) {
            val ret = indirectionRec(secondIndirection(input), remaining-1)
            cache[Pair(input, remaining)] = ret
            return ret
        } else {
            val indices = listOf(-1) + input.indexOfAll(DirectionKeypad.A)
            var ret = 0L
            for(i in 0..indices.size-2) {
                ret += indirectionRec(input.subList(indices[i]+1, indices[i+1]+1), remaining)
            }
            cache[Pair(input, remaining)] = ret
            return ret
        }
    }

    private fun secondIndirection(input: List<DirectionKeypad>): List<DirectionKeypad> {
        var pos = DirectionKeypad.A
        val ret = mutableListOf<DirectionKeypad>()

        input.forEach {
            ret += secondMap[Pair(pos, it)]!!
            pos = it
        }

        return ret
    }

    override fun part1(input: List<String>) {
        println(input.fold(0L) { acc, pass ->
            acc + indirectionRec(firstIndirection(pass.map { NumberPad.from(it) }), 2) * pass.dropLast(
                1
            ).toLong()
        })
    }

    override fun part2(input: List<String>) {
        println(input.fold(0L) { acc, pass ->
            acc + indirectionRec(firstIndirection(pass.map { NumberPad.from(it) }), 25) * pass.dropLast(
                1
            ).toLong()
        })
    }
}