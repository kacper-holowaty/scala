package lab11_1

import akka.actor.{ActorSystem, Actor, ActorLogging, ActorRef, Props}

case object Piłeczka
case class Graj01(przeciwnik: ActorRef)

class Gracz01 extends Actor with ActorLogging {
    def receive: Receive = {
        case Graj01(przeciwnik) => 
            log.info(s"Zaczyna ${przeciwnik.path.name}")
            przeciwnik ! Piłeczka
            context.become(gramyDalej)
        case Piłeczka => 
            log.info(s"odbijam piłeczkę do ${sender().path.name}")
            sender() ! Piłeczka
            context.become(gramyDalej)
    }
    
    def gramyDalej: Receive = {
        case Piłeczka => 
            log.info(s"odbijam piłeczkę do ${sender().path.name}!")
            sender() ! Piłeczka
    }
}


@main def main1: Unit = {
  val system = ActorSystem("PingPong")
  val graczA = system.actorOf(Props[Gracz01](), "GraczA")
  val graczB = system.actorOf(Props[Gracz01](), "GraczB")
  graczA ! Graj01(graczB)
}