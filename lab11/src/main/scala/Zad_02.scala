package lab11

import akka.actor.{ActorSystem, Actor, ActorLogging, ActorRef, Props}

case object Piłeczka2
case class Graj02(przeciwnik: ActorRef, maks: Int)

class Gracz02 extends Actor with ActorLogging {
    def receive: Receive = {
        case Graj02(oponent,maks) => 
            log.info(s"Zaczyna ${oponent.path}")
            oponent ! Piłeczka2
            context.become(gramyDalej(maks))
        case Piłeczka2 => 
            sender() ! Piłeczka2
            context.become(gramyDalej(maks-1))
    }
    
    def gramyDalej(maks: Int): Receive = {
        case Piłeczka2 if maks < 0 => 
            log.info(s"wykonał wymaganą liczbę odbić ${sender().path}!")
            // sender() ! Piłeczka
            context.system.terminate()
        case Piłeczka2 => 
            log.info(s"odbijam piłeczkę do ${sender().path}!")
            sender() ! Piłeczka2
            context.become(gramyDalej(maks-1))
    }
}


@main def main2: Unit = {
  val system = ActorSystem("PingPong")
  val graczA = system.actorOf(Props[Gracz01](), "GraczA")
  val graczB = system.actorOf(Props[Gracz01](), "GraczB")
  graczA ! Graj02(graczB,14)
}