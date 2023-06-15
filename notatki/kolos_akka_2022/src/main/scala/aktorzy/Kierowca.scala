import akka.actor.*

object Kierowca {
  case object Cyk
  case object PrzygotujAuto
  case class ReakcjaAuta(ov: Option[Int])
  case object PodajTrasę
  case class WynikNaprawy(efekt: Option[ActorRef])
}
class Kierowca(dt: Int,warsztat: ActorRef) extends Actor with ActorLogging {

  // val warsztat = context.actorSelection("/user/organizator/warsztat")
  import Kierowca._
  def receive: Receive = {
    case PrzygotujAuto => 
      log.info("Przygotowuje swoje auto")
      val car = context.actorOf(Props[Samochód](),"samochod")
      // context.watch(car)
      context.become(mamAuto(car,0))
  }
  def mamAuto(car: ActorRef,s: Int): Receive = {
    case Cyk => 
      car ! Samochód.Dalej
    // case ReakcjaAuta(None) => 
    //   log.info("Ups...awaria")
    //   warsztat ! Warsztat.Awaria(car)
    //   context.become(wWarsztacie(car,s))
    // case ReakcjaAuta(Some(v)) => 
    //   val dtInH = dt.toDouble/60
    //   val newS = s+(dtInH*v).toInt
    //   log.info(s"Samochód zawodnika ${self.path.name} porusza się z prędkością ${v} - do tej pory przebył ${newS} km")
    //   context.become(mamAuto(car,newS))
    case ReakcjaAuta(ov) =>
      ov match {
        case None => 
          log.info("Ups...awaria")
          warsztat ! Warsztat.Awaria(car)
          context.become(wWarsztacie(car,s))
        case Some(v) => 
          val dtInH = dt.toDouble/60
          val newS = s+(dtInH*v).toInt
          log.info(s"Samochód zawodnika ${self.path.name} porusza się z prędkością ${v} - do tej pory przebył ${newS} km")
          context.become(mamAuto(car,newS))
      }
    case PodajTrasę => 
      log.info("I am waiting")
      sender() ! Organizator.PrzejechanaTrasa(s)
    case _ => log.info("coś innego")
  }
  def wWarsztacie(car: ActorRef,s: Int): Receive = {
    case Cyk => 
    case WynikNaprawy(None) => 
      log.info(s"${self.path.name} - to dla mnie koniec wyścigu...")
      context.become(ukonczono(s))
    case WynikNaprawy(Some(auto)) =>
      log.info(s"${self.path.name} - Zostałem naprawiony") 
      context.become(mamAuto(auto,s)) 
    case PodajTrasę => 
      sender() ! Organizator.PrzejechanaTrasa(s)
  }
  def ukonczono(droga: Int): Receive = {
    case PodajTrasę => 
      sender() ! Organizator.PrzejechanaTrasa(droga)
  }
}
