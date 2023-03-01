package lab02

def jestPierwsza(n: Int): Boolean = {
  require(n >= 2)
  // „trzy znaki zapytania” oznaczają wartość
  // (jeszcze) „niezdefiniowaną” – wygodne na
  // wstępnym etapie implementacji, kiedy
  // nie wszystkie elementy programu mamy już
  // „w ręku”…

  def pomocnicza(a: Int, b: Int): Boolean = {
    for {i <- a until b}
      if (b%i==0) {return false}
    true
  }
    pomocnicza(2,n)
}

@main def zad_01: Unit = {
  print("Podaj liczbę naturalną: ")
  val liczba = io.StdIn.readInt()
  val wynik = if jestPierwsza(liczba) then "" else " nie"
  println(s"Liczba $liczba$wynik jest liczbą pierwszą")
}
