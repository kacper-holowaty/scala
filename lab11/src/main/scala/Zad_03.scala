package lab11_3

import akka.actor.{ActorSystem, Actor, ActorLogging, ActorRef, Props}

case object Piłeczka
case class Graj03(przeciwnik1: ActorRef, przeciwnik2: ActorRef)

class Gracz03 extends Actor with ActorLogging {
    def receive: Receive = {
        case Graj03(przeciwnik1,przeciwnik2) =>
            log.info(s"Zaczynamy!")
            przeciwnik1 ! Piłeczka
            context.become(gramyDalej(przeciwnik1, przeciwnik2))
        case Piłeczka => 
          log.info("Otrzymano piłeczkę!")
          sender() ! Piłeczka
    }
    
    def gramyDalej(przeciwnik1: ActorRef, przeciwnik2: ActorRef): Receive = {
        case Piłeczka =>
            log.info("Otrzymano piłeczkę!")
            przeciwnik1 ! Piłeczka
            // przeciwnik2 ! Piłeczka
            context.become(gramyDalej(przeciwnik2,sender()))
  }

}



@main def main3: Unit = {
  val system = ActorSystem("PingPong")
  val graczA = system.actorOf(Props[Gracz03](), "GraczA")
  val graczB = system.actorOf(Props[Gracz03](), "GraczB")
  val graczC = system.actorOf(Props[Gracz03](), "GraczC")
  graczA ! Graj03(graczB, graczC)
  // graczB ! Graj03(graczC, graczA)
  // graczC ! Graj03(graczA, graczB)
}
