package watermark

import java.awt.Color
import java.awt.Transparency
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

object WatermarkFunctions {

    fun readFile(watermark: String): File {
        println("Input the ${watermark}image filename:")
        val fileName = readln()
        val file = File(fileName)
        if (!file.exists()) throw Exception("The file $fileName doesn't exist.")
        return file
    }
}

fun checkImages(image: BufferedImage, watermarkImage: BufferedImage) {
    if (image.width < watermarkImage.width || image.height < watermarkImage.height)
        throw Exception("The watermark's dimensions are larger.")
}

fun checkImageProperty(image: BufferedImage, watermark: Boolean) {
    val colorModel = image.colorModel
    val imageName = if (watermark) "watermark" else "image"
    if (colorModel.numColorComponents != 3) throw Exception("The number of $imageName color components isn't 3.")
    if (colorModel.pixelSize != 24 && colorModel.pixelSize != 32) throw Exception("The $imageName isn't 24 or 32-bit.")
}

fun checkUsingOfAlpha(watermarkImage: BufferedImage): Boolean {
    var usingOfAlpha = false

    if (watermarkImage.transparency == Transparency.TRANSLUCENT) {
        println("Do you want to use the watermark's Alpha channel?")
        if (readln().lowercase() == "yes") {
            usingOfAlpha = true
        }
    }

    return usingOfAlpha
}

fun checkUsingOfTransparencyColor(watermarkImage: BufferedImage): Boolean {
    var usingOfTransparencyColor = false

    if (watermarkImage.transparency != Transparency.TRANSLUCENT) {
        println("Do you want to set a transparency color?")
        if (readln() == "yes") {
            usingOfTransparencyColor = true
        }
    }

    return usingOfTransparencyColor
}

fun inputFileImage(watermark: String): BufferedImage {
    val file = WatermarkFunctions.readFile(watermark)
    val image = ImageIO.read(file)
    checkImageProperty(image, watermark.isNotEmpty())
    return image
}

fun inputTransparencyColor(): Color {
    try {
        println("Input a transparency color ([Red] [Green] [Blue]):")
        val color = readln().split(" ")
        if (color.size != 3) throw Exception()
        return Color(color[0].toInt(), color[1].toInt(), color[2].toInt())
    } catch (e: Exception) {
        throw Exception("The transparency color input is invalid.")
    }
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

fun readPositionMethod(): String {
    println("Choose the position method (single, grid):")
    val positionMethod = readln()
    return if (positionMethod == "single" || positionMethod == "grid") positionMethod
    else throw Exception("The position method input is invalid.")
}

fun readPosition(process: WatermarkingProcess) {
    println(
        "Input the watermark position ([x 0-${
            process.image.width
                    - process.watermarkImage.width
        }] [y 0-${process.image.height - process.watermarkImage.height}]):"
    )
    val x: Int
    val y: Int
    try {
        val array = readln().split(" ")
        if (array.size != 2) throw Exception()
        x = array[0].toInt()
        y = array[1].toInt()
    } catch (e: Exception) {
        throw Exception("The position input is invalid.")
    }
    if (x < 0 || x > process.image.width
        - process.watermarkImage.width || y < 0 || y > process.image.height - process.watermarkImage.height
    ) {
        throw Exception("The position input is out of range.")
    }
    process.x = x
    process.y = y
}

fun blendingImages(
    image: BufferedImage,
    watermarkImage: BufferedImage,
    transparency: Int,
    usingAlpha: Boolean,
    usingTransparencyColor: Boolean,
    transparencyColor: Color = Color.BLUE,
    positionMethod: Boolean,
    x0: Int,
    y0: Int
): BufferedImage {
    return if (usingTransparencyColor) {
        blendingImagesUsingTransparencyColor(
            image,
            watermarkImage,
            transparency,
            transparencyColor,
            positionMethod,
            x0,
            y0
        )
    } else
        if (usingAlpha) blendingImagesUsingAlpha(image, watermarkImage, transparency, positionMethod, x0, y0)
        else blendingImages(image, watermarkImage, transparency, positionMethod, x0, y0)

}

fun blendingImages(
    image: BufferedImage,
    watermarkImage: BufferedImage,
    transparency: Int,
    positionMethod: Boolean,
    x0: Int,
    y0: Int
): BufferedImage {
    val watermarkedImage = BufferedImage(image.width, image.height, BufferedImage.TYPE_INT_RGB)
    for (x in 0 until image.width) {
        for (y in 0 until image.height) {
            val iColor = Color(image.getRGB(x, y))
            val wColor = Color(
                if (!positionMethod) {
                    watermarkImage.getRGB(x % watermarkImage.width, y % watermarkImage.height)
                } else {
                    if (x >= x0 && x < x0 + watermarkImage.width && y >= y0 && y < y0 + watermarkImage.height) {
                        watermarkImage.getRGB(x - x0, y - y0)
                    } else {
                        0
                    }
                }
            )
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

fun blendingImagesUsingAlpha(
    image: BufferedImage,
    watermarkImage: BufferedImage,
    transparency: Int,
    positionMethod: Boolean,
    x0: Int,
    y0: Int
): BufferedImage {
    val watermarkedImage = BufferedImage(image.width, image.height, BufferedImage.TYPE_INT_RGB)
    for (x in 0 until image.width) {
        for (y in 0 until image.height) {
            val iColor = Color(image.getRGB(x, y))

            val wColor = if (!positionMethod) {
                Color(watermarkImage.getRGB(x % watermarkImage.width, y % watermarkImage.height), true)
            } else if (x >= x0 && x < x0 + watermarkImage.width && y >= y0 && y < y0 + watermarkImage.height) Color(
                watermarkImage.getRGB(x - x0, y - y0), true
            )
            else {
                Color(0)
            }
            val color =
                if (positionMethod && (x >= x0 && x < x0 + watermarkImage.width && y >= y0 && y < y0 + watermarkImage.height) || !positionMethod) {
                    Color(
                        (wColor.red * transparency * wColor.alpha / 255 + iColor.red * (100 - transparency * wColor.alpha / 255)) / 100,
                        (wColor.green * transparency * wColor.alpha / 255 + iColor.green * (100 - transparency * wColor.alpha / 255)) / 100,
                        (wColor.blue * transparency * wColor.alpha / 255 + iColor.blue * (100 - transparency * wColor.alpha / 255)) / 100
                    )
                } else {
                    iColor
                }



            watermarkedImage.setRGB(x, y, color.rgb)
        }
    }
    return watermarkedImage
}

fun blendingImagesUsingTransparencyColor(
    image: BufferedImage,
    watermarkImage: BufferedImage,
    transparency: Int,
    transparencyColor: Color,
    positionMethod: Boolean,
    x0: Int,
    y0: Int
): BufferedImage {
    val watermarkedImage = BufferedImage(image.width, image.height, BufferedImage.TYPE_INT_RGB)
    for (x in 0 until image.width) {
        for (y in 0 until image.height) {
            val iColor = Color(image.getRGB(x, y))
            val wColor = if (!positionMethod) Color(
                watermarkImage.getRGB(
                    x % watermarkImage.width,
                    y % watermarkImage.height
                )
            ) else {
                if (x >= x0 && x < x0 + watermarkImage.width && y >= y0 && y < y0 + watermarkImage.height) {
                    Color(watermarkImage.getRGB(x - x0, y - y0))
                } else transparencyColor
            }
            val color =
                if (wColor.red != transparencyColor.red || wColor.green != transparencyColor.green ||
                    wColor.blue != transparencyColor.blue
                ) Color(
                    (wColor.red * transparency + iColor.red * (100 - transparency)) / 100,
                    (wColor.green * transparency + iColor.green * (100 - transparency)) / 100,
                    (wColor.blue * transparency + iColor.blue * (100 - transparency)) / 100
                ) else iColor
            watermarkedImage.setRGB(x, y, color.rgb)
        }
    }
    return watermarkedImage
}

fun writeImageFile(outputImage: BufferedImage) {
    println("Input the output image filename (jpg or png extension):")
    val fileName = readln()
    val outFile = File(fileName)
    if (outFile.extension != "jpg" && outFile.extension != "png")
        throw Exception("The output file extension isn't \"jpg\" or \"png\".")
    ImageIO.write(outputImage, outFile.extension, outFile)
    println("The watermarked image $fileName has been created.")
}