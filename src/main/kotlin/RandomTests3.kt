fun main() {
    println(sigma(3))
}
fun sigma(int:Int): Int {
    return (1..int).fold(0,Int::plus)
}