package wyk_12_wersja1

import akka.actor.Actor
import akka.actor.ActorLogging
import akka.actor.ActorSystem
import akka.actor.Props
import akka.actor.ActorRef

// wykorzystamy następujące „typy” komunikatów
case class Primes(limit: Int) // jak spowodować żeby Prime(n) dla n <= 1 generowało błąd
case class Kandydat(n: Int, boss: ActorRef)
case class OddajSkarb(skarb: Int)
case class Koniec(boss: ActorRef)
case object Go

// Uwaga: klasy „wzorcowe” (case class) zapewniają, że np. Primes(12) == Primes(12)

class Boss extends Actor with ActorLogging {
  def receive: Receive = {
    case Primes(limit) =>
      log.info(s"Wyszukujemy liczby pierwsze w przedziale [2..$limit]")
      val worker = context.actorOf(Props[Worker](), "worker")
      val wynik = for {
        kandydat <- (2 to limit).toList
      } worker ! Kandydat(kandydat, self) // ()
      worker ! Koniec(self)
      context.become(withResults())
  }

  def withResults(primes: List[Int] = Nil): Receive = {
    case OddajSkarb(prime) =>
      val newPrimes = prime :: primes
      log.info(s"$newPrimes")
      context.become(withResults(newPrimes))
    case Go =>
      log.info(s"${primes.reverse}")
  }
}
object Boss {
  // Inny sposób to „podział” komunikatów na „przeznaczone dla konkretnego odbiorcy”.
  // Przykładowo dla specyficzny dla Bossa mógłby być:
  case object KomunikatDlaBossa
}

class Worker extends Actor with ActorLogging {
  def receive: Receive = {
    case Kandydat(skarb, boss) =>
      log.info(s"Dostałem: $skarb")
      boss ! OddajSkarb(skarb)
      context.become(withMyPrecious(skarb))
    case Koniec(boss) => boss ! Go
  }

  def withMyPrecious(skarb: Int): Receive = {
    case Kandydat(liczba, boss) =>
      if liczba % skarb != 0 then {
        val potomek = context.actorOf(Props[Worker](), s"worker$skarb")
        potomek ! Kandydat(liczba, boss)
        context.become(withMyPreciousAndWorker(skarb, potomek))
      }
    case Koniec(boss) => boss ! Go
  }

  def withMyPreciousAndWorker(skarb: Int, potomek: ActorRef): Receive = {
    case Kandydat(liczba, boss) =>
      if liczba % skarb != 0 then {
        potomek ! Kandydat(liczba, boss)
      }
     case Koniec(boss) => potomek ! Koniec(boss)

  }
}

@main def sito: Unit = {
  val system = ActorSystem("Eratostenes")
  val boss = system.actorOf(Props[Boss](), "boss")

  boss ! Primes(50)
  // boss ! Boss.KomunikatDlaBossa // powędruje do Biura Listów Zagubionych!
}
