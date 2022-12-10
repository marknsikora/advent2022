sealed class Instruction()

object Noop : Instruction() {
    override fun toString(): String = "noop"
}

class Addx(val n: Int) : Instruction() {
    override fun toString(): String = "addx $n"
}

fun String.toInstruction(): Instruction {
    val words = split(" ")
    return when (words.first()) {
        "noop" -> Noop
        "addx" -> Addx(words.last().toInt())
        else -> throw IllegalArgumentException()
    }
}

fun main() {
    val input = generateSequence(::readLine).map(String::toInstruction).toList()

    var x = 1
    var cycle = 0
    var power = 0

    val screen = mutableSetOf<Pair<Int, Int>>()

    for (inst in input) {
        val count = when (inst) {
            is Addx -> 2
            Noop -> 1
        }

        repeat(count) {
            val column = cycle % 40

            if (column in x - 1..x + 1) {
                val row = cycle / 40
                screen.add(Pair(row, column))
            }

            cycle++

            if ((cycle + 20) % 40 == 0) {
                power += cycle * x
            }
        }

        when (inst) {
            is Addx -> x += inst.n
            Noop -> {}
        }
    }

    println(power)

    val rows = screen.maxOf { i -> i.first }
    val output = (0..rows).joinToString("\n") { i ->
        (0..39).joinToString("") { j -> if (Pair(i, j) in screen) "#" else " " }
    }
    println(output)
}