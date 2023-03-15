package lab04

def tasuj(l1: List[Int], l2: List[Int]): List[Int] = {
    @annotation.tailrec
    def mix(l1: List[Int], l2: List[Int], akum: List[Int]=Nil): List[Int] = (l1,l2) match {
      case (Nil,Nil) => akum
      case (Nil,head2 :: tail2) => akum match {
        case glowa :: ogon if head2 == glowa => mix(Nil,tail2,akum)
        case _ => mix(l1,tail2,head2 :: akum)
      }
      case (head1 :: tail1, Nil) => akum match {
        case glowa :: ogon if head1 == glowa => mix(tail1,Nil,akum)
        case _ => mix(tail1,l2,head1 :: akum)
      }
      
      case (head1 :: tail1,head2 :: tail2) if head1 <= head2 => akum match { 
        case glowa :: ogon if head1 == glowa => mix(tail1,l2,akum)
        case _ =>  mix(tail1,l2,head1 :: akum)
      }
      case (head1 :: tail1,head2 :: tail2) => akum match {
        case glowa :: ogon if head2 == glowa=> mix(l1,tail2,akum)
        case _ => mix(l1,tail2,head2 :: akum)
      }
    }
    mix(l1,l2).reverse
}

@main def zadanie_02: Unit = {
  // Program powinien umożliwić „sprawdzenie” działania
  // funkcji „tasuj”.
  val lista1 = List(2, 4, 3, 5)
  val lista2 = List(1, 2, 2, 3, 1, 5)
  println(tasuj(lista1,lista2))
  println(tasuj(lista1, lista2) == List(1, 2, 3, 1, 4, 3, 5)) // true
}
