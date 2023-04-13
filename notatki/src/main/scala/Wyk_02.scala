//-----------------------------------------------------------
// Mimo, że typ wyniku funkcji Scala jest w stanie wyliczyć,
// warto go jawnie podawać żeby uniknąć „przopadkowych błędów”
// oraz ułatwić używanie/zrozumienie naszego kodu
//-----------------------------------------------------------
def inc1(n: Int): Unit = {
  n + 1 // : Int
  println(n + 1) // () : Unit
}

//-----------------------------------------------------------
def inc2(n: Int): Int = {
  n + 2
}

//-----------------------------------------------------------
// W przypadku wartości funkcji określonej pojedynczym
// wyrażeniem nie potrzebujemy wyrażenia „blokowego”
//-----------------------------------------------------------
def inc2a(n: Int): Int = n + 2

//-----------------------------------------------------------
// Wartość wyrażenia „blokowego”:
//-----------------------------------------------------------
val dziwne = {
  val x = 123
  x + 1 // : Int
  println(x + 1) // () : Unit
  x // : Int
}

//-----------------------------------------------------------
// Wyrażenie warunkowe i jego wykorzystanie
//-----------------------------------------------------------
val wyr_1 = if (dziwne % 2 == 0) "parzysta" else "nieparzysta"
//-----------------------------------------------------------
// Typ można podać jawnie, ale nie jest to konieczne
//-----------------------------------------------------------
val wyr_2: String = if (dziwne > 100) "OK" else "nieOK"
//-----------------------------------------------------------
// Typ „Any” pasuje, ale jest mało przydatny
//-----------------------------------------------------------
val wyr_3: Any = if (dziwne > 100) "10" else -1

//-----------------------------------------------------------
// Definicja rekurencyjna funkcji
//-----------------------------------------------------------
def silnia(n: Int): Int = {
  if (n <= 0) 1
  else n * silnia(n - 1)
}
/*
  UWAGA! Funkcje zdefiniowane rekurencyjnie wymagają JAWNEGO
  podania typu wyniku!

  def silnia(n: Int) = {
    if (n <= 0) 1
    else n * silnia(n - 1)
  }

  SPOWODUJE pojawienie się następującegpo sygnału błędu:

  else n * silnia(n - 1)
           ^
           Overloaded or recursive method silnia needs return type
*/

//-----------------------------------------------------------
// Podczas wykonania funkcji „silnia” JVM buduje „stos”, na
// którym odkłada dane z poszczególnych „poziomów” wywołań
//-----------------------------------------------------------
/*
  silnia(4) ==>
    4 * silnia(3) ==>
    4 * 3 * silnia(2) ==>
    4 * 3 * 2 * silnia(1) ==>
    4 * 3 * 2 * 1 * silnia(0) ==>
    4 * 3 * 2 * 1 * 1 ==>
    4 * 3 * 2 * 1 ==>
    4 * 3 * 2 ==>
    4 * 6 ==>
    24
*/

//-----------------------------------------------------------
// Kolejna definicja rekurencyjna funkcji
// Uwaga: poprawiona została „usterka” z zerowym elementem
//-----------------------------------------------------------
def fibo(n: Int): Int = {
  if (n <= 0) 0
  else if (n == 1) 1
  else fibo(n - 1) + fibo(n - 2)
}

//-----------------------------------------------------------
// Wartości domyślne parametrów funkcji
//-----------------------------------------------------------
def szlaczek(n: Int = 80): Unit = println("#" * n)

//-----------------------------------------------------------
// Jeśli wartości domyślne określimy dla większej liczby
// parametrów, w wywołaniu może przydać jawne „cytowanie”
// ich nazw.
//-----------------------------------------------------------
def szlaczek2(n: Int = 80, el: String = "#"): Unit = {
  println(el * n)
}

//-----------------------------------------------------------
// Aby uniknąć zjawiska „przepełnienia stosu” wystarczy
// skorzystać z „rekurencji ogonowej”, która nie wymaga jego
// budowania. Wartość wyniku „akumulujemy“ w dodatkowym
// parametrze samej funkcji.
//-----------------------------------------------------------
def lepszaSilnia(n: Int, akumulator: Int = 1): Int = {
  if (n <= 1) akumulator
  else  lepszaSilnia(n - 1, akumulator * n)
}

//-----------------------------------------------------------
// Uwaga! Scala sama (w odróżnieniu od wielu innych języków)
// optymalizuje wykonanie funkcji zdefiniowanych „ogonowo”.
//-----------------------------------------------------------

//-----------------------------------------------------------
// Nie zdążyliśmy o tym powiedzieć, ale tworząc kod funkcji
// definiowanej rekurencyjnie możemy „zapostulować”, że
// wykorzystywać ona będzie schemat „ogonowy”. Wówczas
// kompilator będzie zgłaszał błąd w sytuacji gdyby nasza
// definicja nie odpowiadała schematowi „ogonowemu”.
//-----------------------------------------------------------
@annotation.tailrec
def jeszczeLepszaSilnia(n: Int, akumulator: BigInt = 1): BigInt = {
  if (n <= 1) akumulator
  else  jeszczeLepszaSilnia(n - 1, akumulator * n)
}
/*
  Jeśli użyjemy adnotacji „tailrec” dla funkcji rekurencyjnej,
  która nie jest zdefiniowana „ogonowo”, to otrzymamy sygnał
  błędu – np. próba kompilacji:

  @annotation.tailrec
  def silnia(n: Int): Int = {
      if (n <= 0) 1
      else n * silnia(n - 1)
  }

  spowoduje:

  else n * silnia(n - 1)
           ^^^^^^^^^^^^^
           Cannot rewrite recursive call: it is not in tail position

*/

@main def mainProg: Unit = {
  // val wynik1 = inc1(123)
  // println(wynik1)
  // println(dziwne)


  //---------------------------------------------------------
  // Poniżej, zamiast wyniku otrzymamy sygnał „przepełnienia
  // stosu” i program przerwie działanie
  //---------------------------------------------------------
  // val wynik2 = silnia(100000)
  // println(wynik2)

  szlaczek()

  val wynik3 = fibo(10)
  println(wynik3)

  szlaczek2(el = "@") // jawne „cytowanie nazwy parametru”

  //---------------------------------------------------------
  // Co prawda przekroczymy „zakres” typu Int i jako wynik
  // otrzymamy 0, ale nie nastąpi przepełnienie stosu.
  //---------------------------------------------------------
  val wynik4 = lepszaSilnia(1000000)
  println(wynik4)

  szlaczek()

  //---------------------------------------------------------
  // Dzięki wykorzystaniu typu BigInt możemy obliczać silnię
  // dla dowolnie dużych wartości argumentu. Będzie to trwało
  // zauważalnie dłużej ze względu na „symboliczny” charakter
  // obliczeń wykonywanych na tym typie.
  //---------------------------------------------------------
  val wynik5 = jeszczeLepszaSilnia(100000)
  println(wynik5)

  szlaczek()

}
