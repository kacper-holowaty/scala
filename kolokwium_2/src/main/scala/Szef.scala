package kolokwium_2

import akka.actor.{Actor, ActorLogging, Props, ActorRef}

abstract class DoSzefa
case class W(słowo: String) extends DoSzefa //dodawanie słow
case class I(słowo: String) extends DoSzefa // pytanie o liczbę wystąpień
case class Ile(słowo: String, n: Int) extends DoSzefa // wysyłają pracownicy aby odp na I od prog. głowny

class Szef extends Actor with ActorLogging {
  def receive: Receive = zPracownikami(Map())
  def zPracownikami(pracownicy: Map[Char, ActorRef]): Receive = {
    case W(słowo) => 
      if (pracownicy.contains(słowo.head)) {
        pracownicy(słowo.head) ! Wstaw(słowo)
      }
      else {
        val worker = context.actorOf(Props[Pracownik]())
        worker ! Wstaw(słowo)
        val newPracownicy = pracownicy + (słowo.head -> worker)
        context.become(zPracownikami(newPracownicy))
      }
    case I(słowo) => 
      if (pracownicy.contains(słowo.head)) {
        val słowoPoczątkowe = słowo
        pracownicy(słowo.head) ! Szukaj(słowo, słowoPoczątkowe)
      }
      else {
        self ! Ile(słowo,0)
      }
    case Ile(słowo, n) =>
      log.info(s"($słowo,$n)")

  }
}
