package jp1.akka_14_03.http.server

import akka.actor.ActorSystem
import akka.actor.Props
import akka.http.scaladsl.Http
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.pattern.ask
import akka.stream.Materializer
import akka.util.Timeout
import com.typesafe.scalalogging.Logger
import spray.json.DefaultJsonProtocol._
import spray.json.RootJsonFormat

import scala.concurrent.Await
import scala.concurrent.ExecutionContextExecutor
import scala.concurrent.Future
import scala.concurrent.duration._
import scala.util.Failure
import scala.util.Success



// „komunikaty” reprezentujące dane przekazywane przez HTTP
object Messages {
  case class Bid(userId: String, bid: Int)
  case class Bids(bids: List[Bid])
  case object GetBids
}
import Messages._

// poniższe „deserializatory” pochodzą ze spray-json
// Ogólnie jsonFormatN(T) daje nam deserializator
// dla dowolnej N-argumentowej klasy wzorcowej T.
implicit val bidFormat: RootJsonFormat[Bid] = jsonFormat2(Bid.apply)
implicit val bidsFormat: RootJsonFormat[Bids] = jsonFormat1(Bids.apply)

@main def go: Unit = {
  val logger = Logger("logger")
  implicit val system = ActorSystem()
  implicit val materializer: Materializer = Materializer(system)

  // Future potrzebuje „puli wątków” – np. następująca
  implicit val executionContext = system.dispatcher

  val auction = system.actorOf(Props[Auction](), "auction")

  /*
    1. definiujemy „interfejs” HTTP (korzystając z DSL modułu akka-http)
    2. „stawiamy” serwer
  */

  // ćwiczenie 1: wysyłać własny komunikat, jeśli ktoś odezwie się do nieznanej ścieżki
  // ćwiczenie 2: wysyłać własny komunikat, jeśli ktoś odezwie się nieobsługiwaną metodą HTTP
  val api: Route =
    // fraza „ ~ Slash.?” pozwala (opcjonalnie) korzystać z „/auction” oraz „/auction/”
    // bez niej tylko pierwsza forma będzie dopuszczalna
    path("auction" ~ Slash.?) {
      post {
        decodeRequest {
          entity(as[Bid]) { bid =>
            auction ! bid
            complete(StatusCodes.Accepted, "Twój bid został zarejestrowany")
          }
        }
      } ~
      get {
        implicit val timeout: Timeout = 10.seconds
        val bids: Future[Bids] = (auction ? GetBids).mapTo[Bids] // Future[Odpowiedź]
        complete(bids)
      }
    }

  // po jakim czasie przestaniemy czekać na „wyłączenie serwera” i zadziałamy „brutalnie”
  val shutdownDeadline: FiniteDuration = 5.seconds

  // konfiguracja parametrów serwera HTTP
  val config = system.settings.config // korzystamy z „application.conf”
  val serverInterface = config.getString("http-server.interface")
  val serverPort = config.getInt("http-server.port")

  // Definiujemy serwer
  Http()
    .newServerAt(serverInterface, serverPort)
    .bind(api)
    .map(_.addToCoordinatedShutdown(shutdownDeadline))
    .foreach { server =>
      logger.info(s"HTTP server running on: http://$serverInterface:$serverPort")

      server.whenTerminationSignalIssued.onComplete { _ =>
        logger.info("Shutdown of HTTP service initiated")
      }

      server.whenTerminated.onComplete {
        case Success(_) => logger.info("Shutdown of HTTP endpoint completed")
        case Failure(_) => logger.error("Shutdown of HTTP endpoint failed")
      }
    }

}

/*
  Po uruchomieniu serwera możemy go przetestować np. używając narzędzia HTTPie

  http POST localhost:8888/auction userId=Roman bid:=123
  http POST localhost:8888/auction userId=Tomasz bid:=127
  http POST localhost:8888/auction userId=Anna bid:=210

  Każdorazowo serwer odpowie „Twój bid został zarejestrowany”

  Następnie możemy sprawdzić historię

  http localhost:8888/auction

  Otrzymamy odpowiedź:

  {
    "bids": [
        {
            "bid": 123,
            "userId": "Roman"
        },
        {
            "bid": 127,
            "userId": "Tomasz"
        },
        {
            "bid": 210,
            "userId": "Anna"
        }
    ]
  }

*/


/*
    Komentarz do DSL:

    val route =
      A {
        B {
          C {
            // Route1
          } ~
          D {
            // Route2
          } ~
          // Route3
        } ~
        E {
          // Route4
        }
      }

    A, B, C, D, E – dyrektywy

    Route1 wykona się jeśli dyrektywy A, B, C przepuszczą żądanie.
    Route2 wykona się jeśli A, B, D przepuszczą,  a C – odrzuci żądanie.
    Route3 wykona się jeśli A i B przepiszczą, a C i D odrzucą żądanie.

 */
