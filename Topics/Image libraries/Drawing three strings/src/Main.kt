import java.awt.Color
import java.awt.image.BufferedImage
import java.awt.image.BufferedImage.TYPE_INT_RGB

fun drawStrings(): BufferedImage {
    val hello = "Hello, images!"
    val image = BufferedImage(200, 200, TYPE_INT_RGB)
    val graphics = image.createGraphics()

    graphics.color = Color.RED
    graphics.drawString(hello, 50, 50)
    graphics.color = Color.GREEN
    graphics.drawString(hello, 51, 51)
    graphics.color = Color.BLUE
    graphics.drawString(hello, 52, 52)

    return image

}