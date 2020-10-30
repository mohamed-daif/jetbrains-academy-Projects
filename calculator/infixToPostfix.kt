package calculator

import java.util.*

/**
 * convert Infix Expression to Postfix Expression
 * @return postfix MutableList<Any>
 */
fun infixToPostfix(infix: MutableList<String>): MutableList<Any> {
    // create lists and stacks for operation
    val tempList = mutableListOf<Any>()
    val postfix = Stack<Any>()
    val operatorStack = Stack<Any>()
    // convert Numbers from list to BigInteger
    for (element in infix) {
        if (element in operators.toString()) tempList.add(element)
        else tempList.add(element.toBigInteger())
    }
    // loop for converting from tempList to postfix stack
    for (element in tempList) {
        if (element is Number) { // if element is Number push it to postfix stack
            postfix.push(element)
        } else if (element == "(") { // if element is "(" push it to operator stack
            operatorStack.push(element)
        } else if (element == "^") { // if element is ^ push it to operator stack
            operatorStack.push(element)
        } else if (element == ")") {
            // if element is ")" pop operators to postfix until find "(" then pop it from operatorStack
            while (operatorStack.isNotEmpty() &&
                operatorStack.peek() != "("
            ) {
                postfix.push(operatorStack.pop())
            }
            operatorStack.pop()
        } else {
            if (operatorStack.isNotEmpty() &&
                precede(element) > precede(operatorStack.peek())
            ) { // if operator preceding is higher than the top of operatorStack
                // push it to operator stack
                operatorStack.push(element)
            } else {
                // if operator preceding is lower than the top of operatorStack
                // pop all higher operators to postfix stack then pop it to operatorStack
                while (operatorStack.isNotEmpty() &&
                    precede(element) <= precede(operatorStack.peek())
                ) {
                    postfix.push(operatorStack.pop())
                }
                operatorStack.push(element)
            }
        }
    }
    // after reach the end of tempList push all operators in operatorStack to postfix
    while (operatorStack.isNotEmpty()) {
        postfix.push(operatorStack.pop())
    }
    return postfix
}

/**
 * privet function to check preceding of operators
 * @return Int
 */
private fun precede(operator: Any): Int {
    return when (operator) {
        "+", "-" -> 1       //Precedence of + or - is 1
        "*", "/" -> 2       //Precedence of * or / is 2
        "^" -> 3            //Precedence of ^ is 3
        else -> 0
    }
}