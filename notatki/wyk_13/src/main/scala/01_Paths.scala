package jp1.akka_13_01.paths

import akka.actor.{ActorSystem, Actor, ActorLogging, ActorRef, Props}

class Worker extends Actor with ActorLogging {
  def receive: Receive = {
    case msg => log.info(s"dostałem $msg")
  }
}

// val w = new Watcher() // Watcher()
// w.x
class Watcher extends Actor with ActorLogging {

  val selection = context.actorSelection("/user/myPrecious*")

  def receive: Receive = {
    case msg =>
      log.info(s"wiadomość: $msg")
      selection ! "Aqq"
  }
}

case object Komunikat

@main def go: Unit = {
  val system = ActorSystem("Hollywood")
  val myActor = system.actorOf(Props[Worker](), "myPrecious")
  val myActorA = system.actorOf(Props[Worker](), "myPreciousA")
  val myActorB = system.actorOf(Props[Worker](), "myPreciousB")
  //.../user/myPrecious/dziecko/myPrecios  // ścieżka do aktora
  val watcher = system.actorOf(Props[Watcher](), "szef")
  watcher ! Komunikat
}
