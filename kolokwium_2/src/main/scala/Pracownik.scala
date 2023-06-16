package kolokwium_2

import akka.actor.{Actor, ActorLogging, Props}

abstract class DoPracownika
case class Wstaw(słowo: String) extends DoPracownika
case class Szukaj(słowo: String, basic: String, counter: Int=0, actual: List[String]=dane) extends DoPracownika
case class AlterWstaw(słowo: String, counter: Int) extends DoPracownika

class Pracownik extends Actor with ActorLogging {
  def receive: Receive = zDanymi(dane)
  def zDanymi(data: List[String]): Receive = {
    case Wstaw(słowo) => log.info("to duuu")
    case AlterWstaw(słowo,counter) => 
      if (słowo.nonEmpty) {
        val first = context.actorOf(Props[Pracownik](),"w")
        val newData = data.filter(n => n(counter) == słowo.head)
        context.become(zDanymi(data))
        first ! AlterWstaw(słowo.tail,counter+1)
      } 
      else {
        log.info("Koniec słowa")
        val newData = data :+ słowo
        context.become(zDanymi(newData))
        val szef = context.actorSelection("/user/szef")
      }
    case Szukaj(słowo,basic,counter,data) => 
      if (słowo.nonEmpty) {
        val first = context.actorOf(Props[Pracownik](),"w")
        val newData = data.filter(n => n(counter) == słowo.head)
        first ! Szukaj(słowo.tail,basic,counter+1,newData)
      } 
      else {
        val lista = data.filter(n => n == basic)
        val len = lista.length
        val szef = context.actorSelection("/user/szef")
        szef ! Ile(basic,len)  
      }
  }
}
