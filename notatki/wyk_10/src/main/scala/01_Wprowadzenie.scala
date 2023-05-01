import akka.actor.Actor
import akka.actor.ActorLogging
import akka.actor.ActorRef
import akka.actor.ActorSystem
import akka.actor.Props

case object Oskar

class MovieStar extends Actor with ActorLogging {
  def receive: Receive = {
    case Oskar =>
      log.info("Oskar? No i świetnie! Idę na emeryturę!")
      context.system.terminate()
    case msg => log.info(s"Odebrałem wiadomość: ${msg}")
    // kolejne „case'y”
  }
}

// Uruchomienie:  reStart
// Zatrzymanie:   reStop
@main def main1: Unit = {
  val system = ActorSystem("Hollywood")
    val leonardo = system.actorOf(Props[MovieStar](), "leonardo")
    val kate = system.actorOf(Props[MovieStar](), "kate")
    kate ! 997
    leonardo ! Oskar
}
