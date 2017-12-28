import scala.io.Source

object pairs {
  def isPrime(n: Int) = {
    (2 until n) forall (n % _ != 0)
  }

  val n = 5
  val iSeq = (1 until n) map (i =>
    (1 until i) map (j =>
      (i, j)))

  // foldLeft takes the initial accumulator value, and a
  // function which has an accumulator and current value arg
  iSeq.foldLeft( List[(Int, Int)]() ) {
    (a, i ) => a ++ i}

  iSeq.foldLeft( List[(Int, Int)]() ) {
    (_ ++ _) }

  // flatten implicitely flattends the collection
  iSeq.flatten

  // (xs map f).flatten = xs flatmap f
  val iSeq2 =  (1 until n) flatMap (i =>
    (1 until i) map (j =>
      (i, j)))


  iSeq2.filter( pair => isPrime(pair._1 + pair._2 ) )

  /******************************************
  * For-Expression
    *
    * for ( p <- persons if p.age >20 ) yield p.name
    *
    * equivalent to
    *
    * persons filter (p => p.age >20) map (p => p.name)
    *
    * for (s) yield e
    *
    * s is a sequence of generators and filters
    *   - must start with a generator
    *
  *******************************************/

  for {
    i <- 1 until n
    j <- 1 until i
    if isPrime(i + j)
    k <- 1 until j+1
  } yield (i, j, k)




}