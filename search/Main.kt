package search

import java.io.File
import kotlin.system.exitProcess

val foundPeople = mutableSetOf<String>()
val tempList = mutableSetOf<String>()

fun main(args: Array<String>) {
    val people = File(args[1]).readLines()

    while (true) {
        actionMenu()
        when (readLine()!!) {
            "1" -> findPerson(people)
            "2" -> getAllPeople(people)
            "0" -> exit()
            else -> println("Incorrect option! Try again.")
        }
    }
}

fun findPerson(people: List<String>) {
    println("\nSelect a matching strategy: ALL, ANY, NONE")
    val strategy = readLine()!!

    println("\nEnter a name or email to search all suitable people.")
    val words = readLine()!!.split(" ")

    words.forEach { it ->
        val x = it.toLowerCase()
        people.forEach {
            val l = it.toLowerCase().split(" ")
            if (x in l) tempList.add(it)
        }
    }

    when (strategy) {
        "ALL" -> findAll(people, words)
        "ANY" -> findAny(people, words)
        "NONE" -> findNon(people, words)
        else -> return println("No selection")
    }

    if (foundPeople.isEmpty()) {
        println("No matching people found.")
    } else {
        println("${foundPeople.size} persons found:")
        for (element in foundPeople) println(element)
    }
    tempList.clear()
    foundPeople.clear()
}

fun findAll(people: List<String>, words: List<String>) {
    val wordsSize = words.size
    tempList.forEach {
        var matches = 0
        val line = it.toLowerCase().split(" ")
        words.forEach { word ->
            if (word.toLowerCase() in line) matches++
            if (matches == wordsSize) foundPeople.add(it)
        }
    }
}

fun findAny(people: List<String>, words: List<String>) {
    people.forEach { if (it in tempList) foundPeople.add(it) }
}

fun findNon(people: List<String>, words: List<String>) {
    people.forEach { if (it !in tempList) foundPeople.add(it) }
}

fun getAllPeople(people: List<String>) {
    println("\n=== List of people ===")
    return people.forEach(::println)
}

fun actionMenu() {
    println(
        """
=== Menu ===
1. Find a person
2. Print all people
0. Exit"""
    )
}

fun exit() {
    println("Bye!")
    exitProcess(0)
}