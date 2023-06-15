package jp1.akka_14_03.http.server

import akka.actor.{Actor, ActorLogging}

class Auction extends Actor with ActorLogging {
  import scala.collection.mutable.{ListBuffer => MList}
  import Messages._

  private val allBids = MList.empty[Bid]

  def receive: Receive = {
    case Bid(userId, bid) =>
      log.info("Bid({}, {}) received", userId, bid)
      // operacja „append” ( czyli += ) działa w czasie stałym
      // http://docs.scala-lang.org/overviews/collections/performance-characteristics.html
      allBids += Bid(userId, bid)
    case GetBids =>
      sender() ! Bids(allBids.toList)
  }
}
