package kolokwium_2

import akka.actor.{Actor, ActorLogging, Props, ActorRef}

abstract class DoPracownika
case class Wstaw(słowo: String) extends DoPracownika
case class Szukaj(słowo: String, szukane: String) extends DoPracownika


class Pracownik(letter: Char, actualWord: String) extends Actor with ActorLogging {
  def receive: Receive = zLicznikiem(0,Map(),Map())
  def zLicznikiem(liczbaSłów: Int, mapa: Map[Char, ActorRef], wystąpienia: Map[String, Int]): Receive = {
    case Wstaw(słowo) =>
      if (słowo.nonEmpty) {
        // log.info(s"$słowo")
        // log.info(s"$letter")
        val newActualWord = actualWord + letter
        // log.info(s"$newActualWord")
        if (słowo.tail.nonEmpty) {
          if (mapa.contains(słowo.tail.head)) {
            mapa(słowo.tail.head) ! Wstaw(słowo.tail)
          }
          else {
            val w = context.actorOf(Props(new Pracownik(słowo.tail.head,newActualWord)))
            w ! Wstaw(słowo.tail)
            val nowaMapa = mapa + (słowo.tail.head -> w)
            // log.info(s"$nowaMapa")
            context.become(zLicznikiem(liczbaSłów,nowaMapa,wystąpienia))
          }
        }
        else {
          // log.info(s"$słowo")
          // log.info(s"$newActualWord")
          if (wystąpienia.contains(newActualWord)) {
            val newLiczbaSłów = liczbaSłów+1
            val newWystąpienia = wystąpienia.updated(newActualWord,newLiczbaSłów)
            // log.info(s"$newWystąpienia")
            context.become(zLicznikiem(newLiczbaSłów,mapa,newWystąpienia))
          }
          else {
            val newLiczbaSłów = liczbaSłów+1
            val newWystąpienia = wystąpienia.updated(newActualWord,newLiczbaSłów)
            // log.info(s"$newWystąpienia")
            context.become(zLicznikiem(newLiczbaSłów,mapa,newWystąpienia))
          }
        }
      }
    case Szukaj(słowo,szukane) =>
      val szef = context.actorSelection("/user/szef") 
      if (słowo.nonEmpty) {
        if (słowo.tail.nonEmpty) {
          if (mapa.contains(słowo.tail.head)) {
            mapa(słowo.tail.head) ! Szukaj(słowo.tail,szukane)
          }
          else {
            szef ! Ile(szukane,0)
          }
        }
        else {
          if (wystąpienia.contains(szukane)) {
            szef ! Ile(szukane, wystąpienia(szukane))
          }
          else {
            szef ! Ile(szukane,0)
          }
        }  
      }
      
  }
}
