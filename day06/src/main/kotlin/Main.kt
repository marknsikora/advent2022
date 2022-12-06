fun main() {
    val input = generateSequence(::readLine).toList()

    for (line in input) {
        val part1 = 4 + line
            .windowed(4, 1)
            .indexOfFirst { it.toSet().size == it.length }
        println(part1)
    }

    for (line in input) {
        val part2 = 14 + line
            .windowed(14, 1)
            .indexOfFirst { it.toSet().size == it.length }
        println(part2)
    }
}