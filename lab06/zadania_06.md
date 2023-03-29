# Laboratorium 6

## Uwaga

- W zadaniach __06.01__ oraz __06.02__ nie korzystaj z rekurencji.

## Zadanie 06.01

Korzystając z „kolekcyjnych” metod `drop` i `take` zdefiniuj generyczną funkcję:

```scala
def subseq[A](list: List[A], begIdx: Int, endIdx: Int): List[A] = {
  Nil
}
```

W swoim rozwiązniu __nie korzystaj__ z rekurencji!

Dla dowolnej listy `l: List[A]`

```scala
l.take: Int => List[A]
l.drop: Int => List[A]
```

__Przykład__:

```scala
val l = List(1,2,3,4,5)
l.take(1)  // List(1)
l.take(3)  // List(1,2,3)
l.take(0)  // List()
l.take(10) // l

l.drop(1)  // List(2,3,4,5)
l.drop(3)  // List(4,5)
l.drop(0)  // l
l.drop(10) // List()
```

## Zadanie 23 06.02

Używając poznanych na wykładzie metod przetwarzania kolekcji (`groupBy` ?) zdefiniuj funkcję:

```scala
def freqMax[A](list: List[A]): (Set[A],Int) = {
  (Set(), 0)
}
```

która dla `list` zwraca parę zawierającą zbiór elementów, których liczba wystapień w `list` jest maksymalna oraz – jako drugi element pary – tę liczbę.

__Przykład__:

```scala
val l = List(1, 1, 2, 4, 4, 3, 4, 1, 3)
assert( freqMax(l) == (Set(1,4), 3) ) // ==> true
```

## Zadanie 06.03

Stosując rekurencję ogonową zdefiniuj generyczną funkcję

```scala
def difficult[A](list: List[A])(len: Int, shift: Int = 1): List[List[A]] = {
  Nil
}
```

która przekształca argument list grupując jego elementy w „podlisty” o (maksymalnej) długości `len`, stosując przy tym „przesunięcie” określone przez wartość parametru `shift`.

__Przykład 1__:

```scala
val ( list, len, shift ) = ( List(1,2,3,4,5), 3, 1 )
difficult(list)(len, shift) == List(List(1,2,3), List(2,3,4), List(3,4,5)) // => true
```

__Przykład 2__:

```scala
val ( list, len, shift ) = ( List(1,2,3,4,5), 2, 2 )
difficult(list)(len, shift) == List(List(1,2), List(3,4), List(5))         // => true
```

W definicji wykorzystaj dopasowanie wzorca (pattern matching). Nie używaj: zmiennych, „pętli” (`foreach`, `while`), wyliczenia `for/yield`,  ani gotowych metod operujących na kolekcjach. Jeśli okaże się to potrzebne/pomocne, możesz skorzystać z metody `reverse` oraz operacji dołączania elementu na początek listy (`el :: lista`)
