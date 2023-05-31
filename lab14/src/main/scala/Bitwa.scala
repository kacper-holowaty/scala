package jp1.akka.lab14

import akka.actor.ActorLogging
import akka.actor.Actor
import akka.actor.ActorRef
import akka.actor.Actor.Receive
import akka.actor.Props
import akka.actor.ActorSystem
import scala.concurrent.duration.*


case object Strzał
case object Strzała

class Nadzorca extends Actor with ActorLogging {
  def receive: Receive = {
    case Strzał => 
      val losowyZamek = context.actorSelection("/user/Zamek*")
      losowyZamek ! Strzał
  }
  
}

class Zamek extends Actor with ActorLogging {
  val obrońcy = for {
      i <- (1 to 20).toList} yield context.actorOf(Props[Obrońca](),s"${self.path.name}_obronca_$i") 
  def receive: Receive = zObrońcami(obrońcy,self)
  def zObrońcami(obrońcy: List[ActorRef], obecnyZamek: ActorRef): Receive = {
    case Strzał => 
      if (obecnyZamek.path.name == "zamekB") {
        val przeciwnik = context.actorSelection("/user/ZamekA")
        obrońcy.foreach(o => przeciwnik ! Strzała)
      }
      else {
        val przeciwnik = context.actorSelection("/user/ZamekB")
        obrońcy.foreach(o => przeciwnik ! Strzała)
      }
      // obrońcy.foreach(o => o ! Strzała)
    case Strzała => 
      log.info("dddddd")
  }
  
  
}

class Obrońca extends Actor with ActorLogging {
  def receive: Receive = {
    case Strzała =>
      log.info(s"dostałem strzałą od ${sender()}")

  }
}

@main def go: Unit = {
  val system = ActorSystem("system")
  // żeby planista mógł działać „w tle” potrzebuje puli wątków:
  implicit val executionContext = system.dispatcher

  // tworzymy zamki
  val nadzorca = system.actorOf(Props[Nadzorca](),"nadzorca")
  val zamekA = system.actorOf(Props[Zamek](), "ZamekA")
  val zamekB = system.actorOf(Props[Zamek](), "ZamekB")
  // val zamek = system.actorOf(Props[Zamek](), "Zamek")
  // wczytujemy konfigurację aplikacji z „resources/application.conf”
  // i odwołujemy się do jej elementu, wyrażonego w milisekundach
  val config = system.settings.config
  val delay = config.getInt("planista.delay").milli

  // val losowyZamek = system.actorSelection("/user/zamek*")
  // uruchamiamy planistę
  system.scheduler.scheduleWithFixedDelay(
    delay,
    delay,
    nadzorca,
    Strzał
  )

  // Uwaga! Oczywiście powyższy planista kontaktuje się jedynie
  // z jednym z zamków!!!
}
