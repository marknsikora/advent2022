import kotlin.math.max
import kotlin.math.min
import kotlin.math.sign

typealias Point = Pair<Int, Int>

fun String.toPoint(): Point {
    val (a, b) = split(",")
    return Point(a.toInt(), b.toInt())
}

fun String.toPath(): List<Point> {
    return split(" -> ").map(String::toPoint)
}

fun Point.pathTo(other: Point): Sequence<Point> {
    var point = this
    return sequence {
        yield(point)

        while (point != other) {
            val dx = other.first - point.first
            val dy = other.second - point.second

            point = Point(point.first + dx.sign, point.second + dy.sign)
            yield(point)
        }
    }
}

fun Point.fall() : List<Point> {
    return listOf(
        Point(first, second + 1),
        Point(first - 1, second + 1),
        Point(first + 1, second + 1),
    )
}


fun main() {
    val input = generateSequence(::readLine).map(String::toPath).toList()

    val rock = input.flatMap { it.zipWithNext().flatMap { (a, b) -> a.pathTo(b) } }.toSet()

    val source = Point(500, 0)

    val upperBound = rock.fold(source) { acc, p -> Point(max(acc.first, p.first), max(acc.second, p.second)) }
    val lowerBound = rock.fold(source) { acc, p -> Point(min(acc.first, p.first), min(acc.second, p.second)) }

    val blocking = rock.toMutableSet()

    val floor = upperBound.second + 2

    loop@ while (true) {
        var point = source
        while (true) {
            point = point.fall().find { it !in blocking } ?: break

            if (point.second > upperBound.second) {
                break@loop
            }
        }
        blocking.add(point)
    }

    val part1 = blocking.size - rock.size
    println(part1)

    blocking.clear()
    blocking.addAll(rock)
    while (source !in blocking) {
        var point = source
        while (true) {
            point = point.fall().find { it !in blocking && it.second != floor } ?: break
        }
        blocking.add(point)
    }

    val part2 = blocking.size - rock.size
    println(part2)
}