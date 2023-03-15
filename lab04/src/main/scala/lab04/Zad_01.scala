package lab04

def ciąg(n: Int): Int = {
  @annotation.tailrec
  def oblicz(n: Int, akum1: Int, akum2: Int): Int = n match {
    case 1 => akum1
    case 2 => akum2
    case _ => oblicz(n-1,akum2,akum2+akum1) 
  }
  oblicz(n,2,1)
}

@main 
def zadanie_01: Unit = {
  // Program powinien umożliwić „sprawdzenie” działania
  // funkcji „ciąg”.
  println(ciąg(10))
}
