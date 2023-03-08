package lab03


def ciągGeometryczny(n: Int, iloraz: Double, początek: Double): Double = {
  @annotation.tailrec
  def helper(n: Int, akum: Double): Double = n match {
    case 1 => akum
    case _ => helper(n-1,akum*iloraz) 
  }
  helper(n,początek)
}

@main def zad_01(n: Int, iloraz: Double, początek: Double): Unit = {
  require(n >= 0)
  // Zdefiniuj funkcję ciągGeometryczny tak, aby zwracała n-ty wyraz
  // ciągu geometrycznego dla zadanego ilorazu i wyrazu początkowego.
  val wynik = ciągGeometryczny(n, iloraz, początek)
  println(s"a_$n dla ciągu a_n=$początek*($iloraz^n) wynosi:$wynik")
}
