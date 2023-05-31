package jp1.akka_13_02.paths

import akka.actor.{ActorSystem, Actor, ActorLogging, ActorRef, Props}
import akka.actor.Identify
import akka.actor.ActorIdentity

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
    case ActorIdentity(_, Some(actor)) =>
      log.info(s"${actor}")
    case msg =>
      log.info(s"wiadomość: $msg")
      selection ! Identify(None) // pytanie o tożsamość
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
