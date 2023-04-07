package lab07

import Ordering.Double.IeeeOrdering

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

@main def zad_03: Unit = {
  
  def obliczSrednia(list: List[Int]): Double = {
    list.sum / list.length
  }
  val lista = List(Ocena("Jan","Kowalski",11,5), Ocena("Jan","Kowalski",16,20), Ocena("Marcin","Kowal",7,15), 
  Ocena("Adrian","Zalewski",11,16), Ocena("Jan","Kowalski",17,11), Ocena("Jan","Kowalski",10,6), Ocena("Dominik","Baran",10, 15),
  Ocena("Igor","Grzyb",11, 16), Ocena("Marcin","Kowal",15,7), Ocena("Adrian","Zalewski",11,6), Ocena("Dominik","Baran",14,20), 
  Ocena("Dominik","Baran",17,7), Ocena("Feliks", "Ostatni", 8, 9), Ocena("Adrian","Kowalski", 13, 14))

  val grupuj = lista.groupBy((elem) => (elem._1,elem._2)).map((first,second)=>second)
  val srednieOcenyWdzieku = grupuj.map(lista => lista.map(elem => elem._3)).map(listaOcen => obliczSrednia(listaOcen))
  val srednieOcenySprytu = grupuj.map(lista => lista.map(elem => elem._4)).map(listaOcen => obliczSrednia(listaOcen))
  val grupujHelp = grupuj.map(lista => lista(0))
  val zipuj3listy = grupujHelp.zip(srednieOcenyWdzieku).zip(srednieOcenySprytu).map({case ((a,b),c) => (a,b,c)})
  val listaWynikow = zipuj3listy.map((elem, n1, n2) => new Wynik(0, elem._1, elem._2, n1, n2, n1+n2)).toList // 0 jako wartość pomocnicza
  val sortowanie = listaWynikow.sortWith((a,b) => {
    if (a.suma == b.suma) {
      if (a.średniWdzięk == b.średniWdzięk) {
        if (a.imię == b.imię) {
          a.nazwisko < b.nazwisko
        }
        else {
          a.imię < b.imię
        }
      }
      else {
      a.średniWdzięk > b.średniWdzięk
      }
    }
    else {
      a.suma > b.suma
    }  
  })
  val posortowaneZindeksem = sortowanie.zipWithIndex.map((elem,index) => Wynik(index,elem._2,elem._3,elem._4, elem._5, elem._6))
  val result = posortowaneZindeksem.foldLeft(List[Wynik]())((akum,elem) => akum match {
    case Nil => Wynik(elem._1 + 1,elem._2,elem._3,elem._4, elem._5, elem._6) :: akum
    case head :: _ if elem.suma == head.suma && elem.średniWdzięk == head.średniWdzięk => 
      Wynik(elem._1 ,elem._2,elem._3,elem._4, elem._5, elem._6) :: akum
    case head :: _ => Wynik(elem._1 + 1 ,elem._2,elem._3,elem._4, elem._5, elem._6) :: akum
  }).reverse
  println(result)
}
