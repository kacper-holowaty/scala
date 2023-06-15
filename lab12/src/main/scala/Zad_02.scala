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
        sender() ! Wynik(k, fib_n)
      }
      else {
        val pracownik = context.actorOf(Props(new Pracownik(k,boss)))
        log.info("Tworze pracownika")
        pracownik ! Oblicz(k)
      }
    case Wynik(k, fib_n) =>
      log.info("jestem w wyniku u Nadzorcy PogU")
      log.info(s"$cache")
      boss ! Wynik(k, fib_n)
      val newCache = cache + (k -> fib_n)
      context.become(obliczaj(newCache))
  }
}

class Pracownik(k: Int, boss: ActorRef) extends Actor with ActorLogging {
  // def receive: Receive = keepGoing(None,None)
    
  // def keepGoing(prev1: Option[BigInt],prev2: Option[BigInt]): Receive = {
  //   // case Oblicz(number) if k <= 2 => 
  //   //   log.info("BUUUUU")
  //   //   sender() ! Wynik(k, 1)
  //   case Oblicz(number) => 
  //     val nadzorca = context.parent
  //     log.info(s"${nadzorca.path.name} - to mój tata!")
  //     nadzorca ! Oblicz(k-2)
  //     nadzorca ! Oblicz(k-1)
  //   case Wynik(key, value) if key == k-1 => 
  //     val nadzorca = context.parent
  //     prev1 = Some(value)
  //     // nadzorca ! Wynik(n, fib_n)
  //     check(prev1,prev2)
  //   case Wynik(key, value) if key == k-2 => 
  //     val nadzorca = context.parent
  //     prev2 = Some(value)
  //     // nadzorca ! Wynik(n, fib_n)
  //     check(prev1,prev2,nadzorca)
  // }
  // def check(prev1: Option[BigInt], prev2: Option[BigInt], nadzorca: ActorRef): Unit = {
  //   (prev1,prev2) match {
  //     case (Some(prev1),Some(prev2)) => {
  //       val result = prev1 + prev2
  //       boss ! Wynik(k,result)
  //       nadzorca ! Wynik(k,result)
  //       context.stop(self)
  //     }
  //     case _ => 

  //   }
  // }   
  private val nadzorca: ActorRef = context.parent

  def receive: Receive = {
    case Oblicz(_) =>
      if (k <= 2) {
        nadzorca ! Wynik(k, 1)
      } else {
        nadzorca ! Oblicz(k - 1)
        nadzorca ! Oblicz(k - 2)
      }
    case Wynik(index, prev) if index == k - 1 =>
      context.become(receiveWithPrev(prev))
    case Wynik(index, prev) if index == k - 2 =>
      context.become(receiveWithPrev(prev, Some(prev)))
    case Wynik(_, _) =>
      // Ignore results for other indices
  }

  def receiveWithPrev(prev: BigInt, prev2Opt: Option[BigInt] = None): Receive = {
    case Oblicz(_) =>
      prev2Opt match {
        case Some(prev2) =>
          val result = prev + prev2
          nadzorca ! Wynik(k, result)
          context.stop(self)
        case None =>
        // Wait for prev2
      }
    case Wynik(index, prev2) if index == k - 2 =>
      val result = prev + prev2
      nadzorca ! Wynik(k, result)
      context.stop(self)
    case Wynik(_, _) =>
      // Ignore results for other indices
  }
}

@main def main: Unit = {
  val system = ActorSystem("Fibonacci")
  val boss = system.actorOf(Props[Boss](),"boss")
  boss ! Oblicz(8)
  // system.terminate()
}



