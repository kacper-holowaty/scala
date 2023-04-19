# Laboratorium 09

## Zadanie 09.01

Wykorzystując „szkielet” rozwiązania znajdujący się w pliku `src/main/scala/lab09/MyList.scala` zdefiniuj
zestaw operacji na „listach” reprezentowanych przez:

```scala
sealed trait MyList[+A]
case object Empty extends MyList[Nothing]
case class Cons[+A](head: A, tail: MyList[A]) extends MyList[A]
```

Jako przykład „motywujący” znajdziesz definicję operacji:

```scala
def head[A](list: MyList[A]): A = list match {
  case Cons(h, tl) => h
  case _ => throw IllegalArgumentException("Head of the empty MyList")
}
```

### Uwagi

- W swoim rozwiązaniu – podobnie, jak to zrobiono w definicji `tail` – wykorzystaj mechanizm dopasowania wzorca.
- Tam, gdzie wykorzystasz rekurencję, zadbaj, aby była ona „ogonowa”:

  ```scala
  // wynik: długość MyList-y będącej argumentem
  @annotation.tailrec
  def length[A](list: MyList[A]): Int = ???
  ```
