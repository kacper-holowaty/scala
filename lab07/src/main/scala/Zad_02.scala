package lab07

def indeksy[A](lista: List[A], el: A): Set[Int] = {
  val res = lista.zipWithIndex.filter((a,b) => a == el).map((a,b) => b).toSet
  res
}

@main def zad_02: Unit = {
  val lista = List(1, 2, 1, 1, 5)
  assert( indeksy(lista, 1) == Set(0, 2, 3) ) // ==> true
  assert( indeksy(lista, 7) == Set() ) 
  println(indeksy(lista, 1)) 
}
