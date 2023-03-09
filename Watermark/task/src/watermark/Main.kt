package watermark

import java.awt.Color
import kotlin.Exception

fun watermark() {
    try {
        val image = inputFileImage("")
        val watermarkImage = inputFileImage("watermark ")
        checkImages(image, watermarkImage)
        val usingTransparencyColor = checkUsingOfTransparencyColor(watermarkImage)
        val usingAlpha = if (!usingTransparencyColor) checkUsingOfAlpha(watermarkImage) else false
        val transparencyColor: Color = if (usingTransparencyColor) inputTransparencyColor() else Color.BLUE
        val transparency = readTransparencyPercentage()
        val watermarkedImage = if (usingTransparencyColor) {
            blendingImagesUsingTransparencyColor(image, watermarkImage, transparency, transparencyColor)
        } else
            if (usingAlpha) blendingImagesUsingAlpha(image, watermarkImage, transparency)
            else blendingImages(image, watermarkImage, transparency)
        writeImageFile(watermarkedImage)

    } catch (e: Exception) {
        println(e.message)
    }
}

fun main() {
    watermark()
}