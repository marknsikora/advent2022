data class Monkey(
    val id: Int,
    val items: MutableList<Long>,
    val op: Operation,
    val div: Long,
    val yes: Int,
    val no: Int,
)

data class Operation(val op: String, val value: String) {
    operator fun invoke(old: Long): Long {
        val right = when (value) {
            "old" -> old
            else -> value.toLong()
        }

        return when (op) {
            "*" -> old * right
            "+" -> old + right
            else -> throw IllegalArgumentException()
        }
    }
}

fun String.toMonkey(): Monkey {
    val lineRegex =
        """Monkey (\d+):\n  Starting items: (\d+(, \d+)*)\n  Operation: new = old ([+*]) (\d+|old)\n  Test: divisible by (\d+)\n    If true: throw to monkey (\d+)\n    If false: throw to monkey (\d+)""".trimIndent()
            .toRegex()

    val (id, items, _, op, v, div, yes, no) = lineRegex
        .matchEntire(this)
        ?.destructured
        ?: throw IllegalArgumentException()

    return Monkey(
        id.toInt(),
        items.split(", ").map(String::toLong).toMutableList(),
        Operation(op, v),
        div.toLong(),
        yes.toInt(),
        no.toInt(),
    )
}

fun play(monkeys: List<Monkey>, n: Int, reduce: (Long) -> Long): Long {
    val activity = MutableList(monkeys.size) { 0 }

    repeat(n) {
        for (monkey in monkeys) {
            activity[monkey.id] += monkey.items.size
            val (div, nonDiv) = monkey.items.map { reduce(monkey.op(it)) }.partition { it % monkey.div == 0L }
            monkeys[monkey.yes].items.addAll(div)
            monkeys[monkey.no].items.addAll(nonDiv)
            monkey.items.clear()
        }
    }

    activity.sortDescending()
    val (a, b) = activity
    return a.toLong() * b.toLong()
}

fun main() {
    val input = generateSequence(::readLine)
        .joinToString("\n")
        .split("\n\n")
        .map(String::toMonkey)
        .toList()


    val part1 = play(input.map { it.copy(items = it.items.toMutableList()) }, 20) { i -> i / 3 }
    println(part1)

    val maxMod = input.map { it.div }.reduce { acc, i -> acc * i }
    val part2 = play(input.map { it.copy(items = it.items.toMutableList()) }, 10000) { i -> i % maxMod }
    println(part2)
}