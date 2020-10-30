package calculator

import kotlin.system.exitProcess

// Global variables
val calculator = Calculator()
val memo = Memo()
val operators = listOf('+', '-', '*', '/', '^', '(', ')')

// Power on Calculator
fun main() {
    startCalculator()
}

// loop for input and determine actions
fun startCalculator() {
    loop@ do {
        val userInput: String? = getUserInput()
        when {
            userInput.isNullOrBlank() -> continue@loop
            userInput == "/exit" -> {
                exit()
                break@loop
            }
            userInput.first() == '/' -> executeCommand(userInput)
            userInput.matches(Regex("^[a-zA=Z]+")) -> println(memo.getFromMemo(userInput))
            userInput.contains('=') -> memo.addToMemo(userInput)
            else -> println(listConverter(userInput))
        }
    } while (true)
}

// Execute user input /commands
fun executeCommand(userInput: String?) {
    try {
        when (userInput) {
            "/exit" -> exit()
            "/help" -> return help()
            "/memo" -> return println(memo.internalMemo)
            else -> throw Exception()
        }
    } catch (e: Exception) {
        println(Error.UnknownCMD.msg)
    }
}

// Getting User Input
fun getUserInput(): String? = readLine()

// Help Menu
fun help() = println(
    """The program must calculate expressions like these: 4 + 6 - 8, 2 - 3 - 4, and so on. 
It must support both unary and binary minus operators. 
If the user has entered several same operators following each other, 
the program still should work (like Java or Python REPL).

Consider that the even number of minuses gives a plus, 
and the odd number of minuses gives a minus! Look at it this way: 2 -- 2 equals 2 - (-2) equals 2 + 2.

We suppose that the name of a variable (identifier) can contain only Latin letters. 
The case is also important; for example, n is not the same as N. 
The value can be an integer number or a value of another variable."""
)

// Exit Calculator
fun exit() {
    println("Bye!")
    exitProcess(0)
}