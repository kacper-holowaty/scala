package lab12_1

import akka.actor.{ActorSystem, Actor, ActorLogging, ActorRef, Props}

case class Oblicz(liczba : Int)
case class Wynik(wynik : Int)

class Boss extends Actor with ActorLogging {
  def receive: Receive = {
    case Oblicz(liczba) => 
      val worker = context.actorOf(Props[Pracownik](),"worker")
      worker ! Oblicz(liczba)
      context.become(zWynikiem(liczba))
  }
  def zWynikiem(elem: Int): Receive = {
    case Wynik(wynik) =>
      // val pracownik = context.actorOf(Props[Pracownik](),"pracownik")
      // pracownik ! Oblicz(liczba)
      log.info(s"Koniec! $elem element ciągu Fibonacciego jest równy: $wynik.")
      // context.system.terminate()
  }
}

class Pracownik extends Actor with ActorLogging {
  def receive: Receive = mamPracownika(0)
  def mamPracownika(wynik: Int): Receive = {
    case Oblicz(liczba) => 
      if (liczba == 0 || liczba == 1) sender() ! Wynik(1)
      else {
        val w1 = context.actorOf(Props[Pracownik](),"w1")
        val w2 = context.actorOf(Props[Pracownik](),"w2")
        w1 ! Oblicz(liczba-1)
        w2 ! Oblicz(liczba-2)
        context.become(zPodwładnym(sender(),0))
      }    
  }  
    
  def zPodwładnym(podwładny: ActorRef, res: Int): Receive = {
    case Wynik(wynik) => 
      if (res == 0) {
        context.become(zPodwładnym(podwładny,wynik))
      }
      else {
        val sum = wynik + res
        podwładny ! Wynik(sum)
      }
  }
}


@main def main: Unit = {
  val system = ActorSystem("Fibonacci")
  val boss = system.actorOf(Props[Boss](),"boss")
  boss ! Oblicz(4)
}
