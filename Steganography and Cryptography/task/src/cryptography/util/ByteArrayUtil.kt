package cryptography.util

import kotlin.math.pow

object ByteArrayUtil {

    fun bytesToBits(bytes: Array<Byte>): List<Boolean> =
        bytes.joinToString(separator = "") { byteToBits(it) }
            .toCharArray().map { it == '1' }

    private fun byteToBits(byte: Byte): String {
        val binaryString = byte.toInt().toString(2)
        val length = binaryString.length
        return "0".repeat(8 - length) + binaryString

    }

    fun setLeastSignificantBit(colorElement: Int, bit: Boolean = true): Int {
        val int = if (bit) 1 else 0
        return (colorElement / 2) * 2 + int
    }

    fun bitsToByteArray(bits: Array<Boolean>): Array<Byte> {
        var counter = 7
        var byte = 0
        val bytes = ArrayList<Byte>()
        for (index in bits.indices) {
            val bit = bits[index]
            if (bit) {
                byte = byte or (2.0.pow(counter).toInt())
            }
            counter--
            if (counter == -1 || index == bits.size - 1) {
                bytes.add(byte.toByte())
                byte = 0
                counter = 7
            }
        }
        return bytes.toTypedArray()
    }
}