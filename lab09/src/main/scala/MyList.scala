package lab09

sealed trait MyList[+A]
case object Empty extends MyList[Nothing]
case class Cons[+A](head: A, tail: MyList[A]) extends MyList[A]

object MyList {

  def head[A](list: MyList[A]): A = list match {
    case Cons(h, tl) => h
    case _ => throw IllegalArgumentException("Head of an empty MyList")
  }

  // wynik: MyList-a zawierająca wszystkie elementy poza pierwszym
  def tail[A](list: MyList[A]): MyList[A] = list match {
    case Cons(h, tl) => tl
    case _ => throw IllegalArgumentException("Tail of an empty MyList")
  }

  // wynik: długość MyList-y będącej argumentem
  def length[A](list: MyList[A]): Int = {
    @annotation.tailrec
    def dlugosc(list: MyList[A], akum: Int = 0): Int = list match {
      case Cons(head, tail) => dlugosc(tail, akum + 1)
      case _ => akum
    }
    dlugosc(list)
  }

  // wynik: MyList-a zawierająca wszystkie elementy poz n początkowymi
  def drop[A](list: MyList[A], n: Int): MyList[A] = {
    @annotation.tailrec
    def usun(list: MyList[A], n: Int, counter: Int=0): MyList[A] = list match {
      case Cons(head, tail) if counter == n => list
      case Cons(head, tail) => usun(tail, n, counter+1)
      case _ => list
    }
    usun(list,n)
  }

  // wynik: „odwrócony” argument
  def reverse[A](list: MyList[A]): MyList[A] = {
    @annotation.tailrec
    def helper(list: MyList[A], akum: MyList[A] = Empty): MyList[A] = list match {
      case Cons(head, tail) => helper(tail, Cons(head ,akum))
      case _ => akum
    }
    helper(list)
  } 

  // wynik: argument po odrzuceniu początkowych elementów spełniających p
  @annotation.tailrec
  def dropWhile[A](l: MyList[A])(p: A => Boolean): MyList[A] = l match {
    case Cons(head, tail) if p(head) => dropWhile(tail)(p)
    case Cons(head, tail) => l
    case _ => l
  }

  // wynik: połączone MyList-y list1 oraz list2
  def append[A](list1: MyList[A], list2: MyList[A]): MyList[A] = {
    @annotation.tailrec
    def helper(list1: MyList[A], list2: MyList[A], akum: MyList[A] = Empty): MyList[A] = (list1, list2) match {
      case (Cons(head1,tail1), Cons(head2,tail2)) => helper(tail1,list2)
    }
  }

  // wynik: MyList-a składająca się ze wszystkich alementów argumentu, poza ostatnim
  def allButLast[A](list: MyList[A]): MyList[A] = ???

  // wynik: MyList-a złożona z wyników zastosowania funkcji f do elementów list
  def map[A,B](list: MyList[A])(f: A => B): MyList[B] = ??? 

}

@main def listy: Unit = {
  val l1 = Cons(1, Cons(2, Cons(3, Empty)))
  val l2 = Cons(4, Cons(5, Cons(6, Empty)))

  val res = MyList.head(l1)
  println(s"MyList.head($l1) == $res")
  // println(MyList.head(Empty)) // spowoduje „wyjątek”

  val res2 = MyList.tail(l1)
  println(s"MyList.tail($l1) == $res2")

  val res3 = MyList.length(l1)
  println(s"MyList.length($l1) == $res3")

  val res4 = MyList.drop(l1,2)
  println(s"MyList.drop($l1) == $res4")

  val res5 = MyList.reverse(l1)
  println(s"MyList.reverse($l1) == $res5")

  val res6 = MyList.dropWhile(l1)(a => a%2!=0)
  println(s"MyList.dropWhile($l1) == $res6")
}
