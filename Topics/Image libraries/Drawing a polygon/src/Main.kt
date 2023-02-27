import java.awt.Color
import java.awt.image.BufferedImage
import java.awt.image.BufferedImage.TYPE_INT_RGB
@SuppressWarnings("MagicNumber")
fun drawPolygon(): BufferedImage {
    val image = BufferedImage(300, 300, TYPE_INT_RGB)
    val graphics = image.createGraphics()
    val arrayX = intArrayOf(50, 100, 200, 250, 200, 100)
    val arrayY = intArrayOf(150, 250, 250, 150, 50, 50)
    graphics.color = Color.YELLOW
    graphics.drawPolygon(arrayX, arrayY, 6)
    return image
}