package watermark

import kotlin.Exception

fun watermark() {
    try {
        val image = inputFileImage("")
        val watermarkImage = inputFileImage("watermark ")
        val watermarkingProcess = WatermarkingProcess(image, watermarkImage)
        val watermarkedImage = watermarkingProcess.processing()
        writeImageFile(watermarkedImage)

    } catch (e: Exception) {
        println(e.message)
    }
}

fun main() {
    watermark()
}