package jp1.akka.lab14

import akka.actor.ActorLogging
import akka.actor.Actor
import akka.actor.ActorRef
import akka.actor.Actor.Receive
import akka.actor.Props
import akka.actor.PoisonPill
import akka.actor.ActorSystem
import scala.concurrent.duration.*
import scala.util.Random


case object Strzał
case object Strzała
case object Pudło
case class Dead(obrońca: ActorRef)

class Nadzorca extends Actor with ActorLogging {
  def receive: Receive = {
    case Strzał => 
      val losowyZamek = context.actorSelection("/user/Zamek*")
      losowyZamek ! Strzał
      
  }
}

class Zamek extends Actor with ActorLogging {
  val obrońcyZamkowi = for {
      i <- (1 to 100).toList} yield context.actorOf(Props[Obrońca](),s"${self.path.name}_obronca_$i") 
  def receive: Receive = zObrońcami(obrońcyZamkowi,self)
  def zObrońcami(obrońcy: List[ActorRef], obecnyZamek: ActorRef): Receive = {
    case Strzał => 
      if (obecnyZamek.path.name == "ZamekB") {
        val przeciwnik = context.actorSelection("/user/ZamekA")
        if (obrońcy.isEmpty) {
          log.info(s"Koniec bitwy! Wygrywa zamekA!")
          context.system.terminate()
        }
        else {
          for {
            o <- obrońcy
          } przeciwnik ! Strzała 
        }
      }
      else if (obecnyZamek.path.name == "ZamekA") {
        val przeciwnik = context.actorSelection("/user/ZamekB")
        if (obrońcy.isEmpty) {
          log.info(s"Koniec bitwy! Wygrywa zamekB!")
          context.system.terminate()
        }
        else {
          for {
            o <- obrońcy
          } przeciwnik ! Strzała
        }
         
      }
    case Strzała => 
      if (obrońcy.length > 0) {
        val czyTrafiony = Random.nextInt(101)
        val randomIndex = Random.nextInt(obrońcy.length)
        val losowyObronca = obrońcy(randomIndex)
        if (czyTrafiony > 75) {
          losowyObronca ! Strzała
        }
        else {
          losowyObronca ! Pudło
        }
      }
    case Dead(aktor) => 
      val nowiObrońcy = obrońcy.filter(_ != aktor)
      // log.info(s"${nowiObrońcy.length}") 
      context.become(zObrońcami(nowiObrońcy,self))
  }
  
}

class Obrońca extends Actor with ActorLogging {
  def receive: Receive = {
    case Strzała =>
      log.info(s"Dostałem i umieram...")
      sender() ! Dead(self)
      // context.stop(self)
      // self ! PoisonPill
    case Pudło => 
      log.info("Prosto w mur!") 
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
