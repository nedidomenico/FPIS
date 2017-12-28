package recfun

//import scala.collection.JavaConverters._

object Main {
  def main(args: Array[String]) {
    println("Pascal's Triangle")
    for (row <- 0 to 10) {
      for (col <- 0 to row){
        if (col == 0) print(" " * (10 - row))
        print(pascal(col, row) + " ")
      }
      println()
    }

//    println(balance( List('(', 'i', 'f', ' ', '(', 'z', 'e', 'r', 'o', '?', ' ', 'x', ')', ' ', 'm', 'a', 'x', ' ', '(', '/', ' ', '1', ' ', 'x', ')', ')')))
//    println(balance( List('I',' ','t','o','l','d',' ','h','i','m',' ','(','t','h','a','t',' ','i','t','’','s',' ','n','o','t',' ','(','y','e','t',')',' ','d','o','n','e',')','.',' ','(','B','u','t',' ','h','e',' ','w','a','s','n','’','t',' ','l','i','s','t','e','n','i','n','g',')')))
//    println(balance( List(':','-',')')))
//    println(balance( List('(',')',')','(')))

    println(countChange(10,List(2,4,8)))
  }

  /**
    * Exercise 1
    */
  def pascal(c: Int, r: Int): Int = {
    if (c == 0 || c == r) 1
    else
      pascal(c-1,r-1) + pascal(c,r-1)
  }

  /**
    * Exercise 2
    */
  def balance(chars: List[Char]): Boolean = {
    def getNext(chars: List[Char]): List[Char] = {
      if (chars.isEmpty) chars
      else {
        var count: Int = 0
        while ( (chars(count) != '(') && (chars(count) != ')') ) {
          count+=1
        }
        chars.drop(count)
      }
    }

    def parKill (chars: List[Char]): List[Char] = {
//      println(chars.toString.substring(5,chars.toString.length-1))
      val chars2 = getNext(chars)
      if (chars2.isEmpty || chars2.head == ')') chars2
      else if (chars2.head == '(' && chars2(1) == ')') parKill(chars2.drop(2))
      else  {
        val rest = parKill(chars2.tail)
        parKill( List('(') ++ parKill(rest) )
      }
    }
    if (parKill(chars).isEmpty) true else false
  }

  /**
    * Exercise 3
    */
  def countChange(money: Int, coins: List[Int]): Int = {
    val smallCoin = coins.min
    val lessMoney = money - smallCoin
    var acc = 0
    if (lessMoney > 0) {
      acc += countChange(lessMoney, coins)
      if (coins.tail.nonEmpty) acc += countChange(money, coins.sortWith( _ < _).tail)
    }
    if (lessMoney  == 0) {
//      println(s"$acc $coins")
      1 + acc
    }
    else 0 + acc
  }

}
