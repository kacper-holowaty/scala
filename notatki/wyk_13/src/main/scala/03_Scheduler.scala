package jp1.akka_13_03.scheduler

import akka.actor.ActorLogging
import akka.actor.Actor
import akka.actor.Actor.Receive
import scala.concurrent.duration.FiniteDuration
import akka.actor.Props
import akka.actor.ActorSystem

// Nie wykorzytaliśmy Workera, ale łatwo wykombinować jak Boss
// może wysyłać coś do niego a potem odbierać, będąc cały czas
// napędzanym przez „planistę”…
class Worker extends Actor with ActorLogging {
  def receive: Receive = { case msg =>
    log.info(s"dostałem $msg")
    sender() ! msg
    log.info(s"odpowiedź do ${sender()}")
  }
}

object Boss {
  def props(delay: FiniteDuration): Props = {
    Props(classOf[Boss], delay)
  }
}
class Boss(delay: FiniteDuration) extends Actor with ActorLogging {
  case object Tick
  override def preStart(): Unit = {
    super.preStart()
    implicit val ec = context.dispatcher

    context.system.scheduler.scheduleWithFixedDelay(
      delay,
      delay,
      self,
      Tick
    )

  }
  def receive: Receive = {
    case Tick =>
      log.info(s"dostałem Tick")
  }
}

import scala.concurrent.duration.*

@main def go: Unit = {
  val system = ActorSystem("system")
  val worker = system.actorOf(Props[Worker](), "worker")
  // wczytujemy konfigurację aplikacji z „resources/application.conf”
  val config = system.settings.config
  // i odwołujemy się do jej elementu
  val delay = config.getInt("planistaDemo.delay")
  val boss = system.actorOf(Boss.props(delay.milli), "boss")
}
