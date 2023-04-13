package wyk_04

@main def przykłady: Unit = {
  val lista = List(1, 2, 3, 4, 1, 5)

  val test1 = lista.exists(n => n % 2 == 0)
  val test2 = lista.forall(n => n % 2 == 0)
  val test3 = lista.count(n => n % 2 == 0)

  println(s"test1 == $test1")
  println(s"test2 == $test2")
  println(s"test3 == $test3")

  // przekształcanie list
  lista.map(n => "#" * n) // typ wyniku: List[String]

  val napisy = List("Ala", "ma", "kota")
  val znaki = napisy.flatMap(s => s.toSet)
  println(s"znaki == $znaki")

  val funkcjaCzęściowa: PartialFunction[Int, Int] = n =>
    n match {
      case 1 => 100
      case 5 => 500
    }

  val wynik = lista.collect(funkcjaCzęściowa)
  println(s"wynik = $wynik")

  Set(1,2,3).collect(funkcjaCzęściowa)

  // Funkcje „rozwinięte”

  val szyfruj: String => String => String = hasło => tekst => s"zaszyfrowane($tekst)"

  val wynik2 = lista.foldLeft(("",0))((akum, n) => (akum._1 + ("#" * n), akum._2 + 1))

  println(s"wynik2 == $wynik2")

}
