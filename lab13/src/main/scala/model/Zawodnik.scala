package jp1.akka.lab13.model

import akka.actor.{Actor, ActorLogging}

object Zawodnik {
  case object Próba
  // polecenie wykonania próby (kończy się zwróceniem Wyniku,
  // za pomocą komunikatu Grupa.Wynik)
}

class Zawodnik(o: Osoba) extends Actor with ActorLogging {
  def receive: Receive = {
    case Zawodnik.Próba => 
      sender() ! Grupa.Wynik(Utl.ocena())
  }
}
