import akka.actor.*
object Samochód {
  case object Dalej
}
class Samochód extends Actor with ActorLogging {
  import Samochód._
  import util.Random
  def receive: Receive = {
    case Dalej =>
      val senderRef = sender()
      val random = new Random()
      val awaria = 0.15
      if (random.nextDouble() < awaria) {
        senderRef ! Kierowca.ReakcjaAuta(None)
      }
      else {
        val prędkość = random.nextInt(200) + 1
        senderRef ! Kierowca.ReakcjaAuta(Some(prędkość))
      }
  }
}
