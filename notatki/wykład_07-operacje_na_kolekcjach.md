# Poznane operacje na kolekcjach

Na wykładach 4 i 5 mówiliśmy o „kolekcjach” `CC[A]`. Poniżej, nieco uzupełniona, lista poznanych operacji na nich:

- rozmiar kolekcji

```scala
isEmpty: Boolean
nonEmpty: Boolean
size: Int
knownSize: Int
```

- testowanie elementów z ewentualnym wyborem

```scala
exists (p: A ⇒ Boolean): Boolean
forall (p: A ⇒ Boolean): Boolean
count (p: A ⇒ Boolean): Int
find (p: A ⇒ Boolean): Option[A]
```

- przekształcanie kolekcji

```scala
map[B] (f: A ⇒ B): CC[B]
flatMap[B] (f: A ⇒ IterableOnce[B]): CC[B]
collect[B] (pf: PartialFunction[A, B]): CC[B]
collectFirst (pf: PartialFunction[A, B]): Option[B]
```

- „zwijanie” i „grupowanie” kolekcji

```scala
foldLeft[B] (z: B)(op: (B, A) ⇒ B): B
foldRight[B] (z: B)(op: (A, B) ⇒ B): B
groupBy[K](key: A => K): Map[K, CC[A]]
groupMap[K, B] (key: A ⇒ K)(f: A ⇒ B): Map[K, CC[B]]
groupMapReduce[K, B] (key: A ⇒ K)(f: A ⇒ B)(op: (B, B) ⇒ B): Map[K, B]
reduce[B >: A](op: (B, B) => B): B
```

- dostęp do podkolekcji

```scala
filter (p: A ⇒ Boolean): CC[A]
filterNot (p: A ⇒ Boolean): CC[A]
take (n: Int): CC[A]
takeWhile (p: A ⇒ Boolean): CC[A]
drop (n: Int): CC[A]
dropWhile (p: A ⇒ Boolean): CC[A]
slice (from: Int, until: Int): CC[A]
```

- kombinacje numeryczne

```scala
sum[B >: A] (implicit math.Numeric[B]): B
product[B >: A] (implicit math.Numeric[B]): B
```

- operacje wykorzytujące „uporządkowanie” kolekcji

```scala
min[B >: A] (implicit math.Ordering[B]): A
minOption[B >: A] (implicit math.Ordering[B]): Option[A]
minBy[B] (f: A ⇒ B) (implicit math.Ordering[B]): A
minByOption[B] (f: A ⇒ B) (implicit math.Ordering[B]): Option[A]
max[B >: A] (implicit math.Ordering[B]): A
maxOption[B >: A] (implicit math.Ordering[B]): Option[A]
maxBy[B] (f: A ⇒ B) (implicit math.Ordering[B]): A
maxByOption[B] (f: A ⇒ B) (implicit math.Ordering[B]): Option[A]
```

- „spinanie” i „rozpinanie” kolekcji

```scala
zipWithIndex: CC[(A, Int)]
zip[B] (that: Iterable[B]): CC[(A, B)]
zipAll[B] (that: Iterable[B], thisElem: A, thatElem: B): CC[(A, B)]
unzip[A1, A2]: (implicit asPair: A ⇒ (A1, A2)): (CC[A1], CC[A2])
unzip3[A1, A2, A3]: (implicit asTriple: A ⇒ (A1, A2, A3)): (CC[A1], CC[A2], CC[A3])
```

- konwersje pomiędzy kolekcjami

```scala
toSeq/toList, toSet, toMap, …
```

- dodawanie/łączenie kolekcji

```scala
concat[B >: A] (suffix: IterableOnce[A]): CC[B]
++ /// to samo co concat
```

- dostęp do „wybranych” elementów

```scala
head, last: A
headOption, lastOption: Option[A]
```

- dostęp do wybranych „podkolekcji”

```scala
init: CC[A]
inits: Iterator[CC[A]]
tail: CC[A]
tails: Iterator[CC[A]]
takeRight (n: Int): CC[A]
dropRight (n: Int): CC[A]
```

## Operacje specyficzne dla odwzorowań `Map[K,V]`

- dostęp do kluczy/wartości

```scala
// „Zastosowanie” odwzorowania do klucza „key” (czy wykorzystanie operacji „apply”)
// zapisujemy jako: odwzorowanie(key)
apply (key: K): V
get (key: K): Option[V]
getOrElse (key: K, default: ⇒ V): V
isDefinedAtt (key: K): Boolean       // (alternatywna nazwa `contains`)
```

- tworzenie nowych kolekcji na podstawie odwzorowania

```scala
keys: Iterable[K]
keySet: Set[K]
values: Iterable[V]
```

- dodawanie/usuwanie elementów

```scala
+ (kvs: (K, V)*): Map[K, V]
- (key: K, keys: K*): Map[K, V]
```

### Operacje specyficzne „widoków” odwzorowań – `MapView[A]`

```scala
// Dla dowolnego odwzorowania
val m: Map[K, V] = ???
// możemy stworzyć jego „widok”
val widok: MapView[K, V] = m.view
// na którym możemy wykonywać (nawet wielokrotnie) operacje
widok.filterKeys (p: K ⇒ Boolean): MapView[K, V]
widok.mapValues[W] (f: V ⇒ W): MapView[K, W]
// jak widać w efekcie otrzymujemy ponownie widok, czyli
// użycie metod filterKeys oraz mapValues możemy kontynuować.
// Aby uzyskać odzworowanie wystarczy na koniec użyć metody
// ….toMap
```

## Operacje specyficzne dla zbiorów – `Set[A]`

- należenie, podzbiór

```scala
  contains (elem: A): Boolean
  subsetOf (that: Set[A]): Boolean
```

- operacje na zbiorach

```scala
  intersect (that: Set[A]): Set[A] // (alternatywna nazwa: &)
  union (that: Set[A]): Set[A]     // (alternatywna nazwa: |)  UWAGA: podobne do `concat` (++)
  diff (that: Set[A]): Set[A]      // (alternatywna nazwa: &~)
```

## Dokumentacj/API kolekcji

- Operacje „wspólne” – czyli na kolekcjach „iterowalnych”: [Iterable](https://scala-lang.org/api/3.x/scala/collection/Iterable.html)
- Operacje na odwzorowaniach: [Map](https://scala-lang.org/api/3.x/scala/collection/Map.html)
  - Operacje na „widokach” odwzorowań [MapView](https://scala-lang.org/api/3.x/scala/collection/MapView.html)
- Operacje na zbiorach: [Set](https://scala-lang.org/api/3.x/scala/collection/Set.html)

__Uwaga__: W przypadku operacji dla `Map`, `MapView` oraz `Set` warto ukryć sobie operacje „dziedziczone”. W tym celu w rozwijalnym menu „Inherited”, znajdującym się pod „Members list”, należy wybrać „Not inherited”.
