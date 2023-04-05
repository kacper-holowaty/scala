package lab07

def przestaw[A](l: List[A]): /*List[A]*/Any= {
  val a = l.zipWithIndex.groupBy((elem,index) => index%2).toList.map((a,b) => b)
  val first = a(0).map(n => n._1)
  val second = a(1).map(n => n._1)
  val zipuj = first.zipAll(second,Nil,Nil).map(a => a.toList).flatten.filter(a => a != Nil)
  zipuj
  
}

@main def zad_04: Unit = {
  val lista = List(1, 2, 3, 4, 5)
  // assert( przestaw(List(1, 2, 3, 4, 5)) == List(2, 1, 4, 3, 5) ) // ==> true
  // assert( przestaw(List(1)) == List(1) )                      // ==> true
  // assert( przestaw(List()) == List() )                        // ==> true
  println(przestaw(List(1, 2, 3, 4, 5)))
  // println(przestaw(List(1)))
}
