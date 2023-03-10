package lab03

def hipoteza(liczba: Int): Unit = {
  @annotation.tailrec
  def pomocnicza(liczba: Int, i: Int, j: Int, licznikPrintów: Int=0): Unit = {
    if (i > j && licznikPrintów==0) {
      println("Nie znaleziono takich liczb.")
      sys.exit()
    }
    if (i > j && licznikPrintów!=0) {
      sys.exit()
    }
    if ((czyPierwsza(i,i-1)==true) && (czyPierwsza(j,j-1)==true)) {
      println(s"${i}+${j}==${liczba}")
      pomocnicza(liczba,i+1,j-1,licznikPrintów+1)
    }
    else {
      pomocnicza(liczba,i+1,j-1,licznikPrintów)
    }
    // if (akum == 0) {
    //   println("Nie znaleziono takich liczb.")
    //   sys.exit()
    // }
    // else {
    //   sys.exit()
    // }
  }
  @annotation.tailrec
  def czyPierwsza(n: Int, i: Int): Boolean = {
    if (i<2) true
    else {
      if (n%i==0) false
      else {
        czyPierwsza(n,i-1)
      } 
    }
  }
  pomocnicza(liczba,2,liczba-2)
  // println(czyPierwsza(liczba))
}

@main def zad_02(liczba: Int): Unit = {
  require(liczba > 2)
  require(liczba % 2 == 0)
  // Zdefiniuj funkcję hipoteza, która jako argument pobiera
  // parzystą liczbę naturalną większą od 2 oraz
  // sprawdza czy jest ona sumą dwóch liczb pierwszych.
  // Jeżeli tak, to funkcja hipoteza powinna wypisać je na
  // konsoli. W przeciwnym wypadku na konsoli powinien pojawićs
  // się komunikat mówiący, że liczb takich nie udało sie znaleźć.
  hipoteza(liczba)
}
