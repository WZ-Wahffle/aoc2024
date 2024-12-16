@Suppress("unused")
class Day15 : Day {
    private data class Point(val x: Int, val y: Int) {
        operator fun plus(vec: Point): Point {
            return Point(x + vec.x, y + vec.y)
        }
    }

    private enum class Direction {
        UP, DOWN, LEFT, RIGHT;

        fun vec(): Point {
            return when (this) {
                UP -> Point(0, -1)
                DOWN -> Point(0, 1)
                LEFT -> Point(-1, 0)
                RIGHT -> Point(1, 0)
            }
        }

        fun cond(): (Int, Int, Int, Int) -> Boolean {
            return when (this) {
                UP -> { _, y, _, _ ->
                    y >= 0
                }

                DOWN -> { _, y, _, maxY ->
                    y < maxY
                }

                LEFT -> { x, _, _, _ ->
                    x >= 0
                }

                RIGHT -> { x, _, maxX, _ ->
                    x < maxX
                }
            }
        }
    }

    private operator fun <T> List<List<T>>.get(x: Int, y: Int): T {
        return this[y][x]
    }

    private operator fun <T> List<List<T>>.get(p: Point): T {
        return this[p.y][p.x]
    }

    private fun <T> List<List<T>>.find2D(elem: T): Point {
        return Point(this.first { it.contains(elem) }.indexOf(elem), this.indexOfFirst { it.contains(elem) })
    }

    private operator fun <T> MutableList<MutableList<T>>.set(p: Point, v: T) {
        this[p.y][p.x] = v
    }

    private fun preProc(input: List<String>): Pair<MutableList<MutableList<Char>>, MutableList<Direction>> {
        val map = input.takeWhile { it != "" }.map { it.toMutableList() }.toMutableList()
        val directions = input.takeLastWhile { it != "" }.map {
            it.map { it2 ->
                when (it2) {
                    '^' -> Direction.UP
                    'v' -> Direction.DOWN
                    '<' -> Direction.LEFT
                    '>' -> Direction.RIGHT
                    else -> throw RuntimeException("unreachable")
                }
            }
        }.flatten().toMutableList()

        return Pair(map, directions)
    }

    private fun step(map: MutableList<MutableList<Char>>, direction: Direction, pos: Point): Point {
        when (map[pos + direction.vec()]) {
            '.' -> {
                map[pos] = '.'
                map[pos + direction.vec()] = '@'
                return pos + direction.vec()
            }

            '#' -> return pos
            'O' -> {
                if (isPushable(map, direction, pos)) {
                    map[pos] = '.'
                    map[pos + direction.vec()] = '@'
                    var curPos = pos + direction.vec()
                    while (map[curPos] != '.') curPos += direction.vec()
                    map[curPos] = 'O'
                    return pos + direction.vec()
                } else {
                    return pos
                }
            }
        }

        throw RuntimeException("unreachable")
    }

    private fun isPushable(map: MutableList<MutableList<Char>>, direction: Direction, start: Point): Boolean {
        var pos = start + direction.vec()
        while (direction.cond().invoke(pos.x, pos.y, map[0].size, map.size)) {
            when (map[pos]) {
                '.' -> return true
                '#' -> return false
                'O' -> pos += direction.vec()
                else -> throw RuntimeException("unreachable")
            }
        }

        throw RuntimeException("unreachable")
    }

    private fun checkPushRecursive(map: MutableList<MutableList<Char>>, direction: Direction, start: Point): Boolean {
        if (start.x < 0 || start.y < 0 || start.x >= map[0].size || start.y >= map.size) return false
        return when (map[start + direction.vec()]) {
            '.' -> true
            '#' -> false
            '[' -> checkPushRecursive(
                map, direction, start + direction.vec()
            ) &&
                    if (direction != Direction.LEFT) checkPushRecursive(
                        map, direction, start + direction.vec() + Point(1, 0)
                    ) else true

            ']' -> checkPushRecursive(
                map, direction, start + direction.vec()
            ) &&
                    if (direction != Direction.RIGHT) checkPushRecursive(
                        map, direction, start + direction.vec() + Point(-1, 0)
                    ) else true

            else -> throw RuntimeException("unreachable")
        }
    }

    private fun stepRecursive(map: MutableList<MutableList<Char>>, direction: Direction, start: Point) {
        if (start.x < 0 || start.y < 0 || start.x >= map[0].size || start.y >= map.size) return
        if (checkPushRecursive(map, direction, start)) {
            when (map[start + direction.vec()]) {
                '.' -> {
                    map[start + direction.vec()] = map[start]
                    map[start] = '.'
                }

                '#' -> {

                }

                '[' -> {
                    if (direction == Direction.LEFT) {
                        stepRecursive(map, direction, start + direction.vec())
                        stepRecursive(map, direction, start + direction.vec() + Point(1, 0))
                    } else {
                        stepRecursive(map, direction, start + direction.vec() + Point(1, 0))
                        stepRecursive(map, direction, start + direction.vec())
                    }
                    map[start + direction.vec()] = map[start]
                    map[start] = '.'
                }

                ']' -> {
                    if (direction == Direction.RIGHT) {
                        stepRecursive(map, direction, start + direction.vec())
                        stepRecursive(map, direction, start + direction.vec() + Point(-1, 0))
                    } else {
                        stepRecursive(map, direction, start + direction.vec() + Point(-1, 0))
                        stepRecursive(map, direction, start + direction.vec())
                    }
                    map[start + direction.vec()] = map[start]
                    map[start] = '.'
                }

                else -> throw RuntimeException("unreachable")
            }
        }
    }

    override fun part1(input: List<String>) {
        val (map, directions) = preProc(input)
        while (directions.isNotEmpty()) step(map, directions.removeFirst(), map.find2D('@'))
        println(map.foldIndexed(0) { y, acc, next ->
            acc + next.foldIndexed(0) { x, acc2, it ->
                if (it == 'O') acc2 + (100 * y + x) else acc2
            }
        })
    }

    override fun part2(input: List<String>) {
        var map: MutableList<MutableList<Char>>
        var directions: MutableList<Direction>
        preProc(input).apply {
            directions = second
            map = first.map {
                it.map { it2 ->
                    when (it2) {
                        '#' -> listOf('#', '#')
                        'O' -> listOf('[', ']')
                        '.' -> listOf('.', '.')
                        '@' -> listOf('@', '.')
                        else -> throw RuntimeException("unreachable")
                    }
                }.flatten().toMutableList()
            }.toMutableList()
        }

        while (directions.isNotEmpty()) stepRecursive(map, directions.removeFirst(), map.find2D('@'))
        println(map.foldIndexed(0) { y, acc, next ->
            acc + next.foldIndexed(0) { x, acc2, it ->
                if (it == '[') acc2 + (100 * y + x) else acc2
            }
        })
    }
}