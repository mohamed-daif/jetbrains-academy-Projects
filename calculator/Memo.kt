package calculator

import java.math.BigInteger

class Memo {
    var internalMemo = mutableMapOf<String, BigInteger>()

    private fun save(varName: String, varValue: BigInteger) {
        internalMemo[varName] = varValue
    }

    fun inMemo(key: String): Boolean {
        return key in internalMemo.keys
    }

    fun getFromMemo(key: String): String {
        return if (inMemo(key)) {
            internalMemo[key].toString()
        } else {
            Error.UnknownVariable.msg
        }
    }

    fun addToMemo(userInput: String) {
        val list = userInput.replace(" ", "").split('=').toMutableList()
        val identifier = list.first()
        var assignment = list.last()

        if (list.size == 2) {
            if (assignment.matches(Regex("^[a-zA=Z]+"))) {
                if (inMemo(assignment)) assignment = getFromMemo(assignment)
                else return println(Error.UnknownVariable.msg)
            }

            if (!identifier.matches(Regex("^[a-zA=Z]+"))) {
                println(Error.InvalidIdentifier.msg)
            } else if (!assignment.matches(Regex("[0-9|\\-]+"))) {
                println(Error.InvalidAssignment.msg)
            } else {
                save(identifier, assignment.toBigInteger())
            }
        } else println(Error.InvalidAssignment.msg)
    }
    fun checkMemo(list: MutableList<String>): MutableList<String> {
        for (i in list.indices) {
            if (list[i].contains(Regex("[A-z]"))) {
                if (memo.inMemo(list[i])) {
                    list[i] = memo.getFromMemo(list[i])
                } else {
                    println(Error.UnknownVariable.msg)
                    startCalculator()
                }
            }
        }

        return list
    }
}
