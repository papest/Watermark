package watermark

import java.awt.Color
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO
import kotlin.Exception

fun readFile(watermark: String): File {
    println("Input the ${watermark}image filename:")
    val fileName = readln()
    val file = File(fileName)
    if (!file.exists()) throw Exception("The file $fileName doesn't exist.")
    return file
}

fun checkImageProperty(image: BufferedImage, watermark: Boolean) {
    val colorModel = image.colorModel
    val imageName = if (watermark) "watermark" else "image"
    if (colorModel.numColorComponents != 3) throw Exception("The number of $imageName color components isn't 3.")
    if (colorModel.pixelSize != 24 && colorModel.pixelSize != 32) throw Exception("The $imageName isn't 24 or 32-bit.")
}

fun inputFileImage(watermark: String): BufferedImage {
    val file = readFile(watermark)
    val image = ImageIO.read(file)
    checkImageProperty(image, watermark.isNotEmpty())
    return image
}

fun checkImages(image: BufferedImage, watermarkImage: BufferedImage) {
    if (image.width != watermarkImage.width || image.height != watermarkImage.height)
        throw Exception("The image and watermark dimensions are different.")
}

fun readTransparencyPercentage(): Int {
    println("Input the watermark transparency percentage (Integer 0-100):")
    val percentage: Int
    try {
        percentage = readln().toInt()

        if (percentage !in 0..100) throw Exception("The transparency percentage is out of range.")

    } catch (e: NumberFormatException) {
        throw Exception("The transparency percentage isn't an integer number.")
    }
    return percentage
}

fun blendingImages(image: BufferedImage, watermarkImage: BufferedImage, transparency: Int): BufferedImage {
    val watermarkedImage = BufferedImage(image.width, image.height, BufferedImage.TYPE_INT_RGB)
    for (x in 0 until image.width) {
        for (y in 0 until image.height) {
            val iColor = Color(image.getRGB(x, y))
            val wColor = Color(watermarkImage.getRGB(x, y))
            val color = Color(
                (wColor.red * transparency + iColor.red * (100 - transparency)) / 100,
                (wColor.green * transparency + iColor.green * (100 - transparency)) / 100,
                (wColor.blue * transparency + iColor.blue * (100 - transparency)) / 100
            )
            watermarkedImage.setRGB(x, y, color.rgb)
        }
    }
    return watermarkedImage
}

fun writeImageFile(outputImage: BufferedImage) {
    println("Input the output image filename (jpg or png extension):")
    val fileName = readln()
    val outFile = File(fileName)
    if (outFile.extension != "jpg" && outFile.extension != "png") throw Exception("The output file extension isn't \"jpg\" or \"png\".")
    ImageIO.write(outputImage, outFile.extension, outFile)
    println("The watermarked image $fileName has been created.")
}

fun watermark() {
    try {
        val image = inputFileImage("")
        val watermarkImage = inputFileImage("watermark ")
        checkImages(image, watermarkImage)
        val transparency = readTransparencyPercentage()
        val watermarkedImage = blendingImages(image, watermarkImage, transparency)
        writeImageFile(watermarkedImage)

    } catch (e: Exception) {
        println(e.message)
    }
}

fun main() {
    watermark()
}