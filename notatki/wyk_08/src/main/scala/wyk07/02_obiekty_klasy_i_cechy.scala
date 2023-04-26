package wyk07

class Q(l: Int, m: Int) {
  // pamiętaj ... nie dziel przez zero.
  require(m != 0)

  val znak = (l * m).sign
  val licz = znak * l / nwd(l, m)
  val mian = znak * m / nwd(l, m)

  // mnożenie liczb wymiernych
  def *(q: Q): Q = Q(licz * q.licz, mian * q.mian)

  // „wygląd” jest najważniejszy
  override def toString(): String = s"$licz/$mian"

  //
  def toJa: String = this.toString()

  @annotation.tailrec
  private def nwd(a: Int, b: Int): Int = {
    if b == 0 then a else nwd(b, a % b)
  }
}

@main def wymierne: Unit = {
  val jednaDruga = Q(1, 2)
  val jednaDrugaBis = Q(2, 4)
  // val jednaDziwna = Q(1,0) // Buuum
  println(s"jednaDruga.toString: ${jednaDruga.toString}")
  println(s"jednaDruga: $jednaDruga")
  println(s"Q(2,4) po „normalizacji”: ${jednaDrugaBis}")
  // println(jednaDruga)
  val minusJednaDruga = Q(-2, 4)
  println(s"Q(-2,4) po „normalizacji”: $minusJednaDruga")
  val minusJednaDrugaBis = Q(2, -4)
  println(s"Q(2,-4) po „normalizacji”: $minusJednaDrugaBis")
  val minusMinuJednaDrugaBis = Q(-2, -4)
  println(s"Q(-2,-4) po „normalizacji”: $minusMinuJednaDrugaBis")
  println(s"1/2 * 1/2 = ${Q(1, 2).*(Q(1, 2))}")
  println(s"Wartość „this” z punktu widzenia „jednaDruga”: ${jednaDruga.toJa}")
}
