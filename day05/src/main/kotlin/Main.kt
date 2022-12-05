fun main() {
    val input = generateSequence(::readLine).toList()

    val blankLine = input.indexOf("")
    val initialStacks = input.slice(0 until blankLine)
    val directions = input
        .slice(blankLine+1 until input.size)
        .map{it.split("move ", " from ", " to ").drop(1).map(String::toInt)}

    val numStacks = initialStacks.last().split("   ").last().trim().toInt()
    val stacks = List(numStacks) { ArrayDeque<Char>() };

    for (line in initialStacks) {
        line.chunked(4).forEachIndexed{ i, stack ->
            val mark = stack[1]
            if (mark != ' ') {
                stacks[i].addFirst(mark)
            }
        }
    }

    for ((n, from, to) in directions) {
        repeat(n) {
            val crate = stacks[from - 1].removeLast()
            stacks[to - 1].addLast(crate)
        }
    }

    val part1 = stacks.map{it.last()}.joinToString(separator="");
    println(part1)

    // Reset Stacks
    for (stack in stacks) {
        stack.clear()
    }
    for (line in initialStacks) {
        line.chunked(4).forEachIndexed{ i, stack ->
            val mark = stack[1]
            if (mark != ' ')
                stacks[i].addFirst(mark)
        }
    }

    for ((n, from, to) in directions) {
        val move = List(n) { stacks[from - 1].removeLast() }
        stacks[to - 1].addAll(move.reversed())
    }

    val part2 = stacks.map{it.last()}.joinToString(separator="");
    println(part2)
}