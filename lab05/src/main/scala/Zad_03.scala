package lab05

<<<<<<< HEAD
def chessboard: /*String*/Any = {
  // val a = (1 to 8).toList
  // val b = a.
  val a = List('a','b','c','d','e','f','g','h')
  val b = a.map(el => (el,8))
  b
  //to be continued ...
=======
def chessboard: String = {
  val litery = "abcdefgh"
  val liczby = "12345678".reverse
  val wynik = liczby.flatMap(liczba => litery.map(n => s"($n,$liczba)")).mkString(" ")
  wynik
>>>>>>> 931e32e57471c9937e01b22bc1ef6adbe41a9284
}

@main def zadanie_03: Unit = {
  // Program powinien umożliwić „sprawdzenie” działania
  // funkcji „szachownica”.
  println(chessboard)
}
