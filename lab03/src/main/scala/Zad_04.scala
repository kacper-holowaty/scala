package lab03

def IntToBin(liczba: Int): Int = {
  @annotation.tailrec
  def pomocnicza(liczba: Int, akum: Int=0, potęga: Int=1): Int = {
    if (liczba==0) akum
    else pomocnicza(liczba/2, akum+(liczba%2)*potęga, potęga * 10)
  }
  pomocnicza(liczba)
}

@main def zad_04(liczba: Int): Unit = {
  require(liczba >= 0)
  // Napisz funkcję IntToBin, która dla podanej liczby naturalnej
  // zwróci jej reprezentację w systemie binarnym
  val binarna = IntToBin(liczba)
  println(s"$liczba w systemie binarnym jest zapisywana jako $binarna")
}
