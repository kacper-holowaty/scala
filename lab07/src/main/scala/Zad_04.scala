package lab07

def przestaw[A](l: List[A]): List[A]= {
  val parzyste = l.zipWithIndex.filter((elem, index) => index%2 == 0).toList.map((a,b) => a)
  val nieparzyste = l.zipWithIndex.filter((elem, index) => index%2 != 0).toList.map((a,b) => a)
  val zipuj = nieparzyste.zip(parzyste).map(a => a.toList).flatMap(a => a)
  val res = if l.length % 2 == 0 then zipuj else zipuj :+ l.last
  // res  

  //albo
  val alternative = l.grouped(2).toList.flatMap(a => a.reverse)
  alternative

}

@main def zad_04: Unit = {
  val lista = List(1, 2, 3, 4, 5)
  assert( przestaw(List(1, 2, 3, 4, 5)) == List(2, 1, 4, 3, 5) ) // ==> true
  assert( przestaw(List(1)) == List(1) )                      // ==> true
  assert( przestaw(List()) == List() )                        // ==> true
  println(przestaw(lista))
  
}
