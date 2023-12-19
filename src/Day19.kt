fun main() = test(
    ::solve,
)

private data class Rule(
    val name: String,
    val commands: List<Command>,
)

private sealed class Command {
    abstract val nextRuleName: String

    abstract fun test(p: Part): Boolean

    data class Unconditional(
        override val nextRuleName: String,
    ) : Command() {
        override fun test(p: Part): Boolean =
            true
    }

    data class Conditional(
        val checkedComponent: Char,
        val condition: Char,
        val value: Int,
        override val nextRuleName: String,
    ) : Command() {
        override fun test(p: Part): Boolean {
            val actualValue = p[checkedComponent]!!
            return when (condition) {
                '>' -> actualValue > value
                '<' -> actualValue < value
                else -> throw IllegalStateException(this.toString())
            }
        }
    }
}

private typealias Part = Map<Char, Int>

private fun solve(input: List<String>): Long {
    val (ruleStrs, partStrs) = input.split("").map { it.toList() }.toList()

    val ruleRegex = """(.+)\{(.+)\}""".toRegex()
    val condCmdRegex = """(.)([<>])(\d+):(.+)""".toRegex()
    val rules = ruleStrs.associate { ruleStr ->
        val (name, cmdStrs) = ruleRegex.matchEntire(ruleStr)!!.destructured
        val cmds = cmdStrs.split(',').map { cmdStr ->
            when (val m = condCmdRegex.matchEntire(cmdStr)) {
                null -> Command.Unconditional(cmdStr)
                else -> {
                    val vs = m.groupValues
                    Command.Conditional(vs[1][0], vs[2][0], vs[3].toInt(), vs[4])
                }
            }
        }
        name to Rule(name, cmds)
    }

    val partRegex = """\{(.+)\}""".toRegex()
    val partComponentRegex = """(.)=(\d+)""".toRegex()
    val parts: List<Part> = partStrs.map { partStr ->
        partRegex.matchEntire(partStr)!!.groupValues[1].split(",").associate { compStr ->
            val (name, value) = partComponentRegex.matchEntire(compStr)!!.destructured
            name[0] to value.toInt()
        }
    }

    tailrec fun applyRules(part: Part, rule: String): Boolean =
        when (rule) {
            "A" -> true
            "R" -> false
            else -> applyRules(part, rules[rule]!!.commands.first { it.test(part) }.nextRuleName)
        }

    val startRule = "in"

    return parts.filter { applyRules(it, startRule) }.sumOf { it.values.sum().toLong() }
}