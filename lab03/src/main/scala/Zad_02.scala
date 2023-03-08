package lab03

def hipoteza(liczba: Int): Unit = {
  @annotation.tailrec
  def pomocnicza(liczba: Int, i: Int=2, j: Int=liczba-2): Unit = {
    if (i > j) println("Nie znaleziono takich liczb.")
    if (czyPierwsza(i) && czyPierwsza(j)) {
      if (i+j==liczba) {
        println(s"${i}+${j}==${liczba}")
        pomocnicza(liczba,i+1,liczba-i)
      }
      else {
        pomocnicza(liczba,i+1,liczba-i)
      }
    }
    else {
      pomocnicza(liczba,i+1,liczba-i)
    }
  }
  @annotation.tailrec
  def czyPierwsza(n: Int, i: Int=liczba-1): Boolean = {
    if (i<=2) true
    else {
      if (n%i==0) false
      else {
        czyPierwsza(n,i-1)
      } 
    }
  }
  pomocnicza(liczba)
}

@main def zad_02(liczba: Int): Unit = {
  require(liczba > 2)
  require(liczba % 2 == 0)
  // Zdefiniuj funkcję hipoteza, która jako argument pobiera
  // parzystą liczbę naturalną większą od 2 oraz
  // sprawdza czy jest ona sumą dwóch liczb pierwszych.
  // Jeżeli tak, to funkcja hipoteza powinna wypisać je na
  // konsoli. W przeciwnym wypadku na konsoli powinien pojawić
  // się komunikat mówiący, że liczb takich nie udało sie znaleźć.
  hipoteza(liczba)
}
