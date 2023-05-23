package wyk_12_xtra

import akka.actor.Actor
import akka.actor.ActorLogging
import akka.actor.ActorRef
import akka.actor.ActorSystem
import akka.actor.Props

/*
  Wykorzystując poniższy przykład jako inspirację stwórz konkurencyjną implementację
  Sita Eratostenesa, w której informacja na temat Bossa przekazywana jest w momencie
  tworzenia Pracownika.
*/

case object Ping
case object LetsHaveAChild
case class Create(name: String)

class Boss extends Actor with ActorLogging {
  def receive: Receive = {
    case Create(name) =>
      log.info(s"Tworzę aktora ${name}")
      val potomek = context.actorOf(Props(MyActor(self)), name)
      potomek ! LetsHaveAChild
    case Ping =>
      log.info(s"Dostałem Ping od aktora ${sender().path.name}")
  }
}
class MyActor(boss: ActorRef) extends Actor with ActorLogging {
  def receive: Receive = {
    case Ping =>
      log.info(s"Dostałem Ping od aktora ${sender().path.name}. Zaraz odeślę do bossa")
      boss ! Ping
    case LetsHaveAChild =>
      log.info("Pracuję nad potomkiem…")
      val potomek = context.actorOf(Props(MyActor(boss)), s"potomek_aktora_${self.path.name}")
      potomek ! Ping
  }
}

@main def główny: Unit = {
  val system = ActorSystem("system")
  val boss = system.actorOf(Props[Boss](), "boss")

  boss ! Create("Bolek")
  boss ! Create("Lolek")

  Thread.sleep(1200)
  system.terminate()
}
