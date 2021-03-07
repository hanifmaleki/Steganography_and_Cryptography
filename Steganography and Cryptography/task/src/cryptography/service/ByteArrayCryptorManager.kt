package cryptography.service

object ByteArrayCryptorManager {

    fun encodeMessage(message: String, password: String): Array<Byte> {
        val messageBytes = message.toByteArray().toTypedArray()
        val passwordBytes = password.toByteArray().toTypedArray()
        return xorArrays(messageBytes, passwordBytes)
    }

    fun decodeMessage(messageByte: Array<Byte>, password: String): String {
        val passwordBytes = password.toByteArray().toTypedArray()
        val decodedArray = xorArrays(messageByte, passwordBytes)
        return decodedArray.toByteArray().toString(Charsets.UTF_8)
    }

    private fun xorArrays(
        messageBytes: Array<Byte>,
        passwordBytes: Array<Byte>
    ): Array<Byte> {
        val iterator = getPasswordIterator(passwordBytes)
        return messageBytes.map {
            byteXor(it, iterator.next())
        }.toTypedArray()
    }

    private fun byteXor(byte1: Byte, byte2: Byte) =
        (byte1.toInt() xor byte2.toInt()).toByte()

    private fun getPasswordIterator(passwordByte: Array<Byte>): Iterator<Byte> = object : Iterator<Byte> {
        var index = 0

        override fun hasNext() = true

        override fun next(): Byte {
            val byte = passwordByte[index]
            index = (index + 1) % passwordByte.size
            return byte
        }
    }
}