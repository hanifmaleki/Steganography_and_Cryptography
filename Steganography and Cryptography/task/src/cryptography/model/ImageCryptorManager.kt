package cryptography.model

import cryptography.util.ByteArrayUtil.bitsToByteArray
import cryptography.util.ByteArrayUtil.bytesToBits
import cryptography.util.ByteArrayUtil.setLeastSignificantBit
import cryptography.exception.ImageIsNotLargeEnough
import cryptography.exception.InputFileNotExist
import java.awt.Color
import java.awt.image.BufferedImage
import java.io.File
import java.lang.RuntimeException
import javax.imageio.ImageIO
import kotlin.collections.ArrayList

const val FORMAT_NAME = "png"

class ImageCryptorManager(private val bufferedImage: BufferedImage) {
    private val width = bufferedImage.width
    private val height = bufferedImage.height

    constructor(filename: String) : this(getBufferedImage(filename))

    companion object {
        private fun getBufferedImage(filename: String): BufferedImage {
            val inputFile = File(filename)
            if (!inputFile.exists()) {
                throw InputFileNotExist(filename)
            }

            return ImageIO.read(inputFile)
        }
    }


    fun hideMessageInImage(bytes: Array<Byte>, outputFilename: String) {
        if (bytes.size * 8 > width * height) {
            throw ImageIsNotLargeEnough()
        }
        val outputBufferImage = writeBytesInBluColor(bytes)

        ImageIO.write(outputBufferImage, FORMAT_NAME, File(outputFilename))
    }

    fun fetchMessageFromImage(): Array<Byte> {
        val bits: Array<Boolean> = getBlueLeastSignificantBits()
        val array = bitsToByteArray(bits)
        val index = getTerminateIndex(array)
        return array.copyOfRange(0, index)
    }

    private fun getTerminateIndex(array: Array<Byte>): Int {
        for (i in array.indices) {
            if (array[i] == 0.toByte() &&
                array[i + 1] == 0.toByte() &&
                array[i + 2] == 3.toByte()
            ) {
                return i
            }
        }
        throw RuntimeException("There is no terminal")
    }


    private fun getBlueLeastSignificantBits(): Array<Boolean> =
        sequence().map { (it.color.blue and 1) == 1 }.toList().toTypedArray()


    private fun sequence() = iterator().asSequence()

    private fun iterator(): Iterator<Pixel> =
        object : Iterator<Pixel> {
            var i = 0
            var j = 0
            override fun hasNext() = (j < height)

            override fun next(): Pixel {
                val color = Color(bufferedImage.getRGB(i, j))
                val pixel = Pixel(i, j, color)
                i++
                if (i == width) {
                    i = 0
                    j++
                }
                return pixel
            }
        }


    //TODO add counter-aware-mapping
    private fun writeBytesInBluColor(bytes: Array<Byte>): BufferedImage {
        val list = ArrayList<Byte>(bytes.map { it })
        list.addAll(listOf(0, 0, 3))
        val bits = bytesToBits(list.toTypedArray())
        val size = bits.size
        val outputBufferImage = BufferedImage(
            width, height,
            BufferedImage.TYPE_INT_RGB
        )
        var counter = 0
        sequence().forEach {
            val sourceColor = it.color
            val destinationColor =
                when {
                    counter < size -> {
                        mapBlueColorByBit(sourceColor, bits[counter])
                    }
                    else -> sourceColor
                }
            outputBufferImage.setRGB(it.x, it.y, destinationColor.rgb)
            counter++
        }
        return outputBufferImage
    }


    private fun mapColorOneByOne(map: (Color) -> Color): BufferedImage {
        val outputBufferImage = BufferedImage(
            width, height,
            BufferedImage.TYPE_INT_RGB
        )
        iterator().forEach {
            val destinationColor = map(it.color)
            outputBufferImage.setRGB(it.x, it.y, destinationColor.rgb)
        }
        return outputBufferImage
    }

    private fun mapBlueColorByBit(sourceColor: Color, bit: Boolean) =
        Color(
            sourceColor.red,
            sourceColor.green,
            setLeastSignificantBit(sourceColor.blue, bit)
        )


}