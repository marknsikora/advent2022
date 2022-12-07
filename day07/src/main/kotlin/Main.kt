class Node(val parent: Node?) {
    val files = mutableMapOf<String, Int>()
    val nodes = mutableMapOf<String, Node>()

    override fun toString(): String {
        return "Node(files: $files, nodes: $nodes)"
    }

    fun getSizes(): List<Int> {
        val sizes = nodes.values.map(Node::getSizes)
        val total = files.values.sum() + sizes.sumOf { it.first() }

        return listOf(total).plus(sizes.flatten())
    }
}

fun main() {
    val input = generateSequence(::readLine).toList()

    var rootNode: Node = Node(null)
    var currentNode: Node? = null;
    for (line in input) {
        val words = line.split(" ")
        when {
            line.startsWith("$ cd /") -> currentNode = rootNode
            line.startsWith("$ cd ..") -> currentNode = currentNode?.parent
            line.startsWith("$ cd") -> {
                val name = words.last()
                currentNode = currentNode?.nodes?.getOrDefault(name, null)
            }

            line.startsWith("$ ls") -> null
            else -> {
                val (size, name) = words

                if (size == "dir") {
                    currentNode?.nodes?.set(name, Node(currentNode))
                } else {
                    currentNode?.files?.set(name, size.toInt())
                }
            }
        }
    }

    val sizes = rootNode.getSizes()

    val part1 = sizes.filter { i -> i < 100_000 }.sum()
    println(part1)

    val needed = 30_000_000 - (70_000_000 - sizes.first())
    val part2 = sizes.filter { i -> i >= needed }.min()
    println(part2)
}