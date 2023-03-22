# Laboratorium 05

## Uwagi

- W rozwiązaniach poniższych zadań nie korzystaj z „pętli”, ani ze __zmiennych__.
- W rozwiązaniach zadań __05.02__ oraz __05.03__ nie korzystaj z rekurencji.
- Tam, gdzie to przydatne pamiętaj o wykorzystaniu „dopasowania wzorca”.

## Zadanie 05.01

Zdefiniuj generyczną funkcję rekurencyjną:

```scala
def isOrdered[A](leq: (A, A) => Boolean)(l: List[A]): Boolean = {
  true
}
```

która, dla zadanego porządku `leq`, sprawdzi czy elementy listy `l` ułożone są zgodnie z nim. W definicji użyj rekurencji ogonowej.

__Przykład__:

```scala
val lt = (m: Int, n: Int) => m < n
val lte = (m: Int, n: Int) => m <= n
val lista = List(1, 2, 2, 5)
isOrdered(lt)(lista) // ==> false
isOrdered(gte)(lista) // ==> true
```

## Zadanie 05.02

Korzystając z metody `foldLeft` zdefiniuj generyczną funkcję

```scala
def deStutter[A](list: List[A]): List[A] = {
  Nil
}
```

„kompresującą” na liscie `list` wszystkie powtarzające się podciągi.

__Przykład__:

```scala
val l = List(1, 1, 2, 4, 4, 4, 1, 3)
assert( deStutter(l) == List(1, 2, 4, 1, 3) ) // ==> true
```

## Zadanie 05.03

Wykorzystując operacje `map` oraz `flatMap` zdefiniuj funkcję

```scala
def chessboard: String = { 
  "aqq"
}
```

która zwraca napis następującej postaci:

```sh
(a,8) (b,8) (c,8) (d,8) (e,8) (f,8) (g,8) (h,8)
(a,7) (b,7) (c,7) (d,7) (e,7) (f,7) (g,7) (h,7)
(a,6) (b,6) (c,6) (d,6) (e,6) (f,6) (g,6) (h,6)
(a,5) (b,5) (c,5) (d,5) (e,5) (f,5) (g,5) (h,5)
(a,4) (b,4) (c,4) (d,4) (e,4) (f,4) (g,4) (h,4)
(a,3) (b,3) (c,3) (d,3) (e,3) (f,3) (g,3) (h,3)
(a,2) (b,2) (c,2) (d,2) (e,2) (f,2) (g,2) (h,2)
(a,1) (b,1) (c,1) (d,1) (e,1) (f,1) (g,1) (h,1)
```

reprezentujący „pozycje” na szachownicy.

W rozwiązaniu nie używaj wyliczenia `for`.
