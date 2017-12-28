/* Syntax for higher order function */
def eq1(x: Int) = (y: Int, z: Int) => (x == y*z)
// same as
def eq2(x: Int): Int => Boolean = (x == _)
// same as
def eq3(x: Int): Int => Boolean = x ==

/* Syntax for anonymous function */
(y: Int) => (1 == y)
// same as
(6 == _*_) : (Int, Int) => Boolean
// same as
(1 ==) : Int => Boolean

/*
 * Pattern matching example. Tail recursive function.
 */
def maxChar(list: List[(Char, Int)], char: Char): List[(Char, Int)]
= list match {
  case Nil => List((char, -1))
  case (c, _) :: xs => if (c > char) maxChar(xs, c) else maxChar(xs, char)
}

eq1(8)(4,2)
