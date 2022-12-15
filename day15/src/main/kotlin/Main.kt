import kotlin.math.abs

typealias Point = Pair<Int, Int>

fun Point.distance(other: Point): Int {
    return abs(other.first - first) + abs(other.second - second)
}

data class Sensor(val position: Point, val nearestBeacon: Point) {
    private val maxDist = position.distance(nearestBeacon)

    fun rowCoverage(row: Int): List<Point> {
        val width = maxDist - abs(position.second - row)

        return (-width..width).map { i -> Point(position.first + i, row) }
    }

    fun perimeter(): List<Point> {
        val (x, y) = position
        val edge = maxDist + 1
        return (0 until edge).flatMap { i ->
            listOf(
                Point(x - edge + i, y + i),
                Point(x + i, y + edge - i),
                Point(x + edge - i, y - i),
                Point(x - i, y - edge + i),
            )
        }
    }

    operator fun contains(other: Point): Boolean {
        val dist = position.distance(other)
        return dist <= maxDist
    }
}

fun String.toSensor(): Sensor {
    val lineRegex = """Sensor at x=(-?\d+), y=(-?\d+): closest beacon is at x=(-?\d+), y=(-?\d+)""".toRegex()

    val (posX, posY, beaconX, beaconY) = lineRegex
        .matchEntire(this)
        ?.destructured
        ?: throw IllegalArgumentException()

    return Sensor(Point(posX.toInt(), posY.toInt()), Point(beaconX.toInt(), beaconY.toInt()))
}

fun main(args: Array<String>) {
    val row = args.first().toInt()
    val input = generateSequence(::readLine).map(String::toSensor).toList()

    val coverage = input.flatMap { it.rowCoverage(row) }.toSet()
    val beacons = input.map { it.nearestBeacon }.toSet()

    val part1 = coverage.subtract(beacons).size
    println(part1)

    val distress = input
        .asSequence()
        .flatMap(Sensor::perimeter)
        .filter { it.first in 0..row * 2 && it.second in 0..row * 2 }
        .first { p -> input.all { s -> p !in s } }

    val part2 = 4000000.toBigInteger() * distress.first.toBigInteger() + distress.second.toBigInteger()
    println("$distress $part2")
}