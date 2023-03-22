package lab05

def deStutter[A](list: List[A]): List[A] = {
    val zwiń = list.foldLeft(List[A]())((akum,elem) => akum match {
      case Nil => elem :: akum
      case head :: tail if head == elem => akum
      case head :: tail => elem :: akum 
    })
    zwiń.reverse
}

@main def zadanie_02: Unit = {
  // Program powinien umożliwić „sprawdzenie” działania
  // funkcji „deStutter”.
  val l = List(1, 1, 2, 4, 4, 4, 1, 3)
  assert(deStutter(l) == List(1, 2, 4, 1, 3)) // ==> true
  println(deStutter(l))
}
