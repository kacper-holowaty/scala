package wyk_03

//----------------------------------------------------------------------------------------------
// Listy
//----------------------------------------------------------------------------------------------

val lista = List(12, 23, 3)
/*
  lista == ( [12] -> ( [23] -> ( [3] -> () )) )

              ↑      ↑______________________↑
        „głowa”               „ogon”
 */

val pustaLista = Nil // List()

@main def część_01_listy: Unit = {
  // Poniżej, po lewej stronie równości, występuje „wzorzec”
  // Tutaj reprezentuje „parę uporządkowaną”, czyli element „iloczynu kartezjańskiego”
  // W Scali typ określający iloczyn kartezjański typów T_1, … , T_n zapisujemy (T_1, …, T_n)
  // czyli w przykłądzie poniżej będzie to (Int, Boolean)
  // „Matematycznie, powyższy iloczyn kartezjański zapisalibyśmy jako „Int x Boolean”
  val (intVal, boolVal) = (123, true)

  // Kolejny „wzorzec” pozwala „rozłożyć” listę od razu na dwie części – głowę i ogon
  val głowa :: ogon = lista
  // Uwaga: ogon listy oczywiście jest listą (być może pustą)!

  // Jeśli, zamiast wzorca, chcemy jawnie użyć metody, to ogon możemy uzyskać następująco:
  val ogonOgona = ogon.tail
  println(s"Głowa listy to $głowa, ogon to $ogon, a ogon ogona to $ogonOgona.")

  // Jak radzić sobie z problemem „pustej listy”?
  // Próba obliczenia każdego z poninżej podanych trzech wyrażeń skończy się „wyjątkiem”:

  // Nil.head
  // println(ogonOgona.tail.head)
  // println(ogonOgona.tail.tail)

  // Jeśli nie wiem czy lista jest niepusta, to bezpieczniej zamiast „head” użyć „headOption”
  println(ogonOgona.headOption)
  println(ogonOgona.tail.headOption)

  // UWAGA: W API Scali nie występuje metoda „tailOption”. Co prawda „Nil.tail” zwraca wyjątek,
  // ale zamiast tego możemy bez problemu skorzystać np z operacji/metody „drop”, „odrzucającej”
  // n elementów z początku listy (tutaj użyjemy n == 1):
  val bezpiecznyTail = Nil.drop(1)
  val czyRówne = lista.tail == lista.drop(1) // true
}

//----------------------------------------------------------------------------------------------
// Parametryzacja typem: np. List[Int], Option[Int], List[A], Option[A]
//----------------------------------------------------------------------------------------------
// l: List[Int]  wtedy l.head jest (potencjalnie) typu Int
//                     l.headOption jest wartością typu Option[Int] (albo Some(n) albo None)

//----------------------------------------------------------------------------------------------
// Predykaty (własności) dla liczb całkowitych
//----------------------------------------------------------------------------------------------

// Zdefiniujmy sobie „alias” dla konkretnego typu „funkcyjnego” (typu wszystkich funkcji, których
// argumentami są liczby całkowite, a wynikiem – wartość „true” lub „false”)
type IntPred = Int => Boolean
// Oczywiście taki „alias” możemy wprowadzić również dla innych przypadków
type FunkcjeCałkowita = Int => Int

// Dwa przykłady własności liczb całkowitych
def parzysta: IntPred = (n: Int) => n % 2 == 0
def większaNiżZero: IntPred = n => n > 0

// Zdefiniujmy koniunkcję własności:
def intAnd(p: IntPred, q: IntPred): IntPred = { n =>
  p(n) && q(n)
}

// Używając koniunkcji możemy zdefiniować kolejną własność na podstawie już zdefiniowanych
val nieujemnaParzysta = intAnd(parzysta, większaNiżZero) // : IntPred

@main def część_02_predykaty_na_Int: Unit = {
  // Predykaty/własności działają tak, jak można się tego spodziewać
  val wynik1 = nieujemnaParzysta(4)
  val wynik2 = nieujemnaParzysta(-4)
  println(s"nieujemnaParzysta(4) == $wynik1")
  println(s"nieujemnaParzysta(-4) == $wynik2")
}

//----------------------------------------------------------------------------------------------
// UOGÓLNIENIE: predykaty (własności) dla dowolnego typu „A” (nazwa „parametru”)
//----------------------------------------------------------------------------------------------
type Pred[A] = A => Boolean

def and[A](p: Pred[A], q: Pred[A]): Pred[A] = { a =>
  p(a) && q(a)
}

// Jak łatwo sprawdzić ogólna definicja działa również w przypadku własności z IntPred:
val nieujemnaParzystaInaczej = and(parzysta, większaNiżZero)

@main def część_03_predykaty_ogólnie: Unit = {
  val wynik1 = nieujemnaParzystaInaczej(4)
  val wynik2 = nieujemnaParzystaInaczej(-4)
  println(s"nieujemnaParzystaInaczej(4) == $wynik1")
  println(s"nieujemnaParzystaInaczej(-4) == $wynik2")
}

// Kolejne operacje na listach (na razie znamy: head, tail, headOption)

@main def część_04_list_ciąg_dalszy: Unit = {
  println(lista)
  // sprawdzanie „pustości” listy
  println(lista.isEmpty)
  println(lista.nonEmpty)

  // obliczanie liczby elementów listy
  println(lista.size)

  // działanie na elementach (bez ich zmieniania!!!!!)
  val listaNapisów = List("ala", "ma", "kota")
  val listaWynikowa1 = listaNapisów.map(s => s.length)
  println(listaWynikowa1)

  val listaWynikowa2 = lista.map(n => nieujemnaParzysta(n))
  println(listaWynikowa2)

  // sprawdzanie, czy na liście występuje element podanej
  // własności
  val występujeParzysta1 = lista.exists(parzysta)
  println(występujeParzysta1)

  val występujeParzysta2 = List(1, -1).exists(parzysta)
  println(występujeParzysta2)

  // sprawdzanie, czy wszystkie elementy listy mają podaną
  // własność
  val wszystkieParzyste = lista.forall(parzysta)
  println(wszystkieParzyste)
}

// PRZYKŁAD: Korzystając z rekurencji ogonowej zdefiniujmy sobie
// funkcję obliczającą długość dodwolnej listy:
def długość[A](lista: List[A], dł: Int = 0): Int = lista match {
  case _ :: ogon => długość(ogon, dł + 1)
  case _ => dł
}

@main def część_05_listy_przykład: Unit = {
  val dł1 = długość(lista)
  println(s"długość($lista) == $dł1")
  val listaNapisów = List("ala", "ma", "kota", "i", "psa")
  val dł2 = długość(listaNapisów)
  println(s"długość($listaNapisów) == $dł2")
}
