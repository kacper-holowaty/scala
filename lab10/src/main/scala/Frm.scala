package lab10

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

@main def zad_1: Unit = {
  // Ćwiczenie 1: zaimplementuj toString tak, aby „minimalizowało”
  //              liczbę nawiasów
  
  def toString(accTrue: Boolean => String, accFalse: Boolean => String)(f: Frm): String = f match {
    case False => "False"
    case True => "True"
    case Not(x) => s"!${toString(accFalse, accTrue)(x)}"
    case And(x, y) => s"(${toString(accTrue, accFalse)(x)} & ${toString(accTrue, accFalse)(y)})"
    case Or(x, y) => s"(${toString(accTrue, accFalse)(x)} | ${toString(accTrue, accFalse)(y)})"
    case Imp(x, y) => s"(${toString(accFalse, accTrue)(x)} -> ${toString(accTrue, accFalse)(y)})"
  }

  println(toString(_ => "True", _ => "False")(frm))

  // Powinno wyprodukować coś „w stylu”:
  // (True | False) -> !(True & False)
}

@main def zad_2: Unit = {
  // Ćwiczenie 2: zaimplementuj funkcję wyliczającą wartość logiczną
  //              formuły.
  
  def eval(frm: Frm): Boolean = frm match {
    case False => false
    case True => true
    case Not(f) => !eval(f)
    case And(f1, f2) => eval(f1) && eval(f2)
    case Or(f1, f2) => eval(f1) || eval(f2)
    case Imp(f1, f2) => !eval(f1) || eval(f2)
  }

  // val frm1 = And(Not(Or(True, False)), And(True,Not(And(True, Or(True,False)))))
  println(eval(frm) == true)
  println(eval(False) == false)
}

@main def zad_3: Unit = {
  // Ćwiczenie 3: napisz funkcję wyliczającą dla danej formuły f
  //              zbiór jej wszystkich „podformuł”
  def closure(f: Frm) : Set[Frm] = f match {
    case True | False => Set(f)
    case Not(f1) => closure(f1) + f
    case And(f1, f2) => closure(f1) ++ closure(f2) + f
    case Or(f1, f2) => closure(f1) ++ closure(f2) + f
    case Imp(f1, f2) => closure(f1) ++ closure(f2) + f
  }
  println(closure(frm))
  // np. dla formuły frm powyżej wynikiem powinien być zbiór:
  //
  // { True, False, True | False, True & False, !(True & False), frm }
  //
  // Jak widać, closure(frm) zawiera również formułę będącą argumentem.
}