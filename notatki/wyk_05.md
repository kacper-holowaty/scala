# Wykład 05

```scala
val lista_1 = List(1, 2, 3)
lista_1: List[Int] = List(1, 2, 3)

// Przetwarzamy wszystkie elementy ciągu otrzymując nowy ciąg
lista_1.map(n => n * 2)
res53: List[Int] = List(2, 4, 6)

lista_1.map(n => Set(123, n))
res54: List[Set[Int]] = List(Set(123, 1), Set(123, 2), Set(123, 3))

// Operacja flatMap przydaje się gdy „po drodze” uzyskujemy kolekcje, których
// „zawartość” chcemy „wyciągnąć” jako elementy wynikowej kolekcji
lista_1.map(n => Set(n))
res55: List[Set[Int]] = List(Set(1), Set(2), Set(3))

lista_1.flatMap(n => Set(n))
res56: List[Int] = List(1, 2, 3)

lista_1.flatMap(n => Set(123, n))
res57: List[Int] = List(123, 1, 123, 2, 123, 3)

// Operacja foldLeft pozwala „zwijać” zawartość kolekcji do pojedynczej
// wartości, korzystając z „akumulatora” oraz operacji „agregującej”
val wyn_1 = lista_1.foldLeft(0)((m, n) => m + n)
wyn_1: Int = 6

// Akumulator nie musi być tak prostego typu jak powyżej (Int) – może być
// dowolnie skomplikowany. Przykładowo, może to iloczyn kartezjański (Int, String)
val wyn_2 = lista_1.foldLeft((0, ""))((akum, liczba) =>
  akum match {
    case (suma, napis) =>
      (suma + liczba, napis + (s"@[$liczba]->" * liczba))
  }
)
wyn_2: (Int, String) = (6, "@[1]->@[2]->@[2]->@[3]->@[3]->@[3]->")

// Iloczyn kartezjański w Scali nie jest (wyłącznie) binarny
val trójka = ("ala", "tomek", "rysio")
trójka: (String, String, String) = ("ala", "tomek", "rysio")

trójka._2
res61: String = "tomek"

trójka._1
res62: String = "ala"

trójka._3
res63: String = "rysio"

// trójka._4  //BUUUMMMMM

// „Krotki” możemy „rozkładać” wykorzystując dopasowanie wzorców
trójka match { case (a, b, c) => a }
res64: String = "ala"

trójka match { case (a, b, c) => (a, c) }
res65: (String, String) = ("ala", "rysio")

// jeśli nie interesuje nas jakiś element to możemy pominąć nadawanie
// mu nazwy:
trójka match { case (a, b, _) => (b, a) }
res66: (String, String) = ("tomek", "ala")

// foldLeft przetwarza kolekcję „od lewej do prawej”, co w przypadku
// list jest bardzo wydajne. Możemy oczywiście skorzystać z foldRight
// i „przejść” przez listę „od prawej do lewej”, ale taka metoda
// jest znacznie mniej efektywna (bo wymaga każdorazowego„rozkładadnia” listy)
val wyn_3 = lista_1.foldRight((0, ""))((liczba, akum) =>
  akum match {
    case (suma, napis) =>
      (suma + liczba, napis + (s"@[$liczba]->" * liczba))
  }
)
wyn_3: (Int, String) = (6, "@[3]->@[3]->@[3]->@[2]->@[2]->@[1]->")

zbiór.filter(n => n % 2 == 1)
res38: Set[Int] = Set(1, 3)

Map(1 -> "ola", 2 -> "ala", (3, "tomek")).filter(para => para._1 == 1)
res39: Map[Int, String] = Map(1 -> "ola")

Map(1 -> "ola", 2 -> "ala", (3, "tomek")).filter(para => para._1 == 3)
res40: Map[Int, String] = Map(3 -> "tomek")

lista_1.reverse
res41: List[Int] = List(3, 2, 1)

// zbiór.reverse // BUUUUMMMMM – odwracać możemy jedynie ciągi (Seq[A])

Seq(1, 2, 3).reverse
res42: Seq[Int] = List(3, 2, 1)

Vector(1, 2, 3).reverse
res43: Vector[Int] = Vector(3, 2, 1)

val ciąg: Seq[Int] = Vector(1, 2, 3).reverse
ciąg: Seq[Int] = Vector(3, 2, 1)

ciąg(0)
res45: Int = 3

ciąg(2)
res46: Int = 1

ciąg(ciąg.length - 1)
res47: Int = 1

zbiór(1)
res48: Boolean = true

zbiór(7)
res49: Boolean = false

lista_1.reduce((m, n) => m + n)
res50: Int = 6

zbiór.reduce(_ + _)
res51: Int = 6
```
