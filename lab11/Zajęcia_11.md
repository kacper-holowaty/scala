# Laboratorium 11

__Uwaga__. W rozwiązaniach dzisiejszych zadań wykorzystaj „szablon” projektu wygenerowany za pomocą polecenia

```sh
sbt new wpug/scala3-akka.g8
```

## Zadanie 11.01

Zdefiniuj klasę

```scala
class Gracz01 extends Actor with ActorLogging {
    // ...
}
```

która, z poziomu programu głównego, posłuży do utworzenia dwóch aktorów, rozgrywających między sobą partię „ping-ponga”. Jako „wirtualnej piłeczki” użyj komunikatu

```scala
case object Piłeczka
```

Ponieważ któryś z graczy musi rozpocząć grę, w programie głównym (po utworzeniu obu graczy) prześlij do jednego z nich komunikat typu

```scala
case class Graj01(przeciwnik: ActorRef)
```

zawierający referencję do jego przeciwnika. Pamiętaj, że „tożsamość” aktora można zmieniać używając konstrukcji

```scala
context.become(...)
```

__Uwaga__: Jak zapewne pamiętasz, „szkielet” rozwiązania zadania, stworzyliśmy już podczas ostatniego wykładu.

## Zadanie 11.02

Zmodyfikuj swoje rozwiązanie poprzedniego zadania definiując klasę

```scala
class Gracz02 extends Actor with ActorLogging {
    // ...
}
```

tak, aby rozgrywka składała się z zadanej liczby odbić, podanej w komunikacie typu

```scala
case class Graj02(przeciwnik: ActorRef, maks: Int)
```

Po wykonaniu `maks` odbić program powinien zakończyć działanie korzystając z metody

```scala
context.system.terminate()
```

## Zadanie 11.03

Zmodyfikuj rozwiązanie Zadania 11.01, definiując klasę

```scala
class Gracz03 extends Actor with ActorLogging {
    // ...
}
```

oraz proponując odpowiednią reprezentację komunikatu „inicjującego rozgrywkę”

```scala
case class Graj03( ??? )
```

w taki sposób, aby po utworzeniu trzech aktorów typu `Gracz03` mogli oni grać w „ping-ponga” w trójkącie. Jako „piłeczki” użyj zdefiniowanego już wcześniej obiektu `Piłeczka`.

## Zadanie 11.04a

Zmodyfikuj rozwiązanie zadania poprzedniego, definiując klasę

```scala
class Gracz04a extends Actor with ActorLogging {
    // ...
}
```

oraz zmodyfikowaną reprezentację komunikatu „startowego”:

```scala
case class Graj04a( ??? )
```

tak, aby rozgrywka mogła odbywać się „po okręgu” złożonym z zadanej (z poziomu programu głównego) liczby graczy. Do utworzenia aktorów możesz wykorzystać wyliczenie `for/yield`.

## Zadanie 11.04b

Zmodyfikuj rozwiązanie zadania poprzedniego, definiując klasę

```scala
class Gracz04b extends Actor with ActorLogging {
    // ...
}
```

oraz zmodyfikowaną reprezentację komunikatu „startowego”:

```scala
case class Graj04b( ??? )
```

tak, aby rozgrywka mogła odbywać się w grupie złożonej z zadanej (z poziomu programu głównego) liczby graczy, piłeczka za każdym razem powinna być odbijana do losowego gracza. Do utworzenia aktorów możesz wykorzystać wyliczenie `for/yield`.
