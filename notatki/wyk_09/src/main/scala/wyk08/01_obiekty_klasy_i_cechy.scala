package wyk08

class Q(l: Int, m: Int) extends Ordered[Q] {
  // pamiętaj ... nie dziel przez zero.
  require(m != 0)

  private val znak = (l * m).sign
  // UWAGA: poniższe, stworzone podczas wykładu, dwie linie nie zawsze działają poprawnie!
  //        Jak słusznie zauważyła pani Ada Welke problem pojawia się np. dla Q(42,-30).
  //
  // val licz = znak * l / nwd(l, m) // val to nie jest VAR :)
  // val mian = znak * m / nwd(l, m)
  //
  // Żeby poprawić kod wystarczy np. działać z wartościami bezwzględnymi:
  private val (aL, aM) = (l.abs, m.abs)
  val licz = znak * aL / nwd(aL, aM)
  val mian = aM / nwd(aL, aM)

  // mnożenie liczb wymiernych
  def *(q: Q): Q = Q(licz * q.licz, mian * q.mian)
  def *(q: Int): Q = Q(licz * q, mian)

  // porównywanie „na piechotę”
  // def <(q: Q): Boolean = ???

  // Zamiast działać „na piechotę” wykorzystamy „cechę” Ordered[A],
  // która wymaga jedynie zaimplementowania funkcji „compare”
  def compare(that: Q): Int = {
    val x = licz * that.mian
    val y = that.licz * mian
    x - y
  }

  // „wygląd” jest najważniejszy
  override def toString(): String =
    if mian == 1 then s"$licz"
    else s"$licz/$mian"

  // równość liczb wymiernych („this.equals(that)”)
  override def equals(that: Any): Boolean = that match {
    case q: Q => licz == q.licz && mian == q.mian
    case _ => false
  }

  // Na użytek „struktur haszowych” potrzebujemy przesłonięcia hashCode
  override def hashCode: Int = licz * 41 + mian * 41 * 41

  // metoda bez której spokojnie moglibyśmy się obyć
  def toJa: String = this.toString()

  @annotation.tailrec
  private def nwd(a: Int, b: Int): Int = {
    if b == 0 then a else nwd(b, a % b)
  }
}
object Q { // obiekt „stowarzyszony” z klasą Q (ang. companion object)
  given Conversion[Int, Q] = i => Q(i, 1)
}

@main def wymierne: Unit = {
  // Aby skorzystać z „niejawnej” konwersji „z Int do Q” BEZ ostrzeżeń ze strony
  // kompilatora „ogłaszamy”, że chcemy używać takich (niejawnych) konwersji.
  import scala.language.implicitConversions

  val jednaDruga = Q(1, 2)
  val drugaJednaDruga = jednaDruga
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
  println(s"Q(42, -30) po „normalizacji”: ${Q(42, -30)}")
  println(s"1/2 * 1/2 = ${Q(1, 2).*(Q(1, 2))}")
  println(s"Wartość „this” z punktu widzenia „jednaDruga”: ${jednaDruga.toJa}")
  println(s"1/2 * 1/2 = ${Q(1, 2) * Q(1, 2)}")
  println(s"Q(1,2) == Q(1,2): ${jednaDruga == Q(1, 2)}")
  println(s"Q(1,2) == Q(2,4): ${jednaDruga == Q(2, 4)}")
  println("-" * 80)
  println("Poprawnie zdefiniowana funkcja hashCode daje sensowne wyniki:")
  val zbiór = Set(Q(1, 2), Q(2, 3), Q(1, 5), Q(2, 7), Q(3, 11))
  println(s"$zbiór contains ${Q(1,2)}: ${zbiór contains Q(1, 2)}")
  println("-" * 80)
  println("Jej wartość zależy jedynie od wartości licznika i mianownika")
  println(s"Q(1, 2).hashCode == ${Q(1, 2).hashCode}")
  println(s"Q(3, 6).hashCode == ${Q(3, 6).hashCode}")
  println("-" * 80)
  println("Liczby całkowite jako liczby wymierne")
  println(s"Q(1, 2) * 3 == ${Q(1, 2) * 3}")
  println(s"Q(1, 2) * 4 == ${Q(1, 2) * 4}")
  println(s"3 * Q(1, 3) == ${3 * Q(1, 3)}")
  println("-" * 80)
  println("Porównywanie liczb wymiernych – „cecha” Ordered[Q]")
  println(s"Q(1, 3) > Q(2, 7): ${Q(1, 3) > Q(2, 7)}")
  println(s"Q(2, 7) <= Q(4, 13): ${Q(2, 7) <= Q(4, 13)}")
}
