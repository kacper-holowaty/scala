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
    case Wynik(n, fib_n) => 
      log.info(s"Wartość $n-tego elementu ciągu Fibonacciego wynosi: $fib_n")
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
        sender() ! Wynik(k, fib_n)
      }
      else {
        val pracownik = context.actorOf(Props(new Pracownik(k)))
        log.info("Tworze pracownika")
        pracownik ! Oblicz(k)
      }
    // case Wynik(k, fib_n) =>
    //   log.info("jestem w wyniku u Nadzorcy PogU")
    //   log.info(s"$cache")
    //    ! Wynik(k, fib_n)
    //   val newCache = cache + (k -> fib_n)
    //   context.become(obliczaj(newCache))
  }
}

class Pracownik(k: Int) extends Actor with ActorLogging {
  def receive: Receive = helper(0,0)
  def helper(suma1: BigInt,suma2: BigInt): Receive = {
    case Oblicz(k) => 
      sender() ! Oblicz(k-1)
      sender() ! Oblicz(k-2)
    case Wynik(n,fib_n) =>
      context.become(helper())
  }  
  
}

@main def main: Unit = {
  val system = ActorSystem("Fibonacci")
  val boss = system.actorOf(Props[Boss](),"boss")
  boss ! Oblicz(4)
  // system.terminate()
}



