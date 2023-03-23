package lab05

def chessboard: /*String*/Any = {
  // val a = (1 to 8).toList
  // val b = a.
  val litery = "abcdefgh"
  val liczby = "12345678".reverse
  val wynik = liczby.flatMap(liczba => litery.map(n => s"($n,$liczba)")).mkString(" ").toString()
  wynik
}

@main def zadanie_03: Unit = {
  // Program powinien umożliwić „sprawdzenie” działania
  // funkcji „szachownica”.
  println(chessboard)
}
