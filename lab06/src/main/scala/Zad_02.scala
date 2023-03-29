package lab06

def freqMax[A](list: List[A]): /*(Set[A],Int)*/Any = {
  // (Set(), 0)
  val ustaw = list.groupBy(a => a).toList.map((a,b) => (a,b.length))
  val maxWystapien = ustaw.maxBy((a,b) => b)._2 //3
  val helper = ustaw.filter((a,b) => b == maxWystapien).map((a,b) => a).toSet
  val res = (helper,maxWystapien)
  res
}

@main def zadanie_02: Unit = {
  val l = List(1, 1, 2, 4, 4, 3, 4, 1, 3)
  assert( freqMax(l) == (Set(1,4), 3) ) // ==> true
  println(freqMax(l))
}
