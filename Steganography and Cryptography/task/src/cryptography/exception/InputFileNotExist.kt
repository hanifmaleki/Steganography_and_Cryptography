package cryptography.exception

import java.lang.RuntimeException

class InputFileNotExist(inputFilename: String) : RuntimeException("File $inputFilename does not exist")
