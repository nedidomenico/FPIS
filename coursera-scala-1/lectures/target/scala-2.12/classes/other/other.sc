/* Syntax for higher order function */
def eq1(x: Int) = (y: Int) => (x == y)
// same as
def eq2(x: Int): Int => Boolean = (x == _)
// same as
def eq3(x: Int): Int => Boolean = x ==

(y: Int) => (1 == y)

(1 == _) : Int => Boolean

