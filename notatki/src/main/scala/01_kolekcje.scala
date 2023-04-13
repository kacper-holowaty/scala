package wyk07

@main def kolekcje: Unit = {
  val lista = List(1, 2, 3, 4, 5)
  // różne (niekoniecznie sensowne) przykłady „grupowania” elementów listy
  lista.groupBy(n => "aqq") // Map[String, List[Int]]
  lista.groupBy(n => n % 2 == 0) // Map[Boolean, List[Int]]
  lista.groupBy(n => n) // Map[Int, List[Int]]
  lista.groupBy(n => (n, 123)) // Map[(Int,Int), List[Int]]
  lista.groupBy(n => (n, n % 2 == 0)) // Map[(Int,Boolean), List[Int]]

  // najpierw grupowanie, a potem przekształcanie
  lista.groupMap(n => n % 2 == 0)(liczba => "#" * liczba)

  // grupowanie, przekształcanie, a następnie „zwijanie/redukowanie”
  // z użyciem skrótowej notacji „_ + _” dla oznaczenia „konkatenacji” napisów
  lista.groupMapReduce(n => n % 2 == 0)(liczba => "#" * liczba)(_ + _)
  // lub alternatywnie
  lista.groupMapReduce(n => n % 2 == 0)(liczba => "#" * liczba)((s1, s2) => s1 + s2)

  // „spinanie” list
  val wynik = lista.zip(List("a", "b", "c"))
  val powrót = wynik.unzip

  // Metody specyficzne dla Map[(K, V)]
  val odwzorowanie = Map(1 -> "Ala", 2 -> "Ola", 3 -> "Tomek")

  // dodawanie/usuwanie elementów
  odwzorowanie + (7 -> "Romek")
  odwzorowanie ++ Set(7 -> "Romek", 9 -> "Tytus")
  odwzorowanie ++ List(7 -> "Romek", 9 -> "Tytus")
  odwzorowanie - 1

  // Działamy z kluczami oraz wartościami odwzorowań używając „widoków”
  val widok = odwzorowanie.view
    .filterKeys(n => n % 2 == 0)

  val odwzorowanieOdfiltrowane = odwzorowanie.view
    .filterKeys(n => n % 2 == 0)
    .toMap

  println(widok) // dowiemy się, że jest to „nieobliczony” widok
  println(odwzorowanieOdfiltrowane) // tutaj będziemy już mieli Map[Int, String]

  val widok2 = odwzorowanie.view
    .mapValues(str => str.length)

  val odwzorowaniePrzekształcone = odwzorowanie.view
    .mapValues(str => str.length)
    .toMap

  println(widok2) // dowiemy się, że jest to „nieobliczony” widok
  println(odwzorowaniePrzekształcone) // tutaj będziemy już mieli Map[Int, Int]
}
