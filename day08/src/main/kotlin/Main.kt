fun main() {
    val input = generateSequence(::readLine).toList()
    val n = input.size

    val trees = sequence {
        input.forEachIndexed { i, line ->
            line.forEachIndexed { j, c ->
                yield(Pair(Pair(i, j), c.digitToInt()))
            }
        }
    }.toMap()

    fun isVisible(point: Pair<Int, Int>): Boolean {
        val (i, j) = point

        val left = (0 until j).map{ a -> trees.getOrDefault(Pair(i, a), -1)}.max()
        val right = (j+1 until n).map{ a -> trees.getOrDefault(Pair(i, a), -1)}.max()
        val top = (0 until i).map{a -> trees.getOrDefault(Pair(a, j), -1)}.max()
        val bottom = (i+1 until n).map{a -> trees.getOrDefault(Pair(a, j), -1)}.max()

        val height = trees.getOrDefault(Pair(i, j), -1)

        return height > left || height > right || height > top || height > bottom
    }

    val part1 = (n * 4 - 4) + (1 until n-1).flatMap {i -> (1 until n-1).map { j -> Pair(i, j)}}.filter(::isVisible).size
    println(part1)
}