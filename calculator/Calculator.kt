package calculator

import java.math.BigInteger
import java.util.*

class Calculator {
    private val operators = listOf("+", "-", "*", "/", "^", "(", ")")

    fun calc(postfix: MutableList<Any>): Any {
        val stack = Stack<Any>()
        var result: BigInteger
        for (element in postfix) {
            if (element in operators) {
                result = operation(element as String, stack.pop() as BigInteger, stack.pop() as BigInteger)
                stack.push(result)
            } else {
                stack.push(element)
            }
        }
        return stack.pop()
    }

    private fun operation(operator: String, b: BigInteger, a: BigInteger): BigInteger {
        var result = BigInteger("0")
        when (operator) {
            "+" -> result = a.plus(b)
            "-" -> result = a.subtract(b)
            "*" -> result = a.multiply(b)
            "/" -> result = a.divide(b)
            "^" -> result = a.pow(b.toInt())
        }
        return result
    }
}