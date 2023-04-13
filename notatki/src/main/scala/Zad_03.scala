package lab07

case class Ocena(
    imię: String,
    nazwisko: String,
    wdzięk: Int,
    spryt: Int
) {
  require(
    // upewniamy się, że składowe Oceny są sensowne
    imię.trim() != "" &&
      nazwisko.trim() != "" &&
      (0 to 20).contains(wdzięk) &&
      (0 to 20).contains(spryt)
  )
}

case class Wynik(
    miejsce: Int,
    imię: String,
    nazwisko: String,
    średniWdzięk: Double,
    średniSpryt: Double,
    suma: Double
) {
  // upewniamy się, że składowe Wyniku są „sensowne”
  require(
    miejsce >= 0 &&
      imię.trim() != "" &&
      nazwisko.trim() != "" &&
      średniWdzięk >= 0 && średniWdzięk <= 20 &&
      średniSpryt >= 0 && średniSpryt <= 20 &&
      suma == średniWdzięk + średniSpryt
  )
}

val wyniki = List(
  Ocena("Jan", "Kowalski", 19, 17),
  Ocena("Jan", "Kowalski", 18, 18),
  Ocena("Pawel", "Cos", 5, 10),
  Ocena("Pawel", "Cos", 20, 20),
  Ocena("Pawel", "Cos", 5, 10),
  Ocena("Adam", "Malysz", 20, 20),
  Ocena("Adam", "Malysz", 20, 20),
  Ocena("Adam", "Malysz", 20, 20),
  Ocena("Nicola", "Cosiek", 5, 10),
  Ocena("Nicola", "Cosiek", 20, 20),
  Ocena("Nicola", "Cosiek", 5, 10)
)

@main def zad_03: Unit = {
  val result = wyniki
    .groupBy({
      case Ocena(x, y, _, _) => (x, y)
    })
    .toList
    .map((x, y) =>
      (
        x,
        y.map({
          case Ocena(_, _, z, c) => (z, c)
        })
      )
    )
    .map((x, y) => (x, y.map((z, c) => (z.toDouble / y.length, c.toDouble / y.length))))
    .map((x, y) =>
      (
        x,
        y.foldLeft((0.0, 0.0))((acc, curr) => {
          (acc._1 + curr._1, acc._2 + curr._2)
        })
      )
    )
    .map((x, y) => (x, (y._1, y._2, y._1 + y._2)))
    .map(x => Wynik(1, x._1._1, x._1._2, x._2._1, x._2._2, x._2._3))
    .sortBy(x => x.imię) // działa
  // .sortBy(x => x.suma) // nie działa

  println(result)
}

// Uwaga! Poniższy przykład to jedynie „inspiracja”
// case class O(imię: String, wynik: Int)
//
// val wyniki = List(O("Ola",3), O("Ala",1), O("Zosia",2))
// val klasyfikacja = wyniki.sortBy(o => o match {
// case O(_, w) => w
// })
// println(klasyfikacja)

// .map((x,y) => (x, y.foldLeft((0,0))((acc, curr) => {
//   (acc._1+curr._1, acc._2+curr._2)
// })))

// .map(x => Wynik(1,x._1._1,x._1._2,x._2._1,x._2._2,x._2._3))

@main def alternatywa: Unit = {

  val klasyfikacja = wyniki
    .groupBy({
      case Ocena(imię, nazwisko, _, _) => (imię, nazwisko)
    })
    .view
    .mapValues(_.map({
      case Ocena(_, _, wdzięk, spryt) => (wdzięk, spryt)
    }))
    .mapValues(_.foldLeft((0.0, 0.0, 0))({
      case ((sumaW, sumaS, liczba), (ocenaW, ocenaS)) =>
        (sumaW + ocenaW, sumaS + ocenaS, liczba + 1)
    }))
    .mapValues({
      case (sumaW, sumaS, liczbaO) => (sumaW / liczbaO, sumaS / liczbaO)
    })
    .toList
    .map({
      case ((imię,nazwisko),(ocenaW, ocenaS)) =>
        (imię, nazwisko, ocenaW, ocenaS, ocenaW + ocenaS)
    })
    .groupBy({
      case (_, _, _, _, suma) => suma
    })
    .toList
    .sortWith({
      case ((suma1, _),(suma2, _)) => suma1 >= suma2
    })


    println(klasyfikacja)
}
  //...
