import java.awt.*
import javax.swing.JButton
import javax.swing.JFrame
import javax.swing.JPanel
import kotlin.math.abs

@Suppress("unused")
class Day14 : Day {

    @Volatile
    var done = false

    @Volatile
    var checked = false


    private class Bitmap(val imgWidth: Int, val imgHeight: Int) : JPanel() {
        var step = -1
        val content = MutableList(imgHeight) { MutableList(imgWidth) { ' ' } }

        override fun getPreferredSize(): Dimension {
            return Dimension(imgWidth, imgHeight)
        }

        override fun paintComponent(g: Graphics?) {
            super.paintComponent(g)

            g!!
            g.font = Font(g.font.name, Font.PLAIN, 6)
            for (i in content.indices) {
                g.drawString(content[i].joinToString(""), 0, i * 5)
            }
            g.font = Font(g.font.name, Font.PLAIN, 18)
            g.drawString("$step", 0, content.size * 5 + 9)
        }
    }

    private enum class Quadrant {
        TL, BR, BL, TR
    }

    private data class Point(val x: Long, val y: Long) {
        operator fun plus(a: Point): Point = Point(x + a.x, y + a.y)
        operator fun times(a: Int): Point = Point(x * a, y * a)
        operator fun rem(a: Pair<Int, Int>): Point = Point(x % a.first, y % a.second)
    }

    private data class Vector(val pos: Point, val dir: Point) {
        fun step(count: Int, maxX: Int, maxY: Int): Point = (dir * count + pos) % Pair(maxX, maxY)
    }

    private fun Long.wrapAround(max: Int): Long {
        return if (this < 0) max - abs(this) else this
    }

    private fun preProc(input: List<String>, maxX: Int, maxY: Int): List<Vector> {
        return input.map {
            """p=(-?\d+),(-?\d+) v=(-?\d+),(-?\d+)""".toRegex().findAll(it).first().groups.chunked(5).map { it2 ->
                Vector(
                    Point(it2[1]!!.value.toLong(), it2[2]!!.value.toLong()),
                    Point(it2[3]!!.value.toLong().wrapAround(maxX), it2[4]!!.value.toLong().wrapAround(maxY))
                )
            }
        }.flatten()
    }

    override fun part1(input: List<String>) {
        val (maxX, maxY) = if (input.size > 50) Pair(101, 103) else Pair(11, 7)
        println(preProc(input, maxX, maxY).map {
                it.step(100, maxX, maxY)
            }.filter { it.x != maxX / 2L && it.y != maxY / 2L }.map {
                if (it.x < maxX / 2L && it.y < maxY / 2L) Quadrant.TL
                else if (it.x < maxX / 2L && it.y > maxY / 2L) Quadrant.BL
                else if (it.x > maxX / 2L && it.y < maxY / 2L) Quadrant.TR
                else if (it.x > maxX / 2L && it.y > maxY / 2L) Quadrant.BR
                else throw RuntimeException("Unreachable")
            }.let {
                it.count { it2 -> it2 == Quadrant.TL } * it.count { it2 -> it2 == Quadrant.BL } * it.count { it2 -> it2 == Quadrant.TR } * it.count { it2 -> it2 == Quadrant.BR }
            })
    }

    override fun part2(input: List<String>) {
        if (input.size < 50) return
        val (maxX, maxY) = Pair(101, 103)
        val startingMap = preProc(input, maxX, maxY)
        val buffer = MutableList(maxY) { MutableList(maxX) { ' ' } }
        for (i in 0..(maxX * maxY)) {
            startingMap.map { it.step(i, maxX, maxY) }.forEach {
                buffer[it.y.toInt()][it.x.toInt()] = '*'
            }
            for (j in buffer) {
                if (j.joinToString("").contains("********")) {
                    println(i)
                    return
                }
            }
            startingMap.map { it.step(i, maxX, maxY) }.forEach {
                buffer[it.y.toInt()][it.x.toInt()] = ' '
            }
        }
    }

    @Suppress("unused")
    private fun part2Graphical(input: List<String>) {
        if (input.size < 50) return
        val (maxX, maxY) = Pair(101, 103)
        val startingMap = preProc(input, maxX, maxY)

        val window = JFrame()
        window.size = Dimension(800, 600)
        window.isVisible = true
        window.setLocationRelativeTo(null)
        val bmp = Bitmap(maxX, maxY)
        val buttonYes = JButton("Christmas")
        val buttonNo = JButton("Not Christmas")
        window.layout = GridBagLayout()
        val const = GridBagConstraints().apply {
            fill = GridBagConstraints.BOTH
            weightx = 1.0
            weighty = 1.0
        }
        window.add(bmp, const.apply { gridx = 0 })
        window.add(buttonYes, const.apply { gridx = 2 })
        window.add(buttonNo, const.apply { gridx = 3 })

        buttonNo.addActionListener {
            checked = true
        }
        buttonYes.addActionListener {
            checked = true
            done = true
        }

        var worthChecking = false

        val doubleBuffer = MutableList(maxY) { MutableList(maxX) { ' ' } }
        for (i in 0..100000) {
            val pointsToDraw = startingMap.map { it.step(i, maxX, maxY) }
            bmp.step++

            pointsToDraw.forEach { doubleBuffer[it.y.toInt()][it.x.toInt()] = '*' }
            for (x in 0..<maxX) for (y in 0..<maxY) bmp.content[y][x] = doubleBuffer[y][x]
            for (j in doubleBuffer) {
                if (j.joinToString("").contains("********")) {
                    worthChecking = true
                    break
                }
            }
            if (worthChecking) {
                window.repaint()
                while (!checked) {/**/
                }
                if (done) {
                    println(i)
                    window.dispose()
                    return
                }
                checked = false
                worthChecking = false
            }
            pointsToDraw.forEach { doubleBuffer[it.y.toInt()][it.x.toInt()] = ' ' }
            for (x in 0..<maxX) for (y in 0..<maxY) bmp.content[y][x] = doubleBuffer[y][x]
        }

        window.dispose()
    }
}