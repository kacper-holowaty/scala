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


// nie tworzyć aktora typu grupa, chyba że chcę sobie utrudnić
// class Grupa(zawodnicy: List[ActorRef]) extends Actor with ActorLogging {
  // def receive: Receive = {
  //   case Grupa.Runda => 
  //     zawodnicy.head ! Zawodnik.Próba
  //     context.become(rozegraj(zawodnicy.tail))
  // }
  // def rozegraj(zawodnicy: List[ActorRef]): Receive = {
  //   case Some(Ocena(n1,n2,n3)) => 
  //     zawodnicy.head ! Zawodnik.Próba 
  //     context.become(rozgrywanieGrupy(zawodnicy.tail))
  // }
// }
