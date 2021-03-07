package cryptography.command

import java.util.*


abstract class CommandManager {
    private val scanner = Scanner(System.`in`)

    private fun processCommand(command: Command, arg: String? = null) {
        do {
            val args = getArguments(command, arg)
            val result = command.runCommand(args)
            println(result.message)
        } while (!result.success)
    }

    private fun getArguments(command: Command, arg: String? = null): Array<String> {
        if (arg != null) return arrayOf(arg)
        val count = command.getArgCount()
        return Array(count) {
            println(command.getInputMessages()[it])
            scanner.nextLine()
        }
    }

    fun executeProgram() {
        initializeApplication()
        do {
            println(getMainCommand())
            val argument = scanner.nextLine()!!
            val command = getCommand(argument)
            if (command == null) {
                val defaultCommand = getDefaultCommand() ?: throw UnknownCommandException(argument)
                processCommand(defaultCommand, argument)
            } else {
                processCommand(command)
            }
        } while (command == null || !command.isExit())
    }

    abstract fun initializeApplication()
    abstract fun getCommand(nextLine: String): Command?
    abstract fun getMainCommand(): String
    open fun getDefaultCommand(): Command? = null

}


