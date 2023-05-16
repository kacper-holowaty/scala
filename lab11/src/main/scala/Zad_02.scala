package lab11_2

import akka.actor.{ActorSystem, Actor, ActorLogging, ActorRef, Props}

case object Piłeczka
case class Graj02(przeciwnik: ActorRef, maks: Int)

class Gracz02 extends Actor with ActorLogging {
    def receive: Receive = {
        case Graj02(oponent,maks) => 
            log.info(s"Zaczyna ${oponent.path.name}")
            context.become(gramyDalej(maks))
            oponent ! Piłeczka
        case Piłeczka => 
            log.info(s"odbijam piłeczkę do ${sender().path.name}!")
            sender() ! Piłeczka
    }
    
    def gramyDalej(maks: Int): Receive = {
        case Piłeczka =>
            if (maks <= 1) {
                log.info(s"odbijam piłeczkę do ${sender().path.name}!")
                context.system.terminate()
            } 
            else {
                log.info(s"odbijam piłeczkę do ${sender().path.name}!")
                sender() ! Piłeczka
                context.become(gramyDalej(maks-1))
            }
    }
}


@main def main2: Unit = {
  val system = ActorSystem("PingPong")
  val graczA = system.actorOf(Props[Gracz02](), "GraczA")
  val graczB = system.actorOf(Props[Gracz02](), "GraczB")
  graczA ! Graj02(graczB,3)
}
