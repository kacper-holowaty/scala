import akka.actor.*
object Warsztat {
  case object Cyk
  case class Awaria(auto: ActorRef)
}
class Warsztat extends Actor with ActorLogging {
  import Warsztat._
  import util.Random
  def receive: Receive = {
    case Awaria(auto) => 
      // println(s"${self.path}")
      val senderRef = sender()
      val random = new Random()
      val naprawa = 0.8
      if (random.nextDouble() > naprawa) {
        senderRef ! Kierowca.WynikNaprawy(None)
      }
      else {
        val liczbaCykli = random.nextInt(6) + 1
        context.become(naprawianie(auto,0,liczbaCykli,senderRef))
      }
    case Cyk => 
      // println("Cyk w warsztacie")
  }
  def naprawianie(auto: ActorRef,counter: Int, liczbaCykli: Int, senderRef: ActorRef): Receive = {
    case Cyk => 
      if (liczbaCykli < counter) {
        log.info("Naprawiono!")
        senderRef ! Kierowca.WynikNaprawy(Some(auto))
        context.become(receive)
      }
      else {
        context.become(naprawianie(auto,counter+1,liczbaCykli,senderRef))
      }
  }
}
