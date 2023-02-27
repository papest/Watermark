import java.awt.Color

fun printARGB() {
    val argb = readln().split(" ").map { it.toInt() }
    for (i in argb) {
        if (i !in 0..255) {
            println("Invalid input")
            return
        }

    }

    val color = Color(argb[1], argb[2], argb[3], argb[0])
    color.getRGB()
    println(color.getRGB().toUInt())

}
