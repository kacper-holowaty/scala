package lab06

def difficult[A](list: List[A])(len: Int, shift: Int): List[List[A]] = {
  /*
  // Pierwszy pomysł - nie wiem jak zastosować shift do tego 
  @annotation.tailrec
  def helpMe(list: List[A], akum: List[A], wynik: List[List[A]]=Nil): List[List[A]] = list match {
    case Nil => (akum.reverse :: wynik).reverse
    case head :: tail => 
      if (akum.length == len) helpMe(tail,List(head),akum.reverse :: wynik)
      else helpMe(tail,head :: akum,wynik)
  }
  helpMe(list,Nil)
  */
  // Funkcja 'last' gdyby operacja last nie była dozwolona
  // def last[A](l: List[A]): A = l match {
  //   case head :: Nil => head
  //   case _ :: tail => last(tail)
  //   case _ => throw new NoSuchElementException("Lista jest pusta")      
  // }
  // Funkcja 'niePusta', gdyby operacja nonEmpty nie była dozwolona
  // def niePusta(l: List[A]): Boolean = l match {
  //   case Nil => false
  //   case _ => true
  // }

  @annotation.tailrec
  def take(list: List[A], akum: List[A], n: Int, licznik: Int = 0): List[A] = list match {
    case Nil => akum.reverse
    case head :: tail if licznik == n => akum.reverse 
    case head :: tail => take(tail, head :: akum, n, licznik + 1)
  }

  @annotation.tailrec
  def drop(list: List[A], n: Int, licznik: Int = 0): List[A] = list match {
    case Nil => list
    case head :: tail if licznik == n => list
    case head :: tail => drop(tail,n,licznik+1)
  }

  @annotation.tailrec
  def helper(list: List[A],akum: List[List[A]]=Nil): List[List[A]] = list match {
    case Nil => akum.reverse
    case _ => 
      val wez = take(list,Nil,len)
      if (wez.nonEmpty && wez.last == list.last) {
        (wez :: akum).reverse
      } else {
        val przesuniecie = drop(list,shift)
        helper(przesuniecie, wez :: akum) 
      }
  }
  helper(list)
}


@main def zadanie_03: Unit = {
  val (list, len, shift) = ( List(1,2,3,4,5), 3, 1 )
  println(difficult(list)(len, shift) == List(List(1,2,3), List(2,3,4), List(3,4,5))) // => true
  println(difficult(list)(len,shift))

  val (list2, len2, shift2) = ( List(1,2,3,4,5), 2, 2 )
  println(difficult(list2)(len2, shift2) == List(List(1,2), List(3,4), List(5)))
  println(difficult(list2)(len2,shift2)) 
}
