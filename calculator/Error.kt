package calculator

enum class Error(val msg: String) {
    InvalidExpression("Invalid expression"),
    InvalidIdentifier("Invalid identifier"),
    InvalidAssignment("Invalid assignment"),
    UnknownVariable("Unknown variable"),
    UnknownCMD("Unknown command");
}