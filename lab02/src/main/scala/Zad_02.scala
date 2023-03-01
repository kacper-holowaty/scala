package lab02

/*
  Funkcja „obramuj” „zdefiniowana” poniżej wykorzystuje dwa parametry
    - „napis” potencjalnie może mieć kilka linijek (patrz przykład)
    - „znak”, z którego należy zbudować ramkę
*/
def obramuj(napis: String, znak: Char): String = {
  // definiujemy funkcję obramowującą
  "AQQ"
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
