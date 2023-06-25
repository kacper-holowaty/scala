package kolokwium_2

import akka.actor.{Actor, ActorLogging, Props, ActorRef}

abstract class DoSzefa
case class W(słowo: String) extends DoSzefa
case class I(słowo: String) extends DoSzefa 
case class Ile(słowo: String, n: Int) extends DoSzefa 

class Szef extends Actor with ActorLogging {
  def receive: Receive = zPracownikami(Map())
  def zPracownikami(pracownicy: Map[Char,ActorRef]): Receive = {
    case W(słowo) => 
      if (pracownicy.contains(słowo.head)) {
        pracownicy(słowo.head) ! Wstaw(słowo)
      }
      else {
        val worker = context.actorOf(Props(new Pracownik(słowo.head,"")))
        worker ! Wstaw(słowo)
        val newWorkers = pracownicy + (słowo.head -> worker)
        // log.info(s"$newWorkers")
        context.become(zPracownikami(newWorkers))
      }
    case I(słowo) => 
      if (pracownicy.contains(słowo.head)) {
        pracownicy(słowo.head) ! Szukaj(słowo,słowo)
      }
      else {
        self ! Ile(słowo,0)
      }
    case Ile(słowo,n) => 
      log.info(s"($słowo,$n)")
  }
}
