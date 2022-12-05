fun main() {
    val input = generateSequence(::readLine)
        .map{it.split(",", "-").map{a -> a.toInt()}}
        .toList()

    val part1 = input
        .filter{(a, b, c, d) -> (c >= a && d <= b) || (a >= c && b <= d)}
        .size
    println(part1)

    val part2 = input
        .filter{(a, b, c, d) -> (a in c..d || b in c..d) || (c in a..b || d in a..b)}
        .size
    println(part2)
}