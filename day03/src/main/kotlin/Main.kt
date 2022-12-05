fun priority(a : Char) : Int {
    return 1 + when (a) {
        in 'a'..'z' -> a - 'a'
        in 'A'..'Z' -> a - 'A' + 26
        else -> throw IllegalArgumentException()
    }
}

fun main() {
    val input = generateSequence(::readLine).toList()

    val part1 = input
        .map{it.chunked(it.length/2)}
        .sumOf{(a, b) -> priority(a.toSet().intersect(b.toSet()).first())}
    println(part1)

    val part2 = input
        .chunked(3)
        .sumOf{priority(it.map{a -> a.toSet()}.reduce{acc, next -> acc.intersect(next)}.first())}
    println(part2)
}