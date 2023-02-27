import java.awt.Color
import java.awt.Graphics2D
import java.awt.image.BufferedImage
import java.awt.image.BufferedImage.TYPE_INT_RGB

const val RADIUS = 100
fun drawCircles(): BufferedImage {
    val image = BufferedImage(200, 200, TYPE_INT_RGB)
    val graphics = image.createGraphics()
    drawCircle(graphics, 50, 50, Color.RED)
    drawCircle(graphics, 50, 75, Color.YELLOW)
    drawCircle(graphics, 75, 50, Color.GREEN)
    drawCircle(graphics, 75, 75, Color.BLUE)
    return image
}

fun drawCircle(graphics: Graphics2D, x: Int, y: Int, color: Color) {
    graphics.color = color
    graphics.drawOval(x, y, RADIUS, RADIUS)
}