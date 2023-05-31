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
      val nadzorca = context.actorOf(Props(new Nadzorca(cache,self)), "nadzorca")
      log.info(s"$nadzorca")
      nadzorca ! Oblicz(n)
      context.become(zWynikiem)
  }
  def zWynikiem: Receive = {
    case Wynik(n, fib_n) => 
      log.info(s"Wartość $n-ego elementu ciągu Fibonacciego wynosi: $fib_n")
  }
}

class Nadzorca(cache: Map[Int, BigInt] = Map(1 -> 1, 2 -> 1), boss: ActorRef) extends Actor with ActorLogging {
  def receive: Receive = obliczaj(cache)
  
  def obliczaj(cache: Map[Int,BigInt]): Receive = {
    case Oblicz(k) => 
      if cache.contains(k) then {
        log.info(s"Sprawdzam...dla $k")
        log.info(s"$cache")
        val fib_n = cache(k)
        boss ! Wynik(k, fib_n)
      }
      else {
        val pracownik = context.actorOf(Props(new Pracownik(k,cache)))
        log.info("Tworze pracownika")
        pracownik ! Oblicz(k)
        // context.become(obliczaj(cache))
      }
    case Wynik(k, fib_n) =>
      log.info(s"$cache")
      // cache.updated(k, fib_n)
      val newCache = cache + (k -> fib_n)
      sender() ! Wynik(k, fib_n)
      context.become(obliczaj(newCache))
  }
}

class Pracownik(k: Int,cache: Map[Int, BigInt]) extends Actor with ActorLogging {
  def receive: Receive = helper(cache)

  def helper(cache: Map[Int, BigInt]): Receive = {
    case Oblicz(number) if k <= 2 => 
      sender() ! Wynik(k, 1)
    case Oblicz(number) => 
      val nadzorca = context.parent
      log.info(s"${nadzorca.path.name} - to mój tata!")
      nadzorca ! Oblicz(k-2)
      nadzorca ! Oblicz(k-1)
    case Wynik(n, fib_n) => 
      val nadzorca = context.parent
      nadzorca ! Wynik(n, fib_n)
      val fib_k = fib_n + cache.getOrElse(k - 1, 0)
      nadzorca ! Wynik(k, fib_k) 
  }   
}

@main def main: Unit = {
  val system = ActorSystem("Fibonacci")
  val boss = system.actorOf(Props[Boss](),"boss")
  boss ! Oblicz(4)
  // system.terminate()
}



