import akka.actor.Actor
import akka.actor.ActorRef
import akka.actor.ActorSystem
import akka.actor.Props

// Definicje wiadomości
case object Ball
case class Start(oponent: ActorRef)

class PingActor extends Actor {
  def receive: Receive = {
    case Start(oponent) =>
      oponent ! Ball
    case Ball =>
      println("pinger: dostałem piłeczkę")
      sender() ! Ball
  }
}

class PongActor extends Actor {
  def receive: Receive = { case Ball =>
    println(s"$self: dostałem piłeczkę")
    sender() ! Ball
  }
}

@main def main3: Unit = {
  val system = ActorSystem("PingPongGame")
  val pinger = system.actorOf(Props[PingActor](), "Pinger")
  // Aktor „pinger” zagra z dwoma rywalami „na raz”.
  val ponger1 = system.actorOf(Props[PongActor](), "Ponger1")
  val ponger2 = system.actorOf(Props[PongActor](), "Ponger2")
  pinger ! Start(ponger1)
  pinger ! Start(ponger2)
}
