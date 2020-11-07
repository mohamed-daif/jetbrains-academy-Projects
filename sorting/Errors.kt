package sorting

fun checkArgsError(args: Array<String>): Boolean {
    val argCommand = listOf(
        "-sortingType", "byCount", "natural",
        "-dataType", "long", "line", "word",
        "-inputFile", "-outputFile"
    )
    if (args.contains("-sortingType")) {
        if (!args.contains("byCount") && !args.contains("natural")) {
            println("No sorting type defined!")
            return true
        }
    }
    if (args.contains("-dataType")) {
        if (!args.contains("long")
            && !args.contains("line")
            && !args.contains("word")
        ) {
            println("No data type defined!")
            return true
        }
    }
    if (args.contains("-inputFile")) {
        if (fileToRead.isBlank()) {
            println("No inputFile defined!")
            return true
        }
    }
    if (args.contains("-outputFile")) {
        if (fileToRead.isBlank()) {
            println("No outputFile defined!")
            return true
        }
    }
    args.forEach {
        if (it !in argCommand) println("\"$it\" is not a long. It will be skipped.")
    }
    return false
}
