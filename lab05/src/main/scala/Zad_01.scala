package lab05

def isOrdered[A](leq: (A, A) => Boolean)(l: List[A]): Boolean = {
    @annotation.tailrec
    def sprawdź(l: List[A],akum: List[A]=Nil): Boolean = l match {
      case Nil => true
      case head :: tail => akum match {
        case Nil => sprawdź(tail,head :: akum)
        case głowa :: ogon if (leq(głowa,head)) => sprawdź(tail,head :: akum)
        case głowa :: ogon => false  
      }
    }
    sprawdź(l)
}

@main def zadanie_01: Unit = {
  // Program powinien umożliwić „sprawdzenie” działania
  // funkcji „isOrdered”.
  val lt = (m: Int, n: Int) => m < n
  val lte = (m: Int, n: Int) => m <= n  
  val lista = List(1, 2, 2, 5)
  println(isOrdered(lt)(lista)) // ==> false
  println(isOrdered(lte)(lista)) // ==> true
}

