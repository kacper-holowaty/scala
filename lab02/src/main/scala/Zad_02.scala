package lab02

/*
  Funkcja „obramuj” „zdefiniowana” poniżej wykorzystuje dwa parametry
    - „napis” potencjalnie może mieć kilka linijek (patrz przykład)
    - „znak”, z którego należy zbudować ramkę
*/
def obramuj(napis: String, znak: Char): String = {
  
  val linie = napis.split("\n")
  val maxDlugosc = linie.map(_.length).max + 4 
  val ramka = znak.toString * maxDlugosc
  val result = linie.map(line => s"$znak ${line.padTo(maxDlugosc - 4, ' ')} $znak") // padTo => przykład użycia: 
                                                                                    // val a = List(1,2,3) val b = a.padTo(5,"c")
                                                                                    // wynik: 
                                                                                    // b = List(1,2,3,"c","c")
  s"$ramka\n${result.mkString("\n")}\n$ramka"
}

@main def zad_02: Unit = {
  val wynik = obramuj("ala\nma\nkota", '*')
  println(wynik)
  /*
    Efektem powino być coś podobnego do:

    ********
    * ala  *
    * ma   *
    * kota *
    ********

  */
}
