sealed class Packet() : Comparable<Packet>

class PacketInt(val n: Int) : Packet() {
    override fun compareTo(other: Packet): Int {
        return when (other) {
            is PacketInt -> n.compareTo(other.n)
            is PacketList -> PacketList(listOf(this)).compareTo(other)
        }
    }

    override fun toString(): String {
        return n.toString()
    }
}

class PacketList(val l: List<Packet>) : Packet() {
    override fun compareTo(other: Packet): Int {
        return when (other) {
            is PacketInt -> l.compareTo(listOf(other))
            is PacketList -> l.compareTo(other.l)
        }
    }

    override fun toString(): String {
        return l.toString()
    }
}

fun List<Packet>.compareTo(other: List<Packet>): Int {
    for ((a, b) in zip(other)) {
        val cmp = a.compareTo(b)

        if (cmp != 0) {
            return cmp
        }
    }

    if (size < other.size) {
        return -1
    }

    if (size > other.size) {
        return 1
    }

    return 0
}

fun String.tokenize(): List<String> {
    val buf = mutableListOf<Char>()
    val tokens = mutableListOf<String>()

    for (window in toList().windowed(2, partialWindows = true)) {
        when (val next = window.first()) {
            '[', ']', ',' -> {
                if (buf.isNotEmpty()) {
                    tokens.add(buf.joinToString(""))
                    buf.clear()
                }

                tokens.add(next.toString())
            }

            else -> buf.add(next)
        }
    }

    if (buf.isNotEmpty()) {
        tokens.add(buf.toString())
        buf.clear()
    }

    return tokens
}

fun List<String>.toPacket(): Packet {
    fun rec(it: Iterator<String>): Packet? {
        while (it.hasNext()) {
            return when (val next = it.next()) {
                "," -> {
                    continue
                }

                "[" -> {
                    PacketList(generateSequence { rec(it) }.toList())
                }

                "]" -> {
                    null
                }

                else -> {
                    PacketInt(next.toInt())
                }
            }
        }

        return null
    }

    return rec(iterator())!!
}

fun main() {
    val input = generateSequence(::readLine)
        .windowed(2, 3)
        .map {
            val (left, right) = it
            Pair(
                left.tokenize().toPacket(),
                right.tokenize().toPacket(),
            )
        }
        .toList()

    val part1 = input.mapIndexed { i, (a, b) -> if (a < b) i + 1 else 0 }.sum()
    println(part1)

    val sorted = input.flatMap { (a, b) -> listOf(a, b) }.sorted()

    val startPacket = "[[2]]".tokenize().toPacket()!!
    val start = 1 + sorted.indexOfFirst { a -> a > startPacket }

    val endPacket = "[[6]]".tokenize().toPacket()!!
    val end = 2 + sorted.indexOfFirst { a -> a > endPacket }

    val part2 = start * end
    println(part2)
}