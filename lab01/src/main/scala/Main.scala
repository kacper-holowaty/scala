@main def example: Unit = {
  println("Hello World!")
}
@main def example2: Unit = {
  println("Goodbye World!")
}
@main def example3: Unit = {
  var x = 1
  while (x < 10) {
    print("x") //gdy println("x") ==> każdy x w innej linijce
    x = x+1
  }
  println("X")
}
var helloStr = "Hello, World!"

@main def example4(n: Int): Unit = {
  val x: Int = 2
  println(s"Commandline argument: $n, ${n*x}")
}

@main def example5(n: Int): Unit = {
  val rand = scala.util.Random()
  val liczba = rand.nextInt(100)
  if (liczba > n) println(s"Wygrałeś! Wylosowana liczba to $liczba.")
  else println(s"Przegrałeś! Wylosowana liczba to $liczba.")
}
