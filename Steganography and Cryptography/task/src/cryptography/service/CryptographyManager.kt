package cryptography.service

import cryptography.command.CommandResult
import cryptography.exception.ImageIsNotLargeEnough
import cryptography.exception.InputFileNotExist
import cryptography.model.ImageCryptorManager

object CryptographyManager {
    val hideCommand: (args: Array<String>) -> CommandResult = { args ->
        val inputFilename = args[0]
        val outputFilename = args[1]
        val message = args[2]
        val password = args[3]
        try {
            val cryptoManager = ImageCryptorManager(inputFilename)
            val encoded: Array<Byte> = ByteArrayCryptorManager.encodeMessage(message, password)
            cryptoManager.hideMessageInImage(encoded, outputFilename)
            CommandResult("Message saved in $outputFilename image.")
        } catch (exception: InputFileNotExist) {
            CommandResult("Can't read input file!")
        } catch (exception: ImageIsNotLargeEnough) {
            CommandResult("The input image is not large enough to hold this message.")
        }
    }

    val showCommand: (args: Array<String>) -> CommandResult = { args ->
        val inputFilename = args[0]
        val password = args[1]
        try {
            val cryptoManager = ImageCryptorManager(inputFilename)
            val encodedMessage: Array<Byte> = cryptoManager.fetchMessageFromImage()
            val message = ByteArrayCryptorManager.decodeMessage(encodedMessage, password)
            CommandResult("Message:\n$message")
        } catch (exception: InputFileNotExist) {
            CommandResult("Can't read input file!")
        }
    }
}