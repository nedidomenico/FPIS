package ned

object Main extends App {
  val problem = new Pouring(Vector(4, 7))
  print( "\n\n" + problem.moves)
  print( "\n\n" + problem.solutions(6) )
}