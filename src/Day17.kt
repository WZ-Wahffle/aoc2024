import kotlin.math.pow

@Suppress("unused")
class Day17 : Day {
    private data class Cpu(var a: Long, var b: Long, var c: Long, var pc: Long, val rom: List<Int>) {

        val out = StringBuilder()
        fun execute(): String {
            out.clear()
            while (rom.indices.contains(pc)) {
                executeInstruction()
            }

            return out.removeSuffix(",").toString()
        }

        private fun executeInstruction() {
            val opcode = getNextCell()
            val operand = getNextCell()
            when (opcode) {
                0L -> {
                    // println("adv ${resolveComboOperandDebug(operand)}")
                    adv(resolveComboOperand(operand))
                }

                1L -> {
                    // println("bxl $operand")
                    bxl(operand)
                }

                2L -> {
                    // println("bst ${resolveComboOperandDebug(operand)}")
                    bst(resolveComboOperand(operand))
                }

                3L -> {
                    // println("jnz $operand")
                    jnz(operand)
                }

                4L -> {
                    // println("bxc $operand")
                    bxc(operand)
                }

                5L -> {
                    // println("out ${resolveComboOperandDebug(operand)}")
                    out(resolveComboOperand(operand))
                }

                6L -> {
                    // println("bdv ${resolveComboOperandDebug(operand)}")
                    bdv(resolveComboOperand(operand))
                }

                7L -> {
                    // println("cdv ${resolveComboOperandDebug(operand)}")
                    cdv(resolveComboOperand(operand))
                }

                else -> throw RuntimeException("unreachable")
            }
        }

        private fun adv(op: Long) {
            a /= 2.0.pow(op.toDouble()).toInt()
        }

        private fun bxl(op: Long) {
            b = b xor op
        }

        private fun bst(op: Long) {
            b = op % 8L
        }

        private fun jnz(op: Long) {
            if (a != 0L) {
                pc = op
            }
        }

        private fun bxc(@Suppress("UNUSED_PARAMETER") op: Long) {
            b = b xor c
        }

        private fun out(op: Long) {
            out.append("${op % 8},")
        }

        private fun bdv(op: Long) {
            b = a / 2.0.pow(op.toDouble()).toInt()
        }

        private fun cdv(op: Long) {
            c = a / 2.0.pow(op.toDouble()).toInt()
        }

        private fun resolveComboOperandDebug(op: Long): String {
            return when (op) {
                in 0..3 -> "$op"
                4L -> "A"
                5L -> "B"
                6L -> "C"
                else -> throw RuntimeException("unreachable")
            }
        }

        private fun resolveComboOperand(op: Long): Long {
            return when (op) {
                in 0..3 -> op
                4L -> a
                5L -> b
                6L -> c
                else -> throw RuntimeException("unreachable")
            }
        }

        private fun getNextCell(): Long {
            return rom[pc++.toInt()].toLong()
        }
    }

    private fun preProc(input: List<String>): Cpu {
        return Cpu(
            input[0].split(" ").last().toLong(),
            input[1].split(" ").last().toLong(),
            input[2].split(" ").last().toLong(),
            0,
            input[4].split(" ").last().split(",").map { it.toInt() }
        )
    }

    override fun part1(input: List<String>) {
        val cpu = preProc(input)
        println(cpu.execute())
    }

    private fun solveDigit(cur: Long, octalDigitToSolve: Int, cmp: String, cpu: Cpu): Long {
        if (octalDigitToSolve < 0) return if (run(cur, cpu) == cmp) cur else Long.MAX_VALUE
        return (0L..7L).minOf {
            val toCheck = cur or (it shl (octalDigitToSolve * 3))
            val result = run(toCheck, cpu)
            if (result.length > cmp.length) return Long.MAX_VALUE
            if (compareOctals(result, cmp, octalDigitToSolve)) {
                solveDigit(toCheck, octalDigitToSolve - 1, cmp, cpu)
            } else Long.MAX_VALUE
        }
    }

    private fun compareOctals(l1: String, l2: String, idx: Int): Boolean {
        return l1.padStart(l2.length, ' ')[idx] == l2[idx]
    }

    private fun run(a: Long, cpu: Cpu): String {
        cpu.a = a
        cpu.b = 0
        cpu.c = 0
        cpu.pc = 0
        return cpu.execute().replace(",", "")
    }

    override fun part2(input: List<String>) {
        val expectedOutput = input.last().split(" ").last().split(",").map { it.toInt() }
        val cpu = preProc(input)

        println(solveDigit(0, expectedOutput.size - 1, expectedOutput.joinToString(""), cpu))
    }
}