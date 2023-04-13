package wyk_05

// Operacje „kolekcyjne” c.d.
// oznaczenie: CC[A] będzie oznaczało kolekcję elementów typu A
@main def kolekcje: Unit = {
  // Ciągi – Seq[A]
  val lista_1 = List(1, 2, 3) // listy to w Scali „domyślny rodzaj ciagów”

  // Przetwarzamy wszystkie elementy ciągu otrzymując nowy ciąg
  lista_1.map(n => n * 2) // List(2, 4, 6)
  lista_1.map(n => Set(123, n)) // List(Set(123, 1), Set(123, 2), Set(123, 3))

  // Operacja flatMap przydaje się gdy „po drodze” uzyskujemy kolekcje, których
  // „zawartość” chcemy „wyciągnąć” jako elementy wynikowej kolekcji
  lista_1.map(n => Set(n)) // List(Set(1), Set(2), Set(3))
  lista_1.flatMap(n => Set(n)) // List(1, 2, 3)
  lista_1.flatMap(n => Set(123, n)) // List(123, 1, 123, 2, 123, 3)

  // Operacja foldLeft pozwala „zwijać” zawartość kolekcji do pojedynczej
  // wartości, korzystając z „akumulatora” oraz operacji „agregującej”
  val wyn_1 = lista_1.foldLeft(0)((m, n) => m + n)

  // Akumulator nie musi być tak prostego typu jak powyżej (Int) – może być
  // dowolnie skomplikowany. Przykładowo, może to iloczyn kartezjański (Int, String)
  val wyn_2 = lista_1.foldLeft((0, ""))((akum, liczba) =>
    akum match {
      case (suma, napis) =>
        (suma + liczba, napis + (s"@[$liczba]->" * liczba))
    }
  ) // (6, "@[1]->@[2]->@[2]->@[3]->@[3]->@[3]->")

  // Iloczyn kartezjański w Scali nie jest (wyłącznie) binarny
  val trójka = ("ala", "tomek", "rysio")
  val el_2 = trójka._2
  val el_1 = trójka._1
  val el_3 = trójka._3
  // trójka._4  //BUUUMMMMM

  // „Krotki” możemy „rozkładać” wykorzystując dopasowanie wzorców
  trójka match { case (a, b, c) => a } // "ala"
  trójka match { case (a, b, c) => (a, c) } // ("ala", "rysio")
  // jeśli nie interesuje nas jakiś element to możemy pominąć nadawanie
  // mu nazwy:
  trójka match { case (a, b, _) => (b, a) } // ("tomek", "ala")

  // foldLeft przetwarza kolekcję „od lewej do prawej”, co w przypadku
  // list jest bardzo wydajne. Możemy oczywiście skorzystać z foldRight
  // i „przejść” przez listę „od prawej do lewej”, ale taka metoda
  // jest znacznie mniej efektywna (bo wymaga każdorazowego„rozkładadnia” listy)
  val wyn_3 = lista_1.foldRight((0, ""))((liczba, akum) =>
    akum match {
      case (suma, napis) =>
        (suma + liczba, napis + (s"@[$liczba]->" * liczba))
    }
  ) // (6, "@[3]->@[3]->@[3]->@[2]->@[2]->@[1]->")

  // Odwzorowania: Map[A,B]
  val lista_2 = List(1, 2, 3, 2)
  // lista_2: List[Int] = List(1, 2, 3, 2)
  val wyn_4 = lista_2.groupBy(n => n)
  // wyn_4: Map[Int, List[Int]] = HashMap(1 List(1), 2 List(2, 2), 3 List(3))
  val wyn_5 = lista_2.groupBy(n => n % 2 == 0)
  // wyn_5: Map[Boolean, List[Int]] = HashMap(false -> List(1, 3), true -> List(2, 2))

  lista_2.filter(n => n % 2 == 1)
  val zbiór = Set(1, 2, 3)
  zbiór.filter(n => n % 2 == 1)
  Map(1 -> "ola", 2 -> "ala", (3, "tomek")).filter(para => para._1 == 1)
  Map(1 -> "ola", 2 -> "ala", (3, "tomek")).filter(para => para._1 == 3)

  // Odwracanie ciągów
  lista_1.reverse
  // zbiór.reverse // BUUUUMMMMM – odwracać możemy jedynie ciągi (Seq[A])
  Seq(1, 2, 3).reverse
  Vector(1, 2, 3).reverse

  // mimo wyboru konkretnej implementacji (tutaj Vector[Int]) możemy postulować
  // przyjęcie ogólniejszego typu (tutaj Seq[Int])
  val ciąg: Seq[Int] = Vector(1, 2, 3).reverse

  // Z ciągu możemy „od razu” wydobyć n-ty element. Oczywiście efektywność
  // tej operacji zależy od wybranej implementacji ciągów.
  ciąg(0)
  ciąg(2)
  ciąg(ciąg.length - 1)

  // Podobnie wyglądająca „operacja” dla zbiorów robi coś zgoła innego… Co?
  zbiór(1)
  zbiór(7)

  // Istnieje jeszcze uproszczona wersja operacji „zwijania” kolekcji.
  // Zakładamy w niej, że do zwijania stosujemy operację binarną „zgodną”
  // z typem elementów kolekcji. Wynik końcowy będzie również „zgodny”
  // z typem elementów
  lista_1.reduce((m, n) => m + n)
  zbiór.reduce(_ + _)
}

// Dopasowanie wzorca może korzystać z faktu, że Seq[A] jest „nadtypem”
// różnych rodzajów ciągów, np. List[A], Vector[A]
def aqq[A](seq: Seq[A]): Unit = seq match {
  case List(jedynyElement) => println(s"Ciąg z element $jedynyElement")
  // case głowa :: _ => println(s"Ciąg z pierwszym elementem $głowa")
  case List(głowa, _*) => println(s"Ciąg z pierwszym elementem $głowa")
  case Vector(1, _, 2, _*) => println("Brawo!!!!")
  case _ => println("coś innego")
}
