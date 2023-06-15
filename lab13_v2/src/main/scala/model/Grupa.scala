package jp1.akka.lab13.model

import akka.actor.{Actor, ActorRef, ActorLogging}

object Grupa {
  case object Runda
  // Zawodnicy mają wykonać swoje próby – Grupa
  // kolejno (sekwencyjnie) informuje zawodników
  // o konieczności wykonania próby i „oczekuje”
  // na ich wynik (typu Option[Ocena])
  case object Wyniki
  // Polecenie zwrócenia aktualnego rankingu Grupy
  // Oczywiście klasyfikowani są jedynie Zawodnicy,
  // którzy pomyślnie ukończyli swoją próbę
  case class Wynik(ocena: Option[Ocena])
  // Informacja o wyniku Zawodnika (wysyłana przez Zawodnika do Grupy)
  // np. Wynik(Some(Ocena(10, 15, 14)))
  // Jeśli zawodnik nie ukończy próby zwracana jest wartość Wynik(None)
  case object Koniec
  // Grupa kończy rywalizację
}

class Grupa(zawodnicy: List[ActorRef]) extends Actor with ActorLogging {
  def receive: Receive = {
    case Grupa.Runda =>
      zawodnicy.head ! Zawodnik.Próba
      context.become(przyjmujWyniki(zawodnicy,Map(),zawodnicy.length))
  }
  def przyjmujWyniki(zawodnicy: List[ActorRef],akum: Map[ActorRef, Option[Ocena]], maks: Int): Receive = {
    case Grupa.Wynik(ocena) => 
      val newMap = akum + (sender() -> ocena)
      if (newMap.size == maks) {
        context.parent ! Organizator.Wyniki(newMap)
      }
      else {
        zawodnicy.head ! Zawodnik.Próba
        context.become(przyjmujWyniki(zawodnicy.tail, newMap, maks))
      }
  }
}
