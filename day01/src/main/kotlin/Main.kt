fun main() {
    val input = generateSequence(::readLine).toList()

    val data = input
        .flatMapIndexed { index, x ->
            when {
                index == 0 || index == input.lastIndex -> listOf(index)
                x.isEmpty() -> listOf(index - 1, index + 1)
                else -> emptyList()
            }
        }
        .windowed(size = 2, step = 2) { (from, to) -> input.slice(from..to).map{it.toInt()} }

    val part1 = data.maxOf{it.sum()}
    println(part1)

    val part2 = data.map{it.sum()}.sortedDescending().take(3).sum()
    println(part2)
}