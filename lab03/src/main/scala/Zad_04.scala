package lab03

def IntToBin(liczba: Int): Int = {
  // @annotation.tailrec
  def pomocnicza( /*argumenty*/ ): Int = {
    ???
  }
  0
}

@main def zad_04(liczba: Int): Unit = {
  require(liczba >= 0)
  // Napisz funkcję IntToBin, która dla podanej liczby naturalnej
  // zwróci jej reprezentację w systemie binarnym
  val binarna = IntToBin(liczba)
  println(s"$liczba w systemie binarnym jest zapisywana jako $binarna")
}
