package jp1.akka.lab13.model

import akka.actor.{Actor, ActorRef, ActorLogging, Props}

val akkaPathAllowedChars = ('a' to 'z').toSet union
  ('A' to 'Z').toSet union
  "-_.*$+:@&=,!~';.)".toSet

object Organizator {
  case object Start
  // rozpoczynamy zawody – losujemy 50 osób, tworzymy z nich zawodników
  // i grupę eliminacyjną
  case object Runda
  // polecenie rozegrania rundy (kwalifikacyjnej bądź finałowej) –  wysyłamy Grupa.Runda
  // do aktualnej grupy
  case object Wyniki
  // polecenie wyświetlenia klasyfikacji dla aktualnej grupy
  case class Wyniki(w: Map[ActorRef, Option[Ocena]])
  // wyniki zwracane przez Grupę
  case object Stop
  // kończymy działanie
}

class Organizator extends Actor with ActorLogging {
  // importujemy komunikaty na które ma reagować Organizator
  import Organizator._

  def receive: Receive = {
    case Start =>
      // tworzenie 50. osób, opdowiadających im Zawodników
      // oraz Grupy eliminacyjnej
      val zawodnicy = List.fill(50) {
        val o = Utl.osoba()
        context.actorOf(Props(Zawodnik(o)), s"${o.imie}-${o.nazwisko}" filter akkaPathAllowedChars)
      }
      log.info(s"Do eliminacji przystąpi ${zawodnicy.length} zawodników")
      context.become(eliminacje(zawodnicy))
    case Stop =>
      log.info("kończymy zawody...")
      context.system.terminate()
  }
  def eliminacje(zawodnicy: List[ActorRef]): Receive = {
    case Runda =>   
      val grupaEliminacyjna = context.actorOf(Props(new Grupa(zawodnicy)),"grupa_eliminacyjna")
      grupaEliminacyjna ! Grupa.Runda
    case Wyniki(w) => 
      log.info("Runda eliminacyjna zakończona! Aby uzyskać wyniki wpisz 'wyniki'")
      context.become(toFinal(w))
    case Stop =>
      log.info("kończymy zawody...")
      context.system.terminate()
  }
  def toFinal(wyniki: Map[ActorRef, Option[Ocena]]): Receive = {
    case Wyniki => 
      val bezNone = wyniki.filter((k,v) => v != None).toList
      val result = sortResults(bezNone)
      printResults(result)
    case Runda =>
      val bezNone = wyniki.filter((k,v) => v != None).toList
      val result = sortResults(bezNone)
      val toFinal = result.filter((aktor, ocena, id, suma) => id <= 20)
      val zawodnicyFinalowi = toFinal.map((aktor,ocena,id,suma) => aktor)
      val ocenyRundaEliminacyjna = toFinal.map((aktor,ocena,id,suma) => ocena)
      val grupaFinalowa = context.actorOf(Props(new Grupa(zawodnicyFinalowi)),"grupaFinalowa")  
      grupaFinalowa ! Grupa.Runda
    case Wyniki(finalowaPunktacja) =>
      log.info("Runda finałowa zakończona! Aby uzyskać wyniki wpisz 'wyniki'")
      context.become(finalResults(finalowaPunktacja,wyniki))
    case Stop =>
      log.info("kończymy zawody...")
      context.system.terminate()
  }
  def finalResults(finalowaPunktacja: Map[ActorRef, Option[Ocena]],eliminacyjnaPunktacja: Map[ActorRef, Option[Ocena]]): Receive = {
    case Wyniki => 
      val finalowa = finalowaPunktacja.toList
      val eliminacyjna = eliminacyjnaPunktacja.toList
      val zip2list = finalowa.zip(eliminacyjna)
      val wspolna = zip2list.map({case ((aktor1,ocena1),(aktor2,ocena2)) => (aktor1,ocena1,ocena2)})
      val oceny = wspolna.map({
        case (aktor,Some(Ocena(n1,n2,n3)),Some(Ocena(m1,m2,m3))) => (aktor,Some(Ocena(n1+m1,n2+m2,n3+m3)))
        case (aktor,None,Some(Ocena(n1,n2,n3))) => (aktor,Some(Ocena(n1,n2,n3)))
        case (aktor,_,_) => (aktor,None)
      })
      val result = sortResults(oceny)
      printResults(result)
    case Stop =>
      log.info("kończymy zawody...")
      context.system.terminate()
  } 
  def sortResults(lista: List[(ActorRef, Option[Ocena])]): List[(ActorRef,Option[Ocena],Int,Int)] = {
    val posortowane = lista.sortWith({
        case ((_, Some(ocena1)),(_,Some(ocena2))) => {
          if (ocena1._1 + ocena1._2 + ocena1._3 == ocena2._1 + ocena2._2 + ocena2._3) {
            if (ocena1._1 == ocena2._1) {
              ocena1._3 > ocena2._3
            }
            else {
              ocena1._1 > ocena2._1
            }
          }
          else {
            ocena1._1 + ocena1._2 + ocena1._3 > ocena2._1 + ocena2._2 + ocena2._3
          }
        } 
        case _ => false
      })
      val posortowaneZindeksem = posortowane.zipWithIndex.map({
        case ((aktor,Some(ocena)),id) => (aktor,Some(ocena),id,ocena._1+ocena._2+ocena._3)
        case ((aktor, _), id) => (aktor, None, id, 0)
      })
      val result = posortowaneZindeksem.foldLeft(List[(ActorRef,Option[Ocena],Int,Int)]())({
        case (akum,(aktor,Some(ocena),id,suma)) => akum match {
          case Nil => (aktor,Some(ocena),id+1,suma) :: akum
          case (_,Some(ocenaHead), _, sumaHead) :: _ if (suma == sumaHead && ocena._1 == ocenaHead._1 && ocena._3 == ocenaHead._3) => (aktor,Some(ocena),id,suma) :: akum
          case (_,Some(ocenaHead), _, sumaHead) :: _ => (aktor,Some(ocena),id+1,suma) :: akum
          case _ => akum
        }
        case _ => Nil
      }).reverse
      result
  } 
  def printResults(lista: List[(ActorRef, Option[Ocena], Int, Int)]): Unit = {
     val niceResults = lista.foldLeft("")({case (akum,(aktor,Some(ocena),id,suma)) => {
        val a = s"${aktor.path.name}"
        val nazwisko = a.split("-").map(_.capitalize).mkString(" ")
        akum + s"${id}. ${nazwisko} - ${ocena._1}-${ocena._2}-${ocena._3} = ${suma} \n"
      }
      case _ => ""})
      println(s"\n$niceResults")
  }
}