@SuppressWarnings("MagicNumber")
fun printIfPrime(number: Int) {
    val flag = if (number == 1 || number.toBigInteger().isProbablePrime(100)) "" else " not"
    val i = "$number is$flag a prime number."
    println(i)
}

fun main(args: Array<String>) {
    val number = readln().toInt()
    printIfPrime(number)
}