package wyk_06

@main def kolekcje: Unit = {
  // Set vs HashSet
  Set(1,2,3,4)
  Set(1,2,3,4,5)
  Map(1 -> "ala", (2, "ma"), 3 -> "kota")
  Seq(1,1,1)
  val s: Seq[Int] = Vector(1,2,3)
  Set(1,1,1)
  Set(1,2,3).sum
  Seq(1,2,3).sum
  Seq(1,2,3).product
  Seq(2,2,3).product
  Set(2,2,3).product
  Seq(2,4,3).product
  val w1: Option[Int] = Some(997)
  val w2: Option[Int] = None
  // val w2: Option[Int] = 123 // oczywiście jest to błąd

  // zbiór.min // nie zadziała bo typ „Osoba” nie ma zdefiniowanego
  // porządku. Żeby temu zaradzić możemy odwółać się do innego typu,
  // który taki porządek ma juz zdefiniowany:

  // Zdefiniujmy sobiue „typ” Osoba
  case class Osoba(imię: String, nazwisko: String, wiek: Int)

  val (ola, ala, janek, tomek, irena, bolek) = (
    Osoba("Ola", "Kowalska", 19),
    Osoba("Ala", "Kowalska", 18),
    Osoba("Janek", "Kowalski", 18),
    Osoba("Tomek", "Kowalski", 18),
    Osoba("Irena", "Kowalska", 18),
    Osoba("Bolek", "Kowalski", 18)
  )

  val zbiór = Set(ola, ala, janek, tomek, irena, bolek)

  // Zgodnie ze swoim typem wyniku „minBy” zwraca pojedynczy element.
  // Jeśli więc każemy obliczyć wartość wyrażenia:
  zbiór.minBy(osoba => osoba.wiek)
  // to zwrócona zostanie PIERWSZA osoba (w sensie opisanym poniżej)
  // o minimalnej wartości funkcji będącej argumentem minBy.

  // „Spinanie kolekcji”
  Set(1,2,3,4,5).zipWithIndex
  Set(1,2,3,4).zipWithIndex
  Seq(1,2,3,4).zipWithIndex
  Seq(1,2,3,4,5).zipWithIndex
  Set("ola","ala","as").zipWithIndex

  // Konwersje pomiędzy typami kolekcji
  Set("ola","ala","as").toSeq
  val odwzorowanie = Map(1 -> 123, 2 -> 997)
  odwzorowanie.toSet
  // Aby udało się wykonać konwersję toMap, to typ „A” elementów
  // kolekcji CC[A] musi być „postaci” (K,W) dla pewnych typów
  // K (jak „klucz”) i W (jak „wartość”). Zatem oczywiście poniższe
  // wyrażenie zwróci błąd:
  // Set(1,2).toMap

  // Tutaj konwersja się powiedzie
  Set((1,997),(2,123)).toMap
  // Tutaj też, ale jako wartość klucza „2” pojawi się 997 – jako że
  // ostatnia napotkana para postaci „(2, coś)” będzie dla coś == 997
  Set((1,997),(2,123),(2,997)).toMap

  // Konkatenacja kolekcji
  Set(1,2) ++ Set(2,3)   // zwróci Set(...)
  Seq(1,2) ++ Set(2,3)   // zwróci Seq(...)
  Seq(1,2).++(Set(2,3))  // ro samo co wyżej, tylko w brzydzszej notacji

  // „Typ” (a właściwie „cecha”) Matchable
  Set(1,2) ++ Seq("ala","ola") // Set[Matchable]

  // Matchable, bo wartości opisywane przez tę cechę można „matchować”,
  // czyli dopasowywać do wzorców:
  val m1 = 1 match {
    case n if n < 0 => "aqq"
    case n => s"Liczba $n"
  }
  val m2 = 1 match {
    case n if n < 0 => -123
    case n => s"Liczba $n"
  }
}
