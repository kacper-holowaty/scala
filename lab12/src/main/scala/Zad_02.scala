package lab12_2

import akka.actor.{ActorSystem, Actor, ActorLogging, ActorRef, Props}

case class Oblicz(n: Int)
case class Wynik(n: Int, fib_n: BigInt)



class Boss extends Actor with ActorLogging {
  import Nadzorca._
  def receive: Receive = {
    case Oblicz(n) => 
      // log.info(s"Widzisz mnie?")
      val cache = Map(1 -> 1, 2 -> 1).map((a,n) => (a,BigInt(n)))
      val nadzorca = context.actorOf(Props(new Nadzorca(cache)), "nadzorca")
      log.info(s"$nadzorca")
      nadzorca ! Oblicz(n)
      context.become(zWynikiem)
  }
  def zWynikiem: Receive = {
    case Wynik(n, fib_n) => 
      log.info(s"Wartość $n-tego elementu ciągu Fibonacciego wynosi: $fib_n")
  }
}

class Nadzorca(cache: Map[Int, BigInt] = Map(1 -> 1, 2 -> 1)) extends Actor with ActorLogging {
  def receive: Receive = {
    case Oblicz(k) => 
      if cache.contains(k) then {
        val fib_n = cache(k)
        sender() ! Wynik(k, fib_n)
      }
      else {
        val pracownik = context.actorOf(Props(new Pracownik(k)))
        pracownik ! Oblicz(k)
      }
    case Wynik(k, fib_n) =>
        cache.updated(k, fib_n)
        sender() ! Wynik(k, fib_n)
  }
}

class Pracownik(k: Int) extends Actor with ActorLogging {
  def receive: Receive = {
    case Oblicz(number) if k <= 2 => 
      sender() ! Wynik(k, 1)
    case Oblicz(number) => 
      val nadzorca = context.parent
      nadzorca ! Oblicz(k-1)
      nadzorca ! Oblicz(k-2)
    case Wynik(n, fib_n) => 
      val nadzorca = context.parent
      // val fib_k = fib_n + cache.getOrElse(k - 1, 0) // nie ma dostępu do cache
      nadzorca ! Wynik(k, fib_k) 
      
  }
}

@main def main: Unit = {
  val system = ActorSystem("Fibonacci")
  val boss = system.actorOf(Props[Boss](),"boss")
  boss ! Oblicz(4)
  // system.terminate()
  // I'm done
}

