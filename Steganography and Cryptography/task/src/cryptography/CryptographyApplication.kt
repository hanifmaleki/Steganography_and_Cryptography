package cryptography

import cryptography.command.Command
import cryptography.command.CommandManager
import cryptography.command.CommandResult
import cryptography.service.CryptographyManager

object CryptographyApplication : CommandManager() {

    override fun initializeApplication() {
    }

    override fun getCommand(argument: String) =
        CommandType.getCommandType(argument)


    override fun getMainCommand() = "Task (hide, show, exit):"

    override fun getDefaultCommand() = CommandType.WRONG_TASK
}

enum class CommandType(val command: String) : Command {
    HIDE("hide") {
        override fun getArgCount() = 4
        override fun runCommand(args: Array<String>) = CryptographyManager.hideCommand(args)
        override fun getInputMessages() = arrayOf("Input image file:", "Output image file:", "Message to hide:", "Password:")
    },
    SHOW("show") {
        override fun getArgCount() = 2
        override fun getInputMessages() = arrayOf("Input image file:", "Password:")
        override fun runCommand(args: Array<String>) = CryptographyManager.showCommand(args)
    },
    WRONG_TASK("") {
        override fun runCommand(args: Array<String>) = CommandResult("Wrong task: ${args[0]}")
        override fun getArgCount() = 1
        override fun isDefault() = true
    },
    Exit("exit") {
        override fun isExit() = true
        override fun runCommand(args: Array<String>) = CommandResult("Bye!")
    };

    companion object {
        fun getCommandType(command: String) = values().firstOrNull { value -> value.command == command }
    }
}