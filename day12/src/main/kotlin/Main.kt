import kotlin.math.abs

typealias Point = Pair<Int, Int>

fun Point.getNeighbours(): List<Point> {
    return listOf(
        Point(first + 1, second),
        Point(first - 1, second),
        Point(first, second + 1),
        Point(first, second - 1),
    )
}

fun Point.distance(other: Point): Int {
    return abs(first - other.first) + abs(second - other.second)
}

fun height(c: Char): Int {
    if (c == 'S') {
        return height('a')
    }

    if (c == 'E') {
        return height('z')
    }

    if (c in 'a'..'z') {
        return c - 'a'
    }

    throw IllegalArgumentException()
}

fun reachable(a: Char, b: Char): Boolean {
    if (height(b) - height(a) <= 1) {
        return true
    }

    return false
}

fun aStar(map: Map<Point, Char>, starts: Set<Point>, end: Point): Int {
    val open = starts.toMutableSet()

    val gScore = starts.associateWith { 0 }.toMutableMap()
    val fScore = starts.associateWith { it.distance(end) }.toMutableMap()

    while (open.isNotEmpty()) {
        val current = open.minBy { a -> fScore.getOrDefault(a, 0) }
        open.remove(current)

        if (current == end) {
            break
        }

        for (neighbour in current.getNeighbours()) {
            if (neighbour !in map) {
                continue
            }

            if (!reachable(map[current]!!, map[neighbour]!!)) {
                continue
            }

            val tentative = gScore.getOrDefault(current, 0) + 1
            if (neighbour !in gScore || tentative < gScore[neighbour]!!) {
                gScore[neighbour] = tentative
                fScore[neighbour] = tentative + neighbour.distance(end)
                open.add(neighbour)
            }
        }
    }

    return gScore[end]!!
}

fun main() {
    val input = generateSequence(::readLine).toList()

    val map = input.flatMapIndexed { i, line -> line.mapIndexed { j, c -> Pair(Point(i, j), c) } }.toMap()

    val start = map.filterValues { it == 'S' }.keys.first()
    val end = map.filterValues { it == 'E' }.keys.first()

    val part1 = aStar(map, setOf(start), end)
    println(part1)

    val allStarts = map.filterValues { height(it) == 0 }.keys.toSet()
    val part2 = aStar(map, allStarts, end)
    println(part2)
}