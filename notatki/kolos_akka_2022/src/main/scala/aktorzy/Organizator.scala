import akka.actor.*

object Organizator {
  case class UstawMaksCyk(maksCyk: Int) {
    assert(maksCyk > 0)
  }
  case object Cyk
  case class PrzejechanaTrasa(liczbaKm: Int)
}
class Organizator extends Actor with ActorLogging {
  import Organizator._
  def receive = {
    case UstawMaksCyk(mc) =>
      log.info(s"liczba cyknięć do wykonania: $mc")
      // println(s"liczba cyknięć do wykonania: $mc")
      val warsztat = context.actorOf(Props[Warsztat](),"warsztat")
      val kierowcy = for {
        x <- (1 to 100)
      } yield context.actorOf(Props(new Kierowca(15, warsztat)),s"kierowca_$x")
      kierowcy.foreach(x => x ! Kierowca.PrzygotujAuto)
      context.become(poInicjalizacji(mc,0,kierowcy.toList,warsztat))
    case _ => // inne pomijamy
  }

  def poInicjalizacji(maksCyk: Int, liczbaCykniec: Int, kierowcy: List[ActorRef], warsztat: ActorRef): Receive = {
    case Cyk =>
      log.info("Cyk")
      if (liczbaCykniec > maksCyk) {
        log.info("Koniec wyścigu")
        kierowcy.foreach(x => x ! Kierowca.PodajTrasę)
        context.become(przyjmijWyniki(Nil,kierowcy))
      }
      else {
        warsztat ! Warsztat.Cyk
        kierowcy.foreach(x => x ! Kierowca.Cyk)
        context.become(poInicjalizacji(maksCyk, liczbaCykniec+1, kierowcy, warsztat))
      }
  }
  def przyjmijWyniki(akum: List[(ActorRef,Int)], kierowcy: List[ActorRef]): Receive = {
    case PrzejechanaTrasa(liczbaKm) => 
      val newList = (sender(),liczbaKm) :: akum
      if (newList.size == kierowcy.size) {
        val wyniki = newList.sortBy((aktor,wynik) => wynik).reverse.zipWithIndex
        val result = wyniki.foldLeft(List[((ActorRef,Int),Int)]())({
          case (akum,((aktor,wynik),index)) => akum match {
            case Nil => ((aktor,wynik),index+1) :: akum
            case ((_, wynikHead),id) :: _ if (wynik == wynikHead) => ((aktor,wynik),index) :: akum
            case ((_, wynikHead),id) :: _ => ((aktor,wynik),index+1) :: akum
          }
        }).reverse
        val readyToPrint = result.foldLeft("")({
          case (akum,((aktor,wynik),index)) => 
            akum + s"${index}. ${aktor.path.name} -> ${wynik} km\n"
        })
        println(readyToPrint)
        log.info(s"$readyToPrint")
        context.system.terminate()
      }
      else {
        context.become(przyjmijWyniki(newList,kierowcy))
      }
      
  }
}