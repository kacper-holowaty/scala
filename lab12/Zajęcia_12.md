# Laboratorium 12


## Zadanie 12.01

Zdefiniuj klasę

```scala
class Boss extends Actor with ActorLogging {
    // ...
}
```

która z poziomu programu głównego posłuży do stworzenia aktora zlecającego obliczenie zadanego wyrazu ciągu Fibonacciego powołując w tym celu pracownika:

```scala

class Pracownik extends Actor with ActorLogging {
    // ...
}
```

i wysyłając mu komunikat:

```scala
case class Oblicz(liczba : Int)
```

Po otrzymaniu takiego komunikatu pracownik powinien odpowiedzieć komunikatem:

```scala
case class Wynik(wynik : Int)
```

Jeśli dla zadanej liczby można bezpośrednio wyznaczyć wartość. W przeciwnym wypadku powinien powołać dwóch kolejnych pracowników, którzy obliczą wartości ciągu dla `liczba-2` i `liczba-1` a następnie zwrócić ich sumę komunikatem `Wynik` do aktora, który zlecił obliczenia.

Po otrzymaniu wyniku `Boss` powinien wypisać informację o wyniku korzystając z `log.info(...)` i zakończyć działanie programu `context.system.terminate()`,

## Zadanie 12.2

Zdefiniuj klasę

```scala
class Boss extends Actor with ActorLogging { /* ... */ }
```

która z poziomu programu głównego posłuży do stworzenia aktora, który po otrzymaniu komunikatu

```scala
case class Oblicz(n: Int)
```

ma za zadanie wypisać na konsoli wartość `n`-tego wyrazu ciągu Fibonacciego. W tym celu, `Boss` wykorzystuje aktora typu

```scala
class Nadzorca(cache: Map[Int, BigInt] = Map(1 -> 1, 2 -> 1), ... ) extends Actor with ActorLogging { /* ... */ }
```

do którego przesyła kolejne otrzymywane przez siebie komunikaty `Oblicz(k)`

`Nadzorca` otrzymując komunikat `Oblicz(k)` sprawdza, czy ma już w cache'u klucz `k`. Jeśli tak, to odsyła wartości `k` oraz `cache(k)` w komunikacie typu

```scala
case class Wynik(n: Int, fib_n: BigInt)
```

do `Boss`-a. W przeciwnym wypadku `Nadzorca` tworzy aktora typu

```scala
class Pracownik(k: Int) extends Actor with ActorLogging { /* ... */ }
```

którego zadaniem będzie obliczenie `k`-tego elementu ciągu. Do obliczenia wartości elementów o indeksach `(k-1)` oraz `(k-2)`, `Pracownik` korzysta z `Nadzorcy` (w sposób analogiczny jak `Boss`). Następnie oblicza ostateczny wynik `Oblicz(k)`, przesyła go do `Nadzorcy` (w celu zapamiętania w cache'u) i kończy działanie.

__Uwaga__: Uzupełnij typ `Nadzorcy` w taki sposób, żeby mógł on odsyłać odpowiednie wyrazy ciągu aktorom, którzy się o nie zwrócili (oczywiście dopiero wtedy, gdy policzony wyraz ciągu znajdzie się w cache'u)...
