package lab05

def chessboard: /*String*/Any = {
  // val a = (1 to 8).toList
  // val b = a.
  val a = List('a','b','c','d','e','f','g','h')
  val b = a.map(el => (el,8))
  b
  //to be continued ...
}

@main def zadanie_03: Unit = {
  // Program powinien umożliwić „sprawdzenie” działania
  // funkcji „szachownica”.
  println(chessboard)
}
