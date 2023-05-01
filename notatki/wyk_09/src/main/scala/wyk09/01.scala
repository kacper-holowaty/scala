package wyk09

class Q(l: Int, m: Int) extends Ordered[Q] {
  // pamiętaj ... nie dziel przez zero.
  require(m != 0)

  private val znak = (l * m).sign
  private val (aL, aM) = (l.abs, m.abs)
  val licz = znak * aL / nwd(aL, aM)
  val mian = aM / nwd(aL, aM)

  // mnożenie liczb wymiernych
  def *(q: Q): Q = Q(licz * q.licz, mian * q.mian)
  def *(q: Int): Q = Q(licz * q, mian)

  def compare(that: Q): Int = {
    val x = licz * that.mian
    val y = that.licz * mian
    x - y
  }
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
  // metoda „statyczna”
  def coś: String = "Ala ma kota"

  // (niejawna) konwersja z typu Int do Q
  given Conversion[Int, Q] = i => Q(i, 1)

  // jeśli chcemy, to możemy „ręcznie” zdefiniować sposoby definiowania liczb Q
  // def apply(l: Int, m: Int) = new Q(l,m)
  // def apply(s: String) = new Q(s.length, 1)

  // Niejako „odwrotną” funkcję pełni metoda pozwalająca do „dopasowywanie wzorców”
  def unapply(q: Q): Option[(Int, Int)] = { // Some((1,2))  LUB None
    Some((q.licz, q.mian))
  }
}

@main def main_00_wymierne: Unit = {
  // Aby skorzystać z „niejawnej” konwersji „z Int do Q” BEZ ostrzeżeń ze strony
  // kompilatora „ogłaszamy”, że chcemy używać takich (niejawnych) konwersji.
  import scala.language.implicitConversions

  val jednaDruga = Q(1, 2)
  Q.coś

  1 * Q(1, 2)

  // Q("12345") // Q(5,1) ===> 5

  jednaDruga match {
    case Q(_, 2) => println("mianownik to 2")
    case _ => println("mianownik rózny od 2")
  }

  Q(2, 6) match {
    case Q(_, 3) => println("mianownik to 3")
    case _ => println("mianownik rózny od 3")
  }

  case class Osoba(imię: String, wiek: Int)

  Osoba("Jaś", 18) match {
    case Osoba(_, 18) => println("Jaś?")
    case _ => println("Niejaś")
  }
}
