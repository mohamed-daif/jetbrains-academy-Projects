package calculator

// converting user input from String to MutableList
fun listConverter(userInput: String): Any {
    // find and replace the sequences of plus ( + )
    val line = userInput.replace(" ", "")
        .replace(Regex("[+]+"), "+")
    // find and replace the sequences of ( - ) depends on the length
    var subtractChecked = line
    val minRegex = Regex("[-]+")
    val matches = minRegex.findAll(line).map { it.value }.toList()
    for (match in matches) {
        subtractChecked = if (match.length % 2 == 0) {
            subtractChecked.replaceFirst(match, "+")
        } else subtractChecked.replaceFirst(match, "-")
    }
    // checking for matches '(' ')'
    if (userInput.filter { it == '(' }.count()
        != userInput.filter { it == ')' }.count()
    ) {
        return Error.InvalidExpression.msg
    }
    var final = ""

    for (i in subtractChecked.indices) {
        if (operators.contains(subtractChecked[i])) {
            final += " ${subtractChecked[i]} "
        } else final += subtractChecked[i]
    }
    val list = final.split(' ').toMutableList()
    for (i in list.indices.reversed()) if (list[i] == "") list.removeAt(i)
    // finding duplicates operators
    for (index in 0 until list.lastIndex) {
        for (operator in operators) {
            if (operator.toString() != "(" &&
                operator.toString() != ")" &&
                operator.toString() == list[index] &&
                operator.toString() == list[index + 1]
            )
                return Error.InvalidExpression.msg
        }
    }
    val memoChecked = memo.checkMemo(list)
    val postfix = infixToPostfix(memoChecked)
    return calculator.calc(postfix).toString()
}