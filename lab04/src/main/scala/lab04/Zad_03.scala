package lab04

def sumuj(l: List[Option[Double]]): Option[Double] = {
    @annotation.tailrec
    def optSum(l: List[Option[Double]], akum: Option[Double]=None): Option[Double] = l match {
      case Nil => akum
      case Some(head) :: tail if head > 0 => akum match {
        case None => optSum(tail,Some(head))
        case Some(x) => optSum(tail,Some(head + x))
      }
      case _ :: tail => optSum(tail,akum)
    }
    optSum(l)
}

@main 
def zadanie_03: Unit = {
  // Program powinien umożliwić „sprawdzenie” działania
  // funkcji „sumuj”.
  val lista = List(Some(4.0), Some(-3.0), None, Some(1.0), Some(0.0))
  val lista2 = List(None,None,None)
  println(sumuj(lista) == Some(5.0)) // true
  println(sumuj(lista))
}
