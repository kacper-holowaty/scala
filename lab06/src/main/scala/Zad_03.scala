package lab06

def difficult[A](list: List[A])(len: Int, shift: Int = 1): List[List[A]] = {
  @annotation.tailrec
  def sprawdzDlugosc(l: List[A], akum: List[A]=Nil): List[A] = l match {
    case Nil => akum
    case head :: tail => sprawdzDlugosc(tail,akum :: head)
  }
  @annotation.tailrec
  def helpMe(list: List[A], akum: List[List[A]], dlugosc: Int=0)(len: Int, shift: Int = 1): /*List[List[A]]*/Any = list match {
    case Nil => akum
    case head :: tail => akum match {
      case Nil => akum :: List(head)
      case glowa :: ogon if dlugosc < len => helpMe(tail,sprawdzDlugosc(glowa),dlugosc+1)(len,shift)
      case glowa :: ogon => 
    } 
    
    // helpMe(akum :: head)(len-1,shift)
  }
}

@main def zadanie_03: Unit = {
  val (list, len, shift) = ( List(1,2,3,4,5), 3, 1 )
  difficult(list)(len, shift) == List(List(1,2,3), List(2,3,4), List(3,4,5)) // => true

  val (list, len, shift) = ( List(1,2,3,4,5), 2, 2 )
  difficult(list)(len, shift) == List(List(1,2), List(3,4), List(5)) 
}
