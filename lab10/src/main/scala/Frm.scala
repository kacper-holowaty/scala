package lab10

/*
// zadanie 1 działa w ten sposób:
sealed trait Frm {
  private def toStr(f: Frm): String = f match {
    case False => "False"
    case True => "True"
    case Not(f) => s"!${toStr(f)}"
    case And(f1, f2) => s"(${toStr(f1)} & ${toStr(f2)})"
    case Or(f1, f2) => s"(${toStr(f1)} | ${toStr(f2)})"
    case Imp(f1, f2) => s"${toStr(f1)} -> ${toStr(f2)}"
  }
  override def toString: String = {
    toStr(this)
  }
}
case object False extends Frm
case object True extends Frm
case class Not(f: Frm) extends Frm
case class And(f1: Frm, f2: Frm) extends Frm
case class Or(f1: Frm,f2: Frm) extends Frm
case class Imp(f1: Frm, f2: Frm) extends Frm

val frm = Imp(Or(True, False), Not(And(True, False)))

// UWAGA: W rozwiązaniach poniższych zadań (tam gdzie to możliwe)
//        wykorzystaj rekurencję ogonową.

@main def zad_1: Unit = {
  println(frm)
}
*/
sealed trait Frm 
case object False extends Frm
case object True extends Frm
case class Not(f: Frm) extends Frm
case class And(f1: Frm, f2: Frm) extends Frm
case class Or(f1: Frm,f2: Frm) extends Frm
case class Imp(f1: Frm, f2: Frm) extends Frm

val frm = Imp(Or(True, False), Not(And(True, False)))

// UWAGA: W rozwiązaniach poniższych zadań (tam gdzie to możliwe)
//        wykorzystaj rekurencję ogonową.
// to do 
@main def zad_1: Unit = {
  println(frm)
  // Ćwiczenie 1: zaimplementuj toString tak, aby „minimalizowało”
  //              liczbę nawiasów
  // to będzie trzeba jakoś rekurencyjnie zrobić
  // var akum = ""
  def toString(frm: Frm, akum: String = ""): String = frm match {
    // case Imp(f1,f2) => 
    //   // s"${f1} -> ${f2}"
    //   (f1,f2) match {
    //     case (Or(t1,f1), And(t2,f2)) => s"($t1 | $f1) -> ($t2 & $f2)"
    //     case (Or(t1,f1), Not(And(t2,f2))) => s"($t1 | $f1) -> !($t2 & $f2)"
    //     case (Not(Or(t1,f1)), And(t2,f2)) => s"!($t1 | $f1) -> ($t2 & $f2)"
    //     case (Not(Or(t1,f1)), Not(And(t2,f2))) => s"!($t1 | $f1) -> !($t2 & $f2)"
    //     case _ => "aqq" 
    //   }
    // case _ => "aqq" 
    case Imp(formula1,formula2) => 
      // akum = s"$formula1 -> $formula2" 
      /*(f1,f2) match {
        case (True | False, True | False) => akum
        case (Or(t1,f1), And(t2,f2)) => /*s"($t1 | $f1) -> ($t2 & $f2)"*/toString(akum)
        case (Or(t1,f1), Not(And(t2,f2))) => s"($t1 | $f1) -> !($t2 & $f2)"
        case (Not(Or(t1,f1)), And(t2,f2)) => s"!($t1 | $f1) -> ($t2 & $f2)"
        case (Not(Or(t1,f1)), Not(And(t2,f2))) => s"!($t1 | $f1) -> !($t2 & $f2)"
        case _ => "aqq"
      }*/
      formula1 match {
        case (True | False) => akum
        case (Or(f1,f2)) => toString(formula1,s"")
        case (And(f1,f2)) => toString(formula1)
        case (Not(f1)) => toString(formula1)
        case (Imp(f1,f2)) => toString(formula1)
        // case _ => "aqq"
      }
      formula2 match {
        case (True | False) => akum
        case (Or(f1,f2)) => toString(formula2)
        case (And(f1,f2)) => toString(formula2)
        case (Not(f)) => toString(formula2)
        case (Imp(f1,f2)) => toString(formula2)
        // case _ => "aqq"
      }
      case _ => "aqq"
  }
  println(toString(frm))
  // Powinno wyprodukować coś „w stylu”:
  // (True | False) -> !(True & False)
}

@main def zad_2: Unit = {
  // Ćwiczenie 2: zaimplementuj funkcję wyliczającą wartość logiczną
  //              formuły.
  def eval(f: Frm): Boolean = ???
  // eval(False) == false
  // eval(frm) == true
}

@main def zad_3: Unit = {
  // Ćwiczenie 3: napisz funkcję wyliczającą dla danej formuły f
  //              zbiór jej wszystkich „podformuł”
  def closure(f: Frm): Set[Frm] = ???

  // np. dla formuły frm powyżej wynikiem powinien być zbiór:
  //
  // { True, False, True | False, True & False, !(True & False), frm }
  //
  // Jak widać, closure(frm) zawiera również formułę będącą argumentem.
}