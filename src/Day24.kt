private typealias Gate = Triple<String, String, String>

@Suppress("unused")
class Day24 : Day {

    private data class LogicGate(val op1: String, val op: String, val op2: String, var out: String) {
        override fun toString(): String {
            return "$op1 $op $op2 -> $out"
        }

    }

    private fun parseEquationLine(ret: MutableMap<String, Boolean>, str: String) {
        val (operand1, operation, operand2, _, target) = str.split(' ')
        when (operation) {
            "AND" -> ret[target] = ret[operand1]!! && ret[operand2]!!
            "OR" -> ret[target] = ret[operand1]!! || ret[operand2]!!
            "XOR" -> ret[target] = ret[operand1]!! xor ret[operand2]!!
        }
    }

    private fun preProc(input: List<String>): Map<String, Boolean> {
        val ret = mutableMapOf<String, Boolean>()
        input.forEach {
            if (it.contains(':')) {
                ret[it.split(':').first()] = it.split(' ')[1] == "1"
            } else {
                return@forEach
            }
        }

        val secondHalf = input.drop(ret.size + 1).toMutableList()

        while (secondHalf.isNotEmpty()) {
            val line = secondHalf.find {
                ret.keys.contains(
                    it.split(' ')[0]
                ) &&
                        ret.keys.contains(
                            it.split(' ')[2]
                        )
            }!!
            secondHalf.remove(line)
            parseEquationLine(
                ret,
                line
            )
        }

        return ret
    }

    override fun part1(input: List<String>) {
        val map = preProc(input)
        println(map.filter { it.key.contains('z') }.filter { it.value }.keys.fold(0L) { acc, next ->
            acc or (1L shl next.drop(1).toInt())
        })
    }

    private fun extractGates(input: List<String>): MutableMap<Gate, String> {
        val map = mutableMapOf<Gate, String>()
        input.forEach {
            if (!it.contains(':') && it.isNotEmpty()) {
                val (operand1, operation, operand2, _, target) = it.split(' ')
                map[Triple(first(operand1, operand2), operation, second(operand1, operand2))] = target
            }
        }

        return map
    }

    private fun first(s1: String, s2: String): String = if (s1 < s2) s1 else s2
    private fun second(s1: String, s2: String): String = if (s1 > s2) s1 else s2

    private fun List<LogicGate>.outToIn(out: String): LogicGate? {
        return this.find { it.op1 == out || it.op2 == out }
    }

    private fun List<LogicGate>.outToInWithGate(out: String, gates: List<String>): LogicGate? {
        return this.find { (it.op1 == out || it.op2 == out) && gates.contains(it.op) }
    }

    // observation: circuit implemented is a 45-bit adder with carry
    // x00-x44
    // y00-y44
    // z00-z45
    override fun part2(input: List<String>) {
        val map =
            extractGates(input).map { LogicGate(it.key.first, it.key.second, it.key.third, it.value) }.toMutableList()
        if (input.size < 30) return

        val problems = mutableListOf<LogicGate>()

        problems += map.filter { it.op != "XOR" && it.out.contains('z') && it.out != "z45" }
        problems += map.filter { it.op == "XOR" && !(it.op1.contains("x") || it.op1.contains("y") || it.out.contains("z")) }
        problems += map.filter { map.outToInWithGate("x00", listOf("AND"))!! != it && it.op == "AND" && map.outToInWithGate(it.out, listOf("AND", "XOR")) != null && map.outToIn(it.out)!!.op != "OR" }
        problems += map.filter { it.op == "OR" && map.outToInWithGate(it.out, listOf("OR")) != null && map.outToIn(it.out)!!.op == "OR" }
        problems += map.filter { it.op == "XOR" && map.outToInWithGate(it.out, listOf("OR")) != null && map.outToIn(it.out)!!.op == "OR" }
        println(problems.toSet().toList().sortedBy { it.out }.joinToString(",") { it.out })

        /*
var previousCarryOut = map[Triple("x00", "AND", "y00")]
val problems = mutableListOf<Pair<Triple<String, String, String>, String?>>()
for (i in 1..44) {
    // in1: xi
    // in2: yi
    // out1: zi

    val resultOperands = map.filter { it.value == "z${i.toString().padStart(2, '0')}" }.keys.first()
    if (resultOperands.second != "XOR") {
        println("Output at $i broken")
        problems += Pair(resultOperands, map[resultOperands])
    }
    val firstResult =
        map[Triple("x${i.toString().padStart(2, '0')}", "XOR", "y${i.toString().padStart(2, '0')}")]!!
    if (resultOperands.first != firstResult && resultOperands.third != firstResult) {
        println("Intermediate at $i broken")
        problems += Pair(map.filter { it.value == firstResult }.keys.first(), firstResult)
    }


    val carryIn = if (resultOperands.first == firstResult) resultOperands.third else resultOperands.first

    if(carryIn != previousCarryOut) {
        println("CarryOut of ${i - 1} broken")
        problems += Pair(map.filter { it.value == previousCarryOut }.keys.first(), previousCarryOut)
    }

    val carryOutOperator1 = map[Triple(first(carryIn, firstResult), "AND", second(carryIn, firstResult))]!!

    val carryOutOperator2 =
        map[Triple("x${i.toString().padStart(2, '0')}", "AND", "y${i.toString().padStart(2, '0')}")]!!
    val carryOut = map[Triple(first(carryOutOperator1, carryOutOperator2), "OR", second(carryOutOperator1, carryOutOperator2))]

    previousCarryOut = carryOut
    println("$i carryIn: $carryIn carryOut: $carryOut")
}
println(problems.toSet().toList())
*/
    }
}