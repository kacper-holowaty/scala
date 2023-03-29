package lab06

def subseq[A](list: List[A], begIdx: Int, endIdx: Int): List[A] = {
  val wytnij = list.take(endIdx+1).drop(begIdx)
  wytnij
}

@main def zadanie_01: Unit = {
  val lista = List(1,2,3,4,5,6)
  println(subseq(lista,1,3))
  println(subseq(lista,7,9))
  println(subseq(lista,0,10))
}
