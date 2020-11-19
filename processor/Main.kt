package processor

import java.util.*
import kotlin.system.exitProcess

val scanner = Scanner(System.`in`)
var mSize = 0

fun main() {
    do {
        menu()
        when (choice()) {
            1 -> addMatrices()
            2 -> matrixBuConst()
            3 -> matrixByMatrix()
            4 -> transposeMatrix()
            5 -> calcDeterminant()
            6 -> inverseMatrix()
            0 -> exit()
        }
        println()
    } while (true)
}

fun getCofactor(matrix: Array<Array<Double>>, temp: Array<Array<Double>>, p: Int, q: Int, n: Int) {
    var i = 0
    var j = 0
    for (row in 0 until n) {
        for (col in 0 until n) {
            if (row != p && col != q) {
                temp[i][j++] = matrix[row][col]
                if (j == n - 1) {
                    j = 0
                    i++
                }
            }
        }
    }
}

fun determinant(matrix: Array<Array<Double>>, n: Int): Double {
    var det = 0.0
    if (n == 1) return matrix[0][0]
    val temp = Array(mSize) { Array(mSize) { 0.0 } }
    var sign = 1
    for (f in 0 until n) {
        getCofactor(matrix, temp, 0, f, n)
        det += (sign * matrix[0][f] * determinant(temp, n - 1))
        sign = -sign
    }
    return det
}

fun adJoint(matrix: Array<Array<Double>>, adJoint: Array<Array<Double>>) {
    if (mSize == 1) {
        adJoint[0][0] = 1.0
        return
    }
    var sign: Int
    val temp = Array(mSize) { Array(mSize) { 0.0 } }
    for (i in 0 until mSize) {
        for (j in 0 until mSize) {
            getCofactor(matrix, temp, i, j, mSize)
            sign = if ((i + j) % 2 == 0) 1 else -1
            adJoint[j][i] = sign * determinant(temp, mSize - 1)
        }
    }
}

fun inverse(matrix: Array<Array<Double>>, inverse: Array<Array<Double>>): Boolean {
    val det = determinant(matrix, mSize)
    if (det == 0.0) {
        print("This matrix doesn't have an inverse.")
        return false
    }
    val adj = Array(mSize) { Array(mSize) { 0.0 } }
    adJoint(matrix, adj)
    for (i in 0 until mSize) for (j in 0 until mSize) inverse[i][j] = adj[i][j] / det
    return true
}

fun inverseMatrix() {
    val matrix = singleMatrix()
    mSize = matrix.size
    val adJoint = Array(mSize) { Array(mSize) { 0.0 } }
    adJoint(matrix, adJoint)
    if (inverse(matrix, adJoint)) printMatrix(adJoint)
}

fun calcDeterminant() {
    val matrix = singleMatrix()
    mSize = matrix.size
    println(determinant(matrix, matrix.size))
}

fun transposeMatrix() {
    println("""
        1. Main diagonal
        2. Side diagonal
        3. Vertical line
        4. Horizontal line""".trimIndent())

    fun mainDiagonal(matrix: Array<Array<Double>>) {
        val result = Array(matrix[0].size) { Array(matrix.size) { 0.0 } }
        for (i in matrix.indices) {
            for (j in matrix[0].indices) {
                result[i][j] = matrix[j][i]
            }
        }
        printMatrix(result)
    }

    fun sideDiagonal(matrix: Array<Array<Double>>) {
        val result = Array(matrix[0].size) { Array(matrix.size) { 0.0 } }
        for (i in matrix.indices) {
            for (j in matrix[0].indices) {
                result[matrix.size - i - 1][matrix[0].size - j - 1] = matrix[j][i]
            }
        }
        printMatrix(result)
    }

    fun verticalLine(matrix: Array<Array<Double>>) {
        val result = Array(matrix[0].size) { Array(matrix.size) { 0.0 } }
        for (i in matrix.indices) {
            for (j in matrix[0].indices) {
                result[i][matrix[0].size - j - 1] = matrix[i][j]
            }
        }
        printMatrix(result)
    }

    fun horizontalLine(matrix: Array<Array<Double>>) {
        val result = Array(matrix[0].size) { Array(matrix.size) { 0.0 } }
        for (i in matrix.indices) {
            for (j in matrix[0].indices) {
                result[matrix.size - i - 1][j] = matrix[i][j]
            }
        }
        printMatrix(result)
    }


    when (choice()) {
        1 -> mainDiagonal(singleMatrix())
        2 -> sideDiagonal(singleMatrix())
        3 -> verticalLine(singleMatrix())
        4 -> horizontalLine(singleMatrix())
    }
}

fun singleMatrix() =
    print("Enter matrix size: ").run { createMatrix(scanner.nextInt(), scanner.nextInt()) }

fun matrixByMatrix() {
    val first =
        print("Enter size of first matrix: ").run { createMatrix(scanner.nextInt(), scanner.nextInt(), " first") }
    val second =
        print("Enter size of second matrix: ").run { createMatrix(scanner.nextInt(), scanner.nextInt(), " second") }
    if (first.checkMatrix(second, '*')) printMatrix(first * second) else err()
}

fun matrixBuConst() {
    val first = print("Enter size of matrix: ").run { createMatrix(scanner.nextInt(), scanner.nextInt()) }
    val constant = print("Enter constant: ").run { scanner.nextInt() }
    printMatrix(first * constant)
}

fun addMatrices() {
    val first =
        print("Enter size of first matrix: ").run { createMatrix(scanner.nextInt(), scanner.nextInt(), " first") }
    val second =
        print("Enter size of second matrix: ").run { createMatrix(scanner.nextInt(), scanner.nextInt(), " second") }
    if (first.checkMatrix(second, '+')) printMatrix(first + second) else err()
}

private operator fun Array<Array<Double>>.plus(second: Array<Array<Double>>): Array<Array<Double>> {
    val result = Array(this.size) { Array(second[0].size) { 0.0 } }
    for (i in this.indices) {
        for (j in this[0].indices) {
            result[i][j] = this[i][j] + second[i][j]
        }
    }
    return result
}

private operator fun Array<Array<Double>>.times(second: Array<Array<Double>>): Array<Array<Double>> {
    val result = Array(this.size) { Array(second[0].size) { 0.0 } }
    for (i in this.indices) {
        for (j in second[0].indices) {
            for (k in second.indices) {
                result[i][j] += this[i][k] * second[k][j]
            }
        }
    }
    return result
}

private operator fun Array<Array<Double>>.times(constant: Int): Array<Array<Double>> {
    val result = Array(this.size) { Array(this[0].size) { 0.0 } }
    for (i in result.indices) {
        for (j in result[0].indices) {
            result[i][j] = constant * this[i][j]
        }
    }
    return result
}

fun menu() = println("""1. Add matrices
2. Multiply matrix by a constant
3. Multiply matrices
4. Transpose matrix
5. Calculate a determinant
6. Inverse matrix
0. Exit""")

fun choice() = print("Your choice: ").run { scanner.nextInt() }

fun createMatrix(n: Int, m: Int, order: String = ""): Array<Array<Double>> {
    println("Enter$order matrix:")
    return Array(n) { Array(m) { scanner.nextDouble() } }
}

fun Array<Array<Double>>.checkMatrix(second: Array<Array<Double>>, operation: Char): Boolean {
    return when (operation) {
        '+' -> (this.size == second.size && this[0].size == second[0].size)
        '*' -> (this[0].size == second.size)
        else -> false
    }
}

fun printMatrix(matrix: Array<Array<Double>>) {
    println("The result is:").run { matrix.forEach { println(it.joinToString(" ")) } }
}

fun err() = println("The operation cannot be performed.")

fun exit(): Nothing = exitProcess(0)