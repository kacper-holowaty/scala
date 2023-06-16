package jp1.akka_14_02.supervision

import akka.actor.Actor
import akka.actor.ActorLogging
import akka.actor.ActorRef
import akka.actor.ActorSystem
import akka.actor.Props
import akka.actor.SupervisorStrategy
import akka.actor.ActorInitializationException
import akka.actor.ActorKilledException

// Komunikaty specyficzne dla Workera
object Worker {
  case object ResumeException extends Exception("Kontynuacja")
  case object RestartException extends Exception("Ponowne uruchomienie")
  case object StopException extends Exception("Zatrzymanie")
}

class Worker extends Actor with ActorLogging {
  import Worker._

  override def preStart() = {
    log.info("wykonuję preStart")
  }

  override def preRestart(powód: Throwable, wiadomość: Option[Any]) = {
    log.info(s"wykonuję preRestart ")
    log.error("Restart z powodu [{}] w trakcie przetwarzania wiadomości [{}]", powód.getMessage, wiadomość.getOrElse("***"))
    // super.preRestart(powód, wiadomość)
  }

  override def postRestart(powód: Throwable) = {
    log.info("wykonuję postRestart")
    // super.postRestart(powód)
  }

  override def postStop() = {
    log.info("wykonuję postStop")
  }

  def receive = {
    case "ResumeException" =>
      log.info("dostałem wiadomość „ResumeException”")
      throw ResumeException
    case "RestartException" =>
      log.info("dostałem wiadomość „RestartException”")
      throw RestartException
    case "StopException" =>
      log.info("dostałem wiadomość „StopException”")
      throw StopException
    case _ =>
      log.info("natrafiłem na inny wyjątek…")
      throw Exception()
  }
}
object Boss {
  def props(worker: ActorRef): Props = {
    Props(classOf[Boss], worker)
  }
}
class Boss(worker: ActorRef) extends Actor with ActorLogging {

  import akka.actor.SupervisorStrategy
  import akka.actor.OneForOneStrategy
  import scala.concurrent.duration.*

  override val supervisorStrategy: SupervisorStrategy = {
    import SupervisorStrategy.*

    def stoppingDecider: Decider = {
      case _: Exception => Stop
    }

    def defaultDecider: Decider = {
      case _: ActorInitializationException => Stop
      case _: ActorKilledException => Stop
      case _: Exception => Restart
    }

    def customDecider: Decider = {
      case Worker.ResumeException => Resume
      case Worker.RestartException => Restart
      case Worker.StopException => Stop
      case _: Exception => Escalate
    }

    OneForOneStrategy()(customDecider)
    // OneForOneStrategy(maxNrOfRetries = 10, withinTimeRange = 1.second)(customDecider)
  }
  override def preStart() = {
    log.info("wykonuję preStart")
    Thread.sleep(100)
  }

  override def postStop() = {
    log.info("wykonuję postStop")
  }

  def receive = {
    case msg =>
      log.info(s"otrzymałem wiadomość: „${msg}”")
      worker ! msg
      Thread.sleep(100)
  }

}

@main def main: Unit = {
  val system = ActorSystem("system")
  val worker = system.actorOf(Props[Worker](), "pracownik")
  val boss = system.actorOf(Boss.props(worker), "szef")

  execute("ResumeException") {
    boss ! "ResumeException"
    Thread.sleep(1000)
  }

  // execute("RestartException") {
  //   boss ! "RestartException"
  //   Thread.sleep(1000)
  // }

  // execute("StopException") {
  //   boss ! "StopException"
  //   Thread.sleep(1000)
  // }

  system.terminate()

}

private def execute(msg: String)(cmd: => Unit) = {
  println()
  sep(s"${msg} (BEGIN)")
  cmd
  sep(s"${msg} (END)")
  println()
}

private def sep(str: String) = {
  val right = 40 - str.length
  println(s"${"%" * 40} $str ${"%" * right}")
}

