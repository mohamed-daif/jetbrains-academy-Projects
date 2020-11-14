package phonebook

import java.io.File
import kotlin.math.floor
import kotlin.math.sqrt
import kotlin.time.ExperimentalTime

val phonebook = mutableListOf<String>()
val phonebookHashMap = mutableMapOf<Number, String>()
val find = mutableListOf<String>()
var startTime = 0L
var linerSearchTime = 0L
var linerMSG = ""
var bubbleSortTime = 0L
var jumpSearchTime = 0L
var count = 0
var timeExceeded = ""
var quickSearchTime = 0L
var binarySearchTime = 0L
var createHashMapTime = 0L
var hashSearchTime = 0L

@ExperimentalTime
fun main(args: Array<String>) {
    readFiles()

    println("Start searching (linear search)...")
    linearSearch()
    println(linerMSG)
    println()

    println("Start searching (bubble sort + jump search)...")
    bubbleSort()
    finalResult()
    println()

    println("Start searching (quick sort + binary search)...")
    startTime = currentTime()
    quickSort(phonebook)
    binarySearch()

    println()
    println("Start searching (hash table)...")
    createHashTable()
}

fun finalResult() {
    fun result(SortTime: Long, SearchTime: Long) {
        println("Found $count / ${find.size} entries. Time taken: ${timeFormat(SortTime + SearchTime)}")
        println("Sorting time: ${timeFormat(SortTime)}")
        println("Searching time: ${timeFormat(SearchTime)}")
    }
    if (jumpSearchTime == 0L) {
        result(bubbleSortTime, linerSearchTime)
    } else {
        result(bubbleSortTime, jumpSearchTime)
    }
}

fun linearSearch() {
    startTime = currentTime()
    find.forEach { i ->
        phonebook.forEach {
            if (it.substringBeforeLast(" ") == i) count++
        }
    }
    linerSearchTime = currentTime() - startTime
    val endTime = timeFormat(linerSearchTime)
    linerMSG = "Found $count / ${find.size} entries. Time taken: $endTime"
}

fun bubbleSort() {
    var swap = true
    startTime = currentTime()

    while (swap) {
        swap = false
        for (i in 0 until phonebook.size - 1) {
            if (phonebook[i] > phonebook[i + 1]) {
                val temp = phonebook[i]
                phonebook[i] = phonebook[i + 1]
                phonebook[i + 1] = temp
                bubbleSortTime = currentTime() - startTime
                if (bubbleSortTime / 10 > linerSearchTime) {
                    timeExceeded = " - STOPPED, moved to linear search"
                    break
                } else swap = true
            }
        }
    }
    if (!timeExceeded.isNotBlank()) linearSearch()
    else {
        jumpSearch()
        timeExceeded = ""
    }
}

fun jumpSearch() {
    count = 0
    startTime = currentTime()
    for (x in find) {
        val size = phonebook.size
        var step = floor(sqrt(size.toDouble())).toInt()
        var prev = 0

        while (phonebook[step.coerceAtMost(size) - 1] < x) {
            prev = step
            step += floor(sqrt(size.toDouble())).toInt()
            if (prev >= size) count += 0
        }

        while (phonebook[prev] < x) {
            prev++
            if (prev == step.coerceAtMost(size)) count += 0
        }

        if (phonebook[prev] == x) 1 else count += 1
    }
    jumpSearchTime = currentTime() - startTime
}

fun quickSort(phonebook: MutableList<String>, low: Int = 0, high: Int = phonebook.lastIndex) {
    fun swap(phonebook: MutableList<String>, first: Int, second: Int) {
        val temp = phonebook[first]
        phonebook[first] = phonebook[second]
        phonebook[second] = temp
    }

    fun partition(phonebook: MutableList<String>, low: Int, high: Int): Int {
        val pivot = phonebook[high]
        var i = low
        for (j in low until high) {
            if (phonebook[j] <= pivot) {
                swap(phonebook, i, j)
                i++
            }
        }
        swap(phonebook, i, high)
        return i
    }
    if (low < high) {
        val pi = partition(phonebook, low, high)
        quickSort(phonebook, low, pi - 1)
        quickSort(phonebook, pi + 1, high)
    }
}

fun binarySearch() {
    quickSearchTime = currentTime() - startTime
    startTime = currentTime()
    count = 0

    fun inSearch(record: String, left: Int = 0, right: Int = phonebook.lastIndex) {
        val mid = (left + right) / 2
        val cc = phonebook[mid].substringBeforeLast(" ")
        when {
            record > cc -> inSearch(record, mid + 1, right)
            record < cc -> inSearch(record, left, mid - 1)
            record == cc -> count++
        }
    }
    find.forEach { inSearch(it) }
    binarySearchTime = currentTime() - startTime

    println("Found $count / ${find.size} entries. Time taken: ${timeFormat(quickSearchTime + binarySearchTime)}")
    println("Sorting time: ${timeFormat(quickSearchTime)}")
    println("Searching time: ${timeFormat(binarySearchTime)}")
}

fun createHashTable() {
    startTime = currentTime()
    phonebook.forEach { phonebookHashMap[it.substringBeforeLast(" ").hashCode()] = it }
    createHashMapTime = currentTime() - startTime
    hashTableSearch()
}

fun hashTableSearch() {
    startTime = currentTime()
    count = 0

    find.forEach { if (it.hashCode() in phonebookHashMap) count++ }
    hashSearchTime = currentTime() - startTime
    println()
    println("Found $count / ${find.size} entries. Time taken: ${timeFormat(createHashMapTime + hashSearchTime)}")
    println("Creating time: ${timeFormat(createHashMapTime)}")
    println("Searching time: ${timeFormat(hashSearchTime)}")
}

fun readFiles() {
    val directory = File("/mnt/sdb1/IntelliJIDEAProjects/directory.txt")
    val findFile = File("/mnt/sdb1/IntelliJIDEAProjects/find.txt")
    directory.forEachLine { line -> phonebook.add("${line.substringAfter(" ")} ${line.substringBefore(" ")}") }
    findFile.forEachLine { find.add(it) }
}

fun timeFormat(ms: Long) = String.format("%1\$tM min. %1\$tS sec. %1\$tL ms.$timeExceeded", ms)

fun currentTime() = System.currentTimeMillis()