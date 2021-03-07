package cryptography.command

import java.lang.RuntimeException

class UnknownCommandException(argument: String) : RuntimeException("Unknown cryptography.command $argument")
