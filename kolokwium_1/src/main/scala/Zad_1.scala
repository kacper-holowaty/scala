def wystąpienia(arg: List[Int]): List[(Int,Int)] = {

  @annotation.tailrec
  def reverse[A](l: List[A], akum: List[A]=Nil): List[A] = l match{
    case Nil => akum
    case head :: tail => reverse(tail,head :: akum)
  }

  def contains[A](list: List[A], elem: A): Boolean = {
    @annotation.tailrec
    def helper(l: List[A], found: Boolean): Boolean = l match {
      case Nil => found 
      case head :: Nil if head == elem => true 
      case head :: tail if head == elem => true
      case head :: tail => helper(tail, found)
    }
    helper(list, false)
  }
  @annotation.tailrec
  def indexOf[A](list: List[A], el: A, index: Int = 0): Int = list match {
    case head :: tail if head == el => index
    case head :: tail => indexOf(tail,el,index+1)
    case _ => 100 
  } 

  def updated[A](list: List[A], index: Int, newValue: A): List[A] = {
    @annotation.tailrec
    def helper(l: List[A], i: Int, akum: List[A]): List[A] = l match {
      case Nil => reverse(akum) 
      case head :: tail if i == 0 => helper(tail, i - 1, newValue :: akum)
      case head :: tail => helper(tail, i - 1, head :: akum)
    }
    helper(list, index, Nil)
  }

  @annotation.tailrec
  def helper(arg: List[Int], akum: List[(Int,Int)] = Nil, licznik: Int = 0): List[(Int,Int)] = arg match {
    case Nil => reverse(akum)
    case head :: tail => akum match {
      case Nil => helper(tail,(head,1) :: akum,licznik+1)
      case glowa :: ogon if contains(akum,head) => helper(tail,updated(akum,indexOf(akum,glowa),(head,licznik+1)),licznik+1) 
      case glowa :: ogon => helper(tail,(head, 1) :: akum, licznik+1)
    } 
  }
  helper(arg)
}
/*
  Poprawność rozwiązania należy testować (z poziomu SBT) poleceniem:
  testOnly Test1
*/


@main def zad_1: Unit = {
  // „program główny” ma znaczenie czysto pomocnicze
  val arg = List(1,2,3,3,2,1)
  val wyn = wystąpienia(arg)
  println(wyn)
}
