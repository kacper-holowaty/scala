package lab12_1

import akka.actor.{ActorSystem, Actor, ActorLogging, ActorRef, Props}

case class Oblicz(liczba : Int)
case class Wynik(wynik : Int)

class Boss extends Actor with ActorLogging {
  def receive: Receive = {
    case Oblicz(liczba) => 
      val worker = context.actorOf(Props[Pracownik](),"worker")
      worker ! Oblicz(liczba)
      context.become(zWynikiem)
  }
  def zWynikiem: Receive = {
    case Wynik(wynik) =>
      // val pracownik = context.actorOf(Props[Pracownik](),"pracownik")
      // pracownik ! Oblicz(liczba)
      log.info(s"Koniec! Aktualny wynik to: $wynik")
  }
}

class Pracownik extends Actor with ActorLogging {
  def receive: Receive = {
    case Oblicz(liczba) => 
      if (liczba == 0 || liczba == 1) sender() ! Wynik(1)
      else {
        val w1 = context.actorOf(Props[Pracownik]())
        val w2 = context.actorOf(Props[Pracownik]())

        w1 ! Oblicz(liczba-1)
        w2 ! Oblicz(liczba-2)
        context.become(mamPracownika(w1))
      }
  }
  /*def mamPracownika(zleceniodawca: ActorRef, wynik: Int): Receive = {
    case Oblicz(liczba) => 
      if (liczba == 0 || liczba == 1) sender() ! Wynik(wynik+1)
      else {
        val w1 = context.actorOf(Props[Pracownik]())
        val w2 = context.actorOf(Props[Pracownik]())

        w1 ! Wynik()
      }
  }*/
  def przetwarzaWynik: Receive = {
    case Wynik(wynik) => 
  }
}


@main def main: Unit = {
  val system = ActorSystem("Fibonacci")
  val boss = system.actorOf(Props[Boss](),"boss")
  boss ! Oblicz(6)
}
