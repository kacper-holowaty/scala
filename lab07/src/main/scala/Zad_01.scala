package lab07

def usuń[A](lista: List[A], k: Int): List[A] = {
  val res = lista.zipWithIndex.filter((a,b) => b != k).map((a,b) => a)
  res
}

@main def zad_01: Unit = {
  val lista = List(1,3,6,7,9,1)
  println(usuń(lista, 2))
}
