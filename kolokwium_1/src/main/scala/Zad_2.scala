//==========================================================================
// Metoda porównuje napisy zgodnie z polskim porządkiem alfabetycznym
// Jedyna zmiana jaka może być tutaj potrzebna to „zamiana komentarzy”
// w linijkach 9 oraz 10.
//--------------------------------------------------------------------------
def ltePL(s1: String, s2: String) = {
  import java.util.Locale
  import java.text.Collator
  val locale = new Locale("pl", "PL") // dla starszych wersji JRE/JDK
  // val locale = Locale.of("pl", "PL") // dla nowszych wersji JRE/JDK
  Collator.getInstance(locale).compare(s1, s2) <= 0
}

// Metoda nie wymaga zmian. Wczytuje dane z pliku i zwraca listę linii
def dane: List[String] = {
  import scala.io.Source
  val plik = Source.fromFile("src/main/resources/machiavelli.txt", "UTF-8")
  plik.getLines.toList
}
//==========================================================================

// Jedyna rzecz do zaimplementowania, czyli metoda „wynik”:
def wynik: List[(String, List[Int])] = {
  val oczysc = dane
    .map(elem => elem.split("\\s+").toList)
    .map(elem => elem.map(a => a.filter(c => c.isLetter)))
    .map(lista => lista.map(char => char.toLowerCase))
    .map(lista => lista.filter(a => a != ""))
    .zipWithIndex.map((a,b) => (a,b+1))
  val grupuj = oczysc.map((lista,index) => lista.groupBy(a => a).toList.map((elem,lista) => ((elem,lista),index)))
    .flatten
    .groupBy(elem => elem._1._1).toList
    .map((elem,rest) => (elem,rest.map((el,index) => index)))
  val sortowanie = grupuj.map((elem,lista) => (elem,lista.sorted.distinct))
    .sortBy((elem,lista) => elem)
  sortowanie
      // działamy z listą zwracaną przez metodę „wynik”
}

/*
  Poprawność rozwiązania należy testować (z poziomu SBT) poleceniem:
  testOnly Test2
*/

@main def zad_2: Unit = {
  // „program główny” ma znaczenie czysto pomocnicze
  // if ltePL("a", "ą") then println("OK")
  // else println("to nie powinno się zdarzyć…")
  println(wynik)
}
