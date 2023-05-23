package lab11_4

import scala.util.Random
import akka.actor.{ActorSystem, Actor, ActorLogging, ActorRef, Props}

case object Piłeczka
case class Graj04a(lista: List[ActorRef])

class Gracz04a extends Actor with ActorLogging {

    def receive: Receive = {
        case Graj04a(przeciwnicy) =>
            log.info(s"Zaczynamy!")
            przeciwnicy.head ! Piłeczka
            context.become(gramyDalej(przeciwnicy))
        case Piłeczka => 
            log.info("Otrzymano piłeczkę!")
            sender() ! Piłeczka
            
    }
    
    def gramyDalej(przeciwnicy: List[ActorRef]): Receive = {
        case Piłeczka =>
            val nastepniGracze = przeciwnicy.tail :+ przeciwnicy.head  
            // log.info("Otrzymano piłeczkę!")
            przeciwnicy.head ! Piłeczka
            context.become(gramyDalej(nastepniGracze)) 
  }
}

@main def main4a: Unit = {
  val system = ActorSystem("PingPong")
//   println("Podaj liczbe graczy: ") //do uruchamiania za pomocą run
//   val liczbaGraczy = io.StdIn.readLine().toInt
  val liczbaGraczy = 12
  val lista = for (
    x <- (0 to liczbaGraczy).toList
  ) yield {system.actorOf(Props[Gracz04a](), s"Gracz${x}")}
  lista.head ! Graj04a(lista)
}

case class Graj04b(lista: List[ActorRef])

class Gracz04b extends Actor with ActorLogging {

    def receive: Receive = {
        case Graj04b(przeciwnicy) =>
            log.info(s"Zaczynamy!")
            przeciwnicy.head ! Piłeczka
            context.become(gramyDalej(przeciwnicy))
        case Piłeczka => 
            log.info("Otrzymano piłeczkę!")
            sender() ! Piłeczka

            
    }
    
    def gramyDalej(przeciwnicy: List[ActorRef]): Receive = {
        case Piłeczka =>
            val nastepny = losowyGracz(przeciwnicy)  
            // log.info("Otrzymano piłeczkę!")
            nastepny ! Piłeczka
    }

    def losowyGracz(przeciwnicy: List[ActorRef]): ActorRef = {
        val random = new Random()
        val index = random.nextInt(przeciwnicy.length)
        przeciwnicy(index)
    }

}

@main def main4b: Unit = {
  val system = ActorSystem("PingPong")
  val liczbaGraczy = 12
  val lista = for (
    x <- (0 to liczbaGraczy).toList
  ) yield {system.actorOf(Props[Gracz04b](), s"Gracz${x}")}
  lista.head ! Graj04b(lista)
}
