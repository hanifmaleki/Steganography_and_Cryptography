package cryptography.command

interface Command {
    fun getArgCount() = 0
    fun runCommand(args: Array<String>) = CommandResult("")
    fun getInputMessages() = emptyArray<String>()
    fun isExit() = false
    fun isDefault()= false
}