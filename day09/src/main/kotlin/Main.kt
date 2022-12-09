import kotlin.math.sign

enum class Direction { RIGHT, LEFT, UP, DOWN, }
data class Step(val direction: Direction, val units: Int)

fun String.toStep(): Step {
    val (a, b) = split(" ")
    return Step(
        when (a) {
            "R" -> Direction.RIGHT
            "L" -> Direction.LEFT
            "U" -> Direction.UP
            "D" -> Direction.DOWN
            else -> throw IllegalArgumentException()
        },
        b.toInt(),
    )
}

fun Pair<Int, Int>.move(dir: Direction): Pair<Int, Int> =
    when (dir) {
        Direction.RIGHT -> Pair(first + 1, second)
        Direction.LEFT -> Pair(first - 1, second)
        Direction.UP -> Pair(first, second + 1)
        Direction.DOWN -> Pair(first, second - 1)
    }

fun Pair<Int, Int>.isNeighbour(other: Pair<Int, Int>): Boolean {
    val dx = first - other.first
    val dy = second - other.second

    return dx in -1..1 && dy in -1..1
}

fun main() {
    val input = generateSequence(::readLine).map(String::toStep).toList()

    var head = Pair(0, 0)
    var tails = List(9) { Pair(0, 0) }

    val positionsPart1 = mutableSetOf(tails.first())
    val positionsPart2 = mutableSetOf(tails.last())

    for (step in input) {
        repeat(step.units) {
            head = head.move(step.direction)

            tails = tails
                .runningFold(head) { acc, i ->
                    if (acc.isNeighbour(i)) {
                        i
                    } else {
                        val dx = acc.first - i.first
                        val dy = acc.second - i.second

                        Pair(i.first + dx.sign, i.second + dy.sign)
                    }
                }
                .drop(1)

            positionsPart1.add(tails.first())
            positionsPart2.add(tails.last())
        }
    }

    val part1 = positionsPart1.size
    println(part1)

    val part2 = positionsPart2.size
    println(part2)
}