fun play1(a : String, b : String) : Int {
    val aVal = a.single() - 'A'
    val bVal = b.single() - 'X'

    val score = when (aVal) {
        bVal               -> 3
        (bVal + 1)     % 3 -> 0
        (bVal + 3 - 1) % 3 -> 6
        else -> throw IllegalArgumentException()
    }

    return score + bVal + 1;
}

fun play2(a : String, b : String) : Int {
    val aVal = a.single() - 'A'
    val bVal = b.single() - 'X'

    val hand = when (bVal) {
        0 -> (aVal + 3 - 1) % 3
        1 -> aVal
        2 -> (aVal + 1) % 3
        else -> throw IllegalArgumentException()
    }

    return bVal * 3 + hand + 1;
}

fun main() {
    val input = generateSequence(::readLine).map{it.split(" ")}.toList()

    val part1 = input.sumOf{(a, b) -> play1(a, b)}
    println(part1)

    val part2 = input.sumOf{(a, b) -> play2(a, b)}
    println(part2)
}