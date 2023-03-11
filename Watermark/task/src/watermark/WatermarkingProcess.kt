package watermark

import java.awt.Color
import java.awt.image.BufferedImage

class WatermarkingProcess(val image: BufferedImage, val watermarkImage: BufferedImage) {

    private var usingTransparencyColor = false
    private var usingAlpha = false
    private var transparencyColor: Color
    private var transparency: Int = 100
    var x: Int = 0
    var y: Int = 0
    private var positionMethod: String = "single"

    init {
        checkImages(image, watermarkImage)
        usingTransparencyColor = checkUsingOfTransparencyColor(watermarkImage)
        usingAlpha = if (!usingTransparencyColor) checkUsingOfAlpha(watermarkImage) else false
        transparencyColor = if (usingTransparencyColor) inputTransparencyColor() else Color.BLUE
        transparency = readTransparencyPercentage()
        positionMethod = readPositionMethod()
        if (positionMethod == "single") readPosition(this)

    }

    fun processing(): BufferedImage {
        return blendingImages(
            image,
            watermarkImage,
            transparency,
            usingAlpha,
            usingTransparencyColor,
            transparencyColor,
            positionMethod == "single",
            x,
            y
        )
    }
}