package kolokwium_2

import akka.actor.{Actor, ActorLogging, Props}

abstract class DoSzefa
case class W(słowo: String) extends DoSzefa //dodawanie słow
case class I(słowo: String) extends DoSzefa // pytanie o liczbę wystąpień
case class Ile(słowo: String, n: Int) extends DoSzefa // wysyłają pracownicy aby odp na I od prog. głowny

class Szef extends Actor with ActorLogging {
  def receive: Receive = mamDane(dane)
  def mamDane(data: List[String]): Receive = {
    // case msg => log.info(s"Odebrałem wiadomość: ${msg}")
    case W(słowo) => 
    log.info(s"$data")
      val worker = context.actorOf(Props[Pracownik](),"wstawiacz")
      worker ! AlterWstaw(słowo,0)
    case I(słowo) =>
      val worker = context.actorOf(Props[Pracownik](),"szukacz")
      worker ! Szukaj(słowo,słowo)
    case Ile(słowo,n) =>
      log.info(s"($słowo, $n)")
  }
}
