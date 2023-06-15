package jp1.akka_14_01.monitoring

import akka.actor.Actor
import akka.actor.ActorLogging
import akka.actor.ActorRef
import akka.actor.ActorSystem
import akka.actor.Props
import akka.actor.Terminated

object Boss {
  // Wiadomości specyficzne dla Boss-a
  sealed trait BossMsg
  case class Work(n: Int) extends BossMsg

  // Jeśli konstruktor klasy aktora wymaga parametrów, to wskazane jest
  // zdefiniowanie metody „fabrycznej” konstruującej obiekt Props
  def props(worker: ActorRef): Props = {
    Props(classOf[Boss], worker)
  }
}

class Boss(worker: ActorRef) extends Actor with ActorLogging {
  import Boss._
  override def preStart() = {
    context.watch(worker)
  }

  override def postStop() = {
    log.info("===> Boss kończy działanie (postStop).")
    context.system.terminate()
  }

  def receive = {
    case Work(n) =>
      worker ! n
    // Jeśli worker zakończy działanie to my też
    case Terminated =>
      context.stop(self)
  }
}

class Worker extends Actor with ActorLogging {

  def receive = {
    case msg =>
      log.info(s"===> Aktor „${self.path.name}” otrzymał wiadomość: „${msg}”")
      context.stop(self)
  }

}

@main def main: Unit = {
  val system = ActorSystem("monitoring")
  val worker = system.actorOf(Props[Worker](), "worker")

  // import Boss._
  val boss = system.actorOf(Boss.props(worker), "boss")
  boss ! Boss.Work(123)

  // system.terminate()
}
