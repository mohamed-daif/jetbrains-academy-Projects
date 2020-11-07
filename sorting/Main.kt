package sorting

import java.io.File
import java.util.*
import kotlin.system.exitProcess

var fileToRead = ""
var fileToWrite = ""
val input = mutableListOf<String>()
fun main(args: Array<String>) {
    val scanner = Scanner(System.`in`)
    getFilesNames(args)
    if (checkArgsError(args)) exitProcess(1)
    when {
        args.contains("-inputFile") -> {
            if (args.contains("line")) {
                File(fileToRead).readLines().forEach { input.add(it) }
            } else File(fileToRead).readText().split(Regex("[\\s]+")).forEach { input.add(it) }
        }
        else -> while (scanner.hasNext()) {
            if (args.isNotEmpty() && args.contains("line")) {
                input.add(scanner.nextLine())
            } else input.add(scanner.next())
        }
    }

    if (args.isNotEmpty()) getSortingType(args) else sortWords("natural")
}

fun getFilesNames(args: Array<String>) {
    for (i in args.indices) {
        if (args[i] == "-inputFile") {
            fileToRead = args[i + 1]
            continue
        }
        if (args[i] == "-outputFile") {
            fileToWrite = args[i + 1]
        }
    }
}

fun getSortingType(args: Array<String>) {
    val sortingType = if (args.contains("byCount")) "byCount" else "natural"
    when {
        args.contains("long") -> sortLongs(sortingType)
        args.contains("line") -> sortLines(sortingType)
        else -> sortWords(sortingType)
    }
}

fun sortLongs(sortingType: String) {
    val total = input.size
    val longData = mutableListOf<Long>()
    input.forEach { longData.add(it.toLong()) }
    val sortedData = longData.sorted().groupingBy { it }.eachCount().toList().sortedBy { it.second }

    if (fileToWrite.isNullOrBlank()) {
        println("Total numbers: $total.")
        when (sortingType) {
            "byCount" -> {
                sortedData.forEach { println("${it.first}: ${it.second} time(s), ${percentage(it.second, total)}%") }
            }
            "natural" -> println("Sorted data: ${longData.sorted().joinToString(" ")}")
        }
    } else {
        File(fileToWrite).writeText("Total numbers: $total.\n")
        when (sortingType) {
            "byCount" -> {
                sortedData.forEach {
                    File(fileToWrite).appendText(
                        "${it.first}: ${it.second} time(s), ${percentage(it.second, total)}%\n"
                    )
                }
            }
            "natural" -> File(fileToWrite).appendText("Sorted data: ${longData.sorted().joinToString(" ")}")
        }
    }
}

fun sortLines(sortingType: String) {
    if (fileToWrite.isNullOrBlank()) {
        println("Total lines: ${input.size}.")
    } else {
        File(fileToWrite).writeText("Total lines: ${input.size}.\n")
    }
    when (sortingType) {
        "byCount" -> sortByCount(input)
        "natural" -> sortByNatural(input, "line")
    }
}

fun sortWords(sortingType: String) {
    if (fileToWrite.isNullOrBlank()) {
        println("Total words: ${input.size}.")
    } else {
        File(fileToWrite).writeText("Total words: ${input.size}.\n")
    }
    when (sortingType) {
        "byCount" -> sortByCount(input)
        "natural" -> sortByNatural(input)
    }
}

fun sortByNatural(arr: MutableList<String>, _dataType: String = "word") {
    if (fileToWrite.isNullOrBlank()) {
        if (_dataType == "line") {
            println("Sorted data:")
            arr.sorted().forEach { println(it) }
        } else println("Sorted data: ${arr.sorted().joinToString(" ")}")
    } else {
        if (_dataType == "line") {
            File(fileToWrite).appendText("Sorted data:\n")
            arr.sorted().forEach { println(it) }
        } else File(fileToWrite).appendText("Sorted data: ${arr.sorted().joinToString(" ")}")
    }
}

fun sortByCount(arr: MutableList<String>) {
    val sortedData = arr.sorted().groupingBy { it }.eachCount().toList().sortedBy { it.second }
    if (fileToWrite.isNullOrBlank()) {
        sortedData.forEach { println("${it.first}: ${it.second} time(s), ${percentage(it.second, arr.size)}%") }
    } else {
        sortedData.forEach {
            File(fileToWrite).appendText(
                "${it.first}: ${it.second} time(s), ${
                    percentage(
                        it.second,
                        arr.size
                    )
                }%\n"
            )
        }
    }
}

fun percentage(element: Int, total: Int) = element * 100 / total