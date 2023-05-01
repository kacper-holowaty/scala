package wyk09

//==========================================================
// Cechy (ang. traits) w Scali
//==========================================================

trait Filozoficzny {
  def filozofuje: String = "Myślę o niebieskich migdałach"
}

trait Filozoficzny2 {
  def filozofuje: String = "Też myślę o niebieskich migdałach"
  def aqq: Int
}

class A extends Filozoficzny {
  def toJa: String = "To ja – A!"
}

class B extends Filozoficzny2 {
  override def filozofuje: String = "To ja – B!"
  def aqq = 123
}

@main def main_01_cechy: Unit = {
  val a = A()
  println(a.toJa)
  println(a.filozofuje)
}

//==========================================================
// „Składanie“ cech
//==========================================================

trait IntQueue {
  def put(x: Int): Unit
  def get(): Int
  // get() dla pustej kolejki wygeneruje wyjątek, który
  // pochodzi z poziomu implementacji ArrayBuffer.
}

// Przykładowa klasa rozszerzająca/implementująca IntQueue
class BasicIntQueue extends IntQueue {
  import collection.mutable.ArrayBuffer
  private val buf = ArrayBuffer[Int]()
  def put(x: Int) = buf += x
  def get() = buf.remove(0)
  // plus metoda pomocnicza
  def print: Unit = println(buf)
  // czym jest „super” w przypadku klasy, która jawnie
  // nie dziedziczy po żadnej klasie?
  def dziwna: String = super.toString // O....t
}

trait Incrementing extends IntQueue {
  abstract override def put(x: Int): Unit = super.put(x + 1)
}

trait Filtering extends IntQueue {
  abstract override def put(x: Int): Unit = {
    if (x % 2 == 0) super.put(x)
  }
}

class K1 extends BasicIntQueue, Incrementing, Filtering
class K2 extends BasicIntQueue, Filtering, Incrementing

@main def main_02_kolejki_1: Unit = {
  val q = BasicIntQueue()
  q.put(123)
  q.put(-12)
  q.print
  q.get()
  q.print
  q.get()
  q.print
  // q.get() // spowoduje „rzucenie” wyjątku
}

@main def main_03_kolejki_2: Unit = {
  val k1 = K1()
  val k2 = K2()

// class K1 extends BasicIntQueue, Incrementing, Filtering
  println(szlaczek("="))
  println(" class K1 extends BasicIntQueue, Incrementing, Filtering")
  println(szlaczek())
  println(" (1 to 5).foreach(n => k1.put(n))")
  (1 to 5).foreach(n => k1.put(n))
  println(szlaczek())
  print("Wynik: ")
  k1.print  // ArrayBuffer(3, 5)
  println(szlaczek("="))
  println()

// class K2 extends BasicIntQueue, Filtering, Incrementing
  println(szlaczek("="))
  println(" class K2 extends BasicIntQueue, Filtering, Incrementing")
  println(szlaczek())
  println(" (1 to 5).foreach(n => k2.put(n))")
  (1 to 5).foreach(n => k2.put(n))
  println(szlaczek())
  print(" Wynik: ")
  k2.print // ArrayBuffer(2, 4, 6)
  println(szlaczek("="))
  println()

  // Poniżej użyta zostanie metoda toString z poziomu Object
  println(k2.dziwna)
}

// Słowo kluczowe „inline” nie jest niezbędne, ale zainteresowanym polecam
// wyjaśnienie czym ono jest:
//
// https://scalac.io/blog/scala-3-inline-macro-like-superpowers/
//
//
inline def szlaczek(s: String = "-", n: Int = 60): String = s * n
