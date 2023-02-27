package watermark

import java.io.File
import javax.imageio.ImageIO

fun main() {
    println("Input the image filename:")
    val fileName = readln()
    val file = File(fileName)
    if (!file.exists() || !file.isFile) {
        println("The file ${file.name} doesn't exist.")
        return
    }
    val transparency = arrayOf<String>("OPAQUE", "BITMASK", "TRANSLUCENT")
    val image = ImageIO.read(file)
    val colorModel = image.colorModel
    println(
        """Image file: $fileName
Width: ${image.width}
Height: ${image.height}
Number of components: ${colorModel.numComponents}
Number of color components: ${colorModel.numColorComponents}
Bits per pixel: ${colorModel.pixelSize}
Transparency: ${(transparency[colorModel.transparency - 1])}"""
    )

}