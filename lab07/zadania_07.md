# Laboratorium 7

## Zadanie 07.01

Używając metod `filter`, `map` i `zipWithIndex` zdefiniuj funkcję:

```scala
def usuń[A](lista: List[A], k: Int): List[A] = {
  Nil
}
```

usuwającą `k`-ty element listy `lista`.

## Zadanie 07.02

Korzystając z metod oferowanych przez kolekcje zdefiniuj funkcję:

```scala
def indeksy[A](lista: List[A], el: A): Set[Int] = {
  Set()
}
```

zwracającą wszystkie indeksy w liście `lista`, na których znajduje się element `el`.

__Przykłady__:

```scala
val lista = List(1, 2, 1, 1, 5)
assert( indeksy(lista, 1) == Set(0, 2, 3) ) // ==> true
assert( indeksy(lista, 7) == Set() )        // ==> true
```

## Zadanie 07.03

Napisz program, który oblicza wyniki zawodów sportowych w konkurencji, w której zawodnicy oceniani są w dwóch kategoriach:

- wdzięk
- spryt

Ocena „cząstkowa” ma postać:

```scala
case class Ocena(imię: String, nazwisko: String, wdzięk: Int, spryt: Int) {
  require(
    // upewniamy się, że składowe Oceny są sensowne
    imię.trim() != "" &&
    nazwisko.trim() != "" &&
    (0 to 20).contains(wdzięk) &&
    (0 to 20).contains(spryt)
  )
}

// Przykład
Ocena("Jan", "Kowalski", 19, 17) // ocena „cząstkowa” dla Jana Kowalskiego
Ocena("Jan", "Kowalski", 18, 18) // kolejna ocena „cząstkowa” dla Jana Kowalskiego
// ...
```

Załóż, że:

- zawodnicy identyfikowani są poprzez imię i nazwisko
- każdy zawodnik może otrzymać __dowolną__ liczbę ocen „cząstkowych”
- oceny `wdzięk` oraz `spryt` są dowolnymi liczbami całkowitymi z zakresu `[0..20]`.

Ostateczny wynik zawodnika jest to para liczb typu `Double` będących średnimi arytmetycznymi ocen cząstkowych w podanych powyżej „kategoriach”.

„Ranking” ustala się sumując obie „średnie” noty każdego z zawodników - wyższa suma oznacza lepszy wynik.

Jeśli sumy not dwóch zawodników są identyczne, to wyższe miejsce zajmuje ten, którego (średnia) nota za wdzięk była wyższa. Jeśli również noty za wdzięk są identyczne, to zawodnicy zajmuja miejsca ex-aequo.

Załóż, że dane wejściowe programu stanowi lista obiektów reprezentujących oceny „cząstkowe”. Program powinien stworzyć uporządkowaną listę obiektów reprezentujących informacje o:

- miejscu zajętym przez zawodnika
- imieniu i nazwisku zawodnika
- uzyskanym wyniku

```scala
case class Wynik(
  miejsce: Int,
  imię: String,
  nazwisko: String,
  średniWdzięk: Double,
  średniSpryt: Double,
  suma: Double
) {
  // upewniamy się, że składowe Wyniku są „sensowne”
  require(
    miejsce >= 0 &&
    imię.trim() != "" &&
    nazwisko.trim() != "" &&
    średniWdzięk >= 0 && średniWdzięk <= 20 &&
    średniSpryt >= 0 && średniSpryt <= 20 &&
    suma == średniWdzięk + średniSpryt
  )
}
```

W przypadku miejsc ex-aequo kolejność na liście wynikowej powinna być zgodna z porządkiem alfabetycznym __nazwisk__ zawodników.

W rozwiązaniu możesz wykorzystywać dowolne niemutowalne kolekcje języka Scala i wszelkie dostępne dla nich metody standardowe.

## Zadanie 07.04

Korzystając z metod oferowanych przez kolekcje zdefiniuj funkcję:

```scala
def przestaw[A](l: List[A]): List[A] = {
  Nil
}
```

zamieniającą kolejnością wartości znajdujące się na parzystych i nieparzystych indeksach.

__Przykłady__:

```scala
val lista = List(1, 2, 3, 4, 5)
assert( przestaw(List(1, 2, 3, 4, 5)) List(2, 1, 4, 3, 5) ) // ==> true
assert( przestaw(List(1)) == List(1) )                      // ==> true
assert( przestaw(List()) == List() )                        // ==> true
```
