package kolokwium_2

import akka.actor.{Actor, ActorLogging, Props, ActorRef}

abstract class DoPracownika
case class Wstaw(słowo: String) extends DoPracownika
case class Szukaj(słowo: String, słowoPoczątkowe: String) extends DoPracownika

class Pracownik extends Actor with ActorLogging {
  def receive: Receive = zDanymi(0,Map())
  def zDanymi(liczbaSłów: Int, workers: Map[Char, ActorRef]): Receive = {
    case Wstaw(słowo) => 
      if (słowo.tail.nonEmpty) {
        if (workers.contains(słowo.tail.head)) {
          workers(słowo.tail.head) ! Wstaw(słowo.tail)
        }
        else {
          val w = context.actorOf(Props[Pracownik]())
          w ! Wstaw(słowo.tail)
          val newWorkers = workers.updated(słowo.tail.head,w)
          context.become(zDanymi(liczbaSłów, newWorkers))
        }
      }
      else {
        val newLiczbaSłów = liczbaSłów+1
        context.become(zDanymi(newLiczbaSłów, workers))
      }  
    case Szukaj(słowo, słowoPoczątkowe) => 
      val szef = context.actorSelection("/user/szef")
      if (słowo.tail.nonEmpty) {
        if (workers.contains(słowo.tail.head)) {
          workers(słowo.tail.head) ! Szukaj(słowo.tail,słowoPoczątkowe)
        }
        else {
          szef ! Ile(słowoPoczątkowe, 0)
        }
      }
      else {
        szef ! (Ile(słowoPoczątkowe, liczbaSłów))
      }  
  }
}
