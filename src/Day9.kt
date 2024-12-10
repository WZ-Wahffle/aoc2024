class Day9 : Day {
    private data class Gap(var start: Int, var size: Int)

    private fun compact(input: List<Int>): List<Int> {
        val ret = input.toMutableList()

        while (ret.contains(-1)) {
            val toTransfer = ret.removeLast()
            if (toTransfer == -1) continue
            ret[ret.indexOf(-1)] = toTransfer
        }

        return ret
    }

    private fun compactChunked(input: List<Int>, gaps: List<Gap>): List<Int> {
        val ret = input.toMutableList()
        var highestIdToTransfer = ret.last { it >= 0 }

        while (highestIdToTransfer > 0) {
            val start = ret.indexOf(highestIdToTransfer)
            val end = ret.lastIndexOf(highestIdToTransfer)
            if (start == -1 || end == -1) continue
            val length = end - start + 1

            val offset = gaps.firstOrNull { it.size >= length && it.start < start }
            if (offset != null) {
                for (i in offset.start..<offset.start + length) ret[i] = highestIdToTransfer
                for (i in start..end) ret[i] = -1
                offset.start += length
                offset.size -= length
            }

            highestIdToTransfer--
        }

        return ret
    }

    private fun preProc(input: List<String>): Pair<List<Int>, List<Gap>> {
        var id = 0
        val gaps = mutableListOf<Gap>()
        return Pair(input[0].chunked(2).fold(listOf()) { acc, it ->
            id++
            if (it.length > 1 && it[1].digitToInt() != 0) gaps.add(Gap(acc.size + it[0].digitToInt(), it[1].digitToInt()))
            acc + List(it[0].digitToInt()) { id - 1 } + List(if (it.length > 1) it[1].digitToInt() else 0) { -1 }
        }, gaps)
    }

    override fun part1(input: List<String>) {
        println(compact(preProc(input).first).foldIndexed(0L) { idx, acc, it ->
            if (it == -1) acc else acc + idx * it
        })
    }

    override fun part2(input: List<String>) {
        println(compactChunked(preProc(input).first, preProc(input).second).foldIndexed(0L) { idx, acc, it ->
            if (it == -1) acc else acc + idx * it
        })
    }
}