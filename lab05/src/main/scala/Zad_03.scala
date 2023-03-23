package lab05

def chessboard: String = {
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
