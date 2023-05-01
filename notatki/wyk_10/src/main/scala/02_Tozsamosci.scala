import akka.actor.Actor
import akka.actor.ActorSystem
import akka.actor.Props

// „Tożsamość” aktora to sposób jego reakcji na wiadomości
class MyActor extends Actor {
  def receive: Receive = { case msg =>
    println(s"Odebrałem wiadomość: ${msg}")
    // tozsamość można zmieniać wykorzystując „context.become(…)”
    context.become(nextStep(2))
  }

  def nextStep(msgCount: Int): Receive = { case msg =>
    println(s"Odebrałem wiadomość nr ${msgCount}: $msg")
    context.become(nextStep(msgCount + 1))
  }

}

// Uwaga – ten przykład (wyjątkowo) powinien być uruchamiany
//         z użyciem polecenia „run” (a nie reStart).
@main def main2: Unit = {
  val system = ActorSystem("HaloAkka")
  val leonardo = system.actorOf(Props[MyActor](), "leonardo")
  leonardo ! "Dostałeś Oskara!"
  leonardo ! "Dostałeś drugiego Oskara!"
  leonardo ! "Dostałeś trzeciego Oskara! Brawo!"
  println(">>> Naciśnij ENTER żeby zakończyć <<<")
  io.StdIn.readLine() // program czeka na znak nowej linii
  system.terminate()
}
