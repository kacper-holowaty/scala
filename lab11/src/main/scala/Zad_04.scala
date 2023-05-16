package lab11_4

import scala.util.Random
import akka.actor.{ActorSystem, Actor, ActorLogging, ActorRef, Props}

case object Piłeczka
case class Graj04a(lista: List[ActorRef])

class Gracz04a extends Actor with ActorLogging {

    def receive: Receive = {
        case Graj04a(przeciwnicy) =>
            // val gracze = List(przeciwnik1,przeciwnik2)
            // val random = new Random()
            // val zaczyna = gracze(random.nextInt(2))
            // val nastepny = gracze.filterNot(_ == zaczyna).head
            // println(zaczyna)
            // println(nastepny)
            log.info(s"Zaczynamy!")
            // val nastepniGracze = przeciwnicy.tail :+ przeciwnicy.head
            przeciwnicy.head ! Piłeczka
            context.become(gramyDalej(przeciwnicy))
        case Piłeczka => 
            log.info("Gramy?")
            
    }
    
    def gramyDalej(przeciwnicy: List[ActorRef]): Receive = {
        case Piłeczka =>
            val nastepniGracze = przeciwnicy.tail :+ przeciwnicy.head  
            log.info("Otrzymano piłeczkę!")
            przeciwnicy.head ! Piłeczka
            context.become(gramyDalej(nastepniGracze)) 
  }

}

@main def main4a: Unit = {
  val system = ActorSystem("PingPong")
//   println("Podaj liczbe graczy: ")
//   val liczbaGraczy = io.StdIn.readLine().toInt
  val liczbaGraczy = 12
  val lista = for (
    x <- (0 to liczbaGraczy-1).toList
  ) yield {system.actorOf(Props[Gracz04a](), s"Gracz${x+1}")}
  lista.head ! Graj04a(lista)
}
