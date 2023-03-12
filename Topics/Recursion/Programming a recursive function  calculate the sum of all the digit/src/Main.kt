@SuppressWarnings("MagicNumber")
fun digitSum(n: Int): Int {
    if (n < 10) return n
    return n % 10 + digitSum(n / 10)
}