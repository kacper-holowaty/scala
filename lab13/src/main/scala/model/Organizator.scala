package jp1.akka.lab13.model

import akka.actor.{Actor, ActorRef, ActorLogging, Props}

val akkaPathAllowedChars = ('a' to 'z').toSet union
  ('A' to 'Z').toSet union
  "-_.*$+:@&=,!~';.)".toSet

object Organizator {
  case object Start
  // rozpoczynamy zawody – losujemy 50 osób, tworzymy z nich zawodników
  // i grupę eliminacyjną
  case object Runda
  // polecenie rozegrania rundy (kwalifikacyjnej bądź finałowej) –  wysyłamy Grupa.Runda
  // do aktualnej grupy
  case object Wyniki
  // polecenie wyświetlenia klasyfikacji dla aktualnej grupy
  case class Wyniki(w: Map[ActorRef, Option[Ocena]])
  // wyniki zwracane przez Grupę
  case object Stop
  // kończymy działanie
}

class Organizator extends Actor with ActorLogging {
  // importujemy komunikaty na które ma reagować Organizator
  import Organizator._

  def receive: Receive = {
    case Start =>
      // tworzenie 50. osób, opdowiadających im Zawodników
      // oraz Grupy eliminacyjnej
      val zawodnicy = List.fill(50) {
        val o = Utl.osoba()
        // log.info(s"$o")
        context.actorOf(Props(Zawodnik(o)), s"${o.imie}-${o.nazwisko}" filter akkaPathAllowedChars)
      }
      // val grupa1 = context.actorOf(Props(Grupa(zawodnicy)), "grupa_eliminacyjna")
      // grupa1 ! Grupa.Runda
      context.become(grupaEliminacyjna(zawodnicy))

    // Obsługa pozostałych komunikatów

    case Stop =>
      log.info("kończymy zawody...")
      context.system.terminate()
  }
  def grupaEliminacyjna(zawodnicy: List[ActorRef], oddalWynik: Map[ActorRef, Option(Ocena)]=Map()): Receive = {
    case Runda => 
      zawodnicy.head ! Zawodnik.Próba
      context.become(zawodnicy.tail, Map())
    case Some(Ocena(n1,n2,n3)) => 
      context.become(zawodnicy, Map(zawodnicy.head -> Some(Ocena(n1,n2,n3))))
    case None => // jak dodać kolejny element do mapy? check in home
  }
}
