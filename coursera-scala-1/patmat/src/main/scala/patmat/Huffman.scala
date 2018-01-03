package patmat

import common._

/**
  * Assignment 4: Huffman coding
  *
  */
object Huffman {

  def main(args: Array[String]): Unit = {
    def printLeaves(myList: List[(Char, Int)]): Unit = {
      for (a <- myList) {
        println("(" + a._1 + ", " + a._2 + ")")
      }
    }

    def printTree(tree: CodeTree, tabs: String): Unit = {
      print(tabs)
      tree match {
        case Fork(l, r, c, w) => {
          print("( ")
          for (i <- c) {
            print(i + " ")
          }
          print(", " + w + ")")
          print("\n")
          printTree(l, tabs + "\t")
          print("\n")
          printTree(r, tabs + "\t")
        }
        case Leaf(a, b) => print("(" + a + ", " + b + ")")
      }
    }

    def printTreeList(tList: List[CodeTree]) = {
      for (t <- tList) {
        print(" \n----------------------\n ")
        printTree(t, "")
      }
    }

    val leafA = Leaf('a', 1)
    val leafB = Leaf('b', 1)
    val leafC = Leaf('c', 1)
    val leafD = Leaf('d', 1)
    val leafE = Leaf('e', 1)
    val leafF = Leaf('f', 1)

    val myTree: Fork = Fork(Leaf('a', 1), Leaf('b', 2), List('a', 'b'), 3)
    val myTree2: Fork = Fork(Leaf('c', 2), Leaf('d', 4), List('c', 'd'), 6)
    val myTree3: Fork = Fork(Leaf('e', 3), Leaf('f', 4), List('e', 'f'), 7)
    val myTreeAll: Fork = Fork(myTree, myTree2, List('a', 'b', 'c', 'd'), 9)

    val treeList = List(myTree, myTree2, myTree3)
    val treeList2 = combine(treeList)
    val treeList3 = combine(treeList2)
    val code = List(0, 0, 0, 1, 1, 0, 1, 1)
    printTree(myTreeAll, "")
    println("\n\n==================================")
    print(decode(myTreeAll, code))

  }

  /**
    * A huffman code is represented by a binary tree.
    *
    * Every `Leaf` node of the tree represents one character of the alphabet that the tree can encode.
    * The weight of a `Leaf` is the frequency of appearance of the character.
    *
    * The branches of the huffman tree, the `Fork` nodes, represent a set containing all the characters
    * present in the leaves below it. The weight of a `Fork` node is the sum of the weights of these
    * leaves.
    */
  abstract class CodeTree

  case class Fork(left: CodeTree, right: CodeTree, chars: List[Char], weight: Int) extends CodeTree

  case class Leaf(char: Char, weight: Int) extends CodeTree


  // Part 1: Basics
  def weight(tree: CodeTree): Int = {
    tree match {
      case Fork(_, _, _, weight) => weight
      case Leaf(_, weight) => weight
    }
  }

  def chars(tree: CodeTree): List[Char] = {
    tree match {
      case Fork(_, _, chars, _) => chars
      case Leaf(char, _) => List(char)
    }
  }

  def makeCodeTree(left: CodeTree, right: CodeTree) =
    Fork(left, right, chars(left) ::: chars(right), weight(left) + weight(right))

  // Part 2: Generating Huffman trees
  /**
    * In this assignment, we are working with lists of characters. This function allows
    * you to easily create a character list from a given string.
    */
  def string2Chars(str: String): List[Char] = str.toList

  /**
    * This function computes for each unique character in the list `chars` the number of
    * times it occurs. For example, the invocation
    *
    * times(List('a', 'b', 'a'))
    *
    * should return the following (the order of the resulting list is not important):
    *
    * List(('a', 2), ('b', 1))
    *
    * The type `List[(Char, Int)]` denotes a list of pairs, where each pair consists of a
    * character and an integer. Pairs can be constructed easily using parentheses:
    *
    * val pair: (Char, Int) = ('c', 1)
    *
    * In order to access the two elements of a pair, you can use the accessors `_1` and `_2`:
    *
    * val theChar = pair._1
    * val theInt  = pair._2
    *
    * Another way to deconstruct a pair is using pattern matching:
    *
    * pair match {
    * case (theChar, theInt) =>
    * println("character is: "+ theChar)
    * println("integer is  : "+ theInt)
    * }
    */
  def times(chars: List[Char]): List[(Char, Int)] = {

    def updateList(list: List[(Char, Int)], char: Char): List[(Char, Int)] = list match {
      case Nil => List((char, 1))
      case (c, t) :: xs => if (c == char) (c, t + 1) :: xs else (c, t) :: updateList(xs, char)
    }

    def timesAcc(chars: List[Char], acc: List[(Char, Int)]): List[(Char, Int)] = chars match {
      case Nil => acc
      case c :: xs => timesAcc(xs, updateList(acc, c))
    }

    timesAcc(chars, Nil)
  }


  def makeOrderedLeafList(freqs: List[(Char, Int)]): List[Leaf] = {

    def sort(freqs: List[(Char, Int)], sorted: List[Leaf]): List[Leaf] = {

      def insert(elem: (Char, Int), list: List[Leaf]): List[Leaf] = {
        val leaf = Leaf(elem._1, elem._2)
        if (list.isEmpty) List(leaf)
        else if (leaf.weight < list.head.weight) leaf :: list
        else list.head :: insert(elem, list.tail)
      }

      freqs match {
        case Nil => sorted
        case x :: xs => insert(x, sort(xs, sorted))
      }
    }

    sort(freqs, List())
  }
 /**
    * Checks whether the list `trees` contains only one single code tree.
    */
  def singleton(trees: List[CodeTree]): Boolean = {
    trees.length == 1
  }

  /**
    * The parameter `trees` of this function is a list of code trees ordered
    * by ascending weights.
    *
    * This function takes the first two elements of the list `trees` and combines
    * them into a single `Fork` node. This node is then added back into the
    * remaining elements of `trees` at a position such that the ordering by weights
    * is preserved.
    *
    * If `trees` is a list of less than two elements, that list should be returned
    * unchanged.
    */
  def combine(trees: List[CodeTree]): List[CodeTree] = trees match {
    case Nil => Nil
    case x :: Nil => trees
    case x :: y :: zs => List(makeCodeTree(x, y)) ::: zs
  }
//  def combine(trees: List[CodeTree]): List[CodeTree] = {
//    if (trees.isEmpty || trees.tail.isEmpty) trees else {
//      val a = trees.head
//      val tail1 = trees.tail
//      val b = tail1.head
//      val tail2 = tail1.tail
//      val outFork = makeCodeTree(a, b)
//      val (x, y) = tail2.partition(x => weight(x) < weight(outFork))
//      x ++ List(outFork) ++ y
//    }
//  }

  /**
    * This function will be called in the following way:
    *
    * until(singleton, combine)(trees)
    *
    * where `trees` is of type `List[CodeTree]`, `singleton` and `combine` refer to
    * the two functions defined above.
    *
    * In such an invocation, `until` should call the two functions until the list of
    * code trees contains only one single tree, and then return that singleton list.
    *
    * Hint: before writing the implementation,
    *  - start by defining the parameter types such that the above example invocation
    * is valid. The parameter types of `until` should match the argument types of
    * the example invocation. Also define the return type of the `until` function.
    *  - try to find sensible parameter names for `xxx`, `yyy` and `zzz`.
    */
  def until(sing: List[CodeTree] => Boolean, comb: List[CodeTree] => List[CodeTree])
           (inList: List[CodeTree]): List[CodeTree] = {
    if (sing(inList)) inList
    else until(sing, comb)(comb(inList))
  }

  /**
    * This function creates a code tree which is optimal to encode the text `chars`.
    *
    * The parameter `chars` is an arbitrary text. This function extracts the character
    * frequencies from that text and creates a code tree based on them.
    */
  def createCodeTree(chars: List[Char]): CodeTree = {
    until(singleton, combine)(makeOrderedLeafList(times(chars))).last
  }

  // Part 3: Decoding
  type Bit = Int

  /**
    * This function decodes the bit sequence `bits` using the code tree `tree` and returns
    * the resulting list of characters.
    */
  def decode(tree: CodeTree, bits: List[Bit]): List[Char] = {
    def subdecode(subtree: CodeTree, bits: List[Bit], text: List[Char]): List[Char] = {
      subtree match {
        case Leaf(c, _) =>
          val currentText = text ::: List(c)
          if (bits.isEmpty) currentText else subdecode(tree, bits, currentText)
        case Fork(l, r, _, _) =>
          if (bits.head == 0)
            subdecode(l, bits.tail, text)
          else
            subdecode(r, bits.tail, text)
      }
    }

    subdecode(tree, bits, List())

  }

  /**
    * A Huffman coding tree for the French language.
    * Generated from the data given at
    * http://fr.wikipedia.org/wiki/Fr%C3%A9quence_d%27apparition_des_lettres_en_fran%C3%A7ais
    */
  val frenchCode: CodeTree = Fork(Fork(Fork(Leaf('s', 121895), Fork(Leaf('d', 56269), Fork(Fork(Fork(Leaf('x', 5928), Leaf('j', 8351), List('x', 'j'), 14279), Leaf('f', 16351), List('x', 'j', 'f'), 30630), Fork(Fork(Fork(Fork(Leaf('z', 2093), Fork(Leaf('k', 745), Leaf('w', 1747), List('k', 'w'), 2492), List('z', 'k', 'w'), 4585), Leaf('y', 4725), List('z', 'k', 'w', 'y'), 9310), Leaf('h', 11298), List('z', 'k', 'w', 'y', 'h'), 20608), Leaf('q', 20889), List('z', 'k', 'w', 'y', 'h', 'q'), 41497), List('x', 'j', 'f', 'z', 'k', 'w', 'y', 'h', 'q'), 72127), List('d', 'x', 'j', 'f', 'z', 'k', 'w', 'y', 'h', 'q'), 128396), List('s', 'd', 'x', 'j', 'f', 'z', 'k', 'w', 'y', 'h', 'q'), 250291), Fork(Fork(Leaf('o', 82762), Leaf('l', 83668), List('o', 'l'), 166430), Fork(Fork(Leaf('m', 45521), Leaf('p', 46335), List('m', 'p'), 91856), Leaf('u', 96785), List('m', 'p', 'u'), 188641), List('o', 'l', 'm', 'p', 'u'), 355071), List('s', 'd', 'x', 'j', 'f', 'z', 'k', 'w', 'y', 'h', 'q', 'o', 'l', 'm', 'p', 'u'), 605362), Fork(Fork(Fork(Leaf('r', 100500), Fork(Leaf('c', 50003), Fork(Leaf('v', 24975), Fork(Leaf('g', 13288), Leaf('b', 13822), List('g', 'b'), 27110), List('v', 'g', 'b'), 52085), List('c', 'v', 'g', 'b'), 102088), List('r', 'c', 'v', 'g', 'b'), 202588), Fork(Leaf('n', 108812), Leaf('t', 111103), List('n', 't'), 219915), List('r', 'c', 'v', 'g', 'b', 'n', 't'), 422503), Fork(Leaf('e', 225947), Fork(Leaf('i', 115465), Leaf('a', 117110), List('i', 'a'), 232575), List('e', 'i', 'a'), 458522), List('r', 'c', 'v', 'g', 'b', 'n', 't', 'e', 'i', 'a'), 881025), List('s', 'd', 'x', 'j', 'f', 'z', 'k', 'w', 'y', 'h', 'q', 'o', 'l', 'm', 'p', 'u', 'r', 'c', 'v', 'g', 'b', 'n', 't', 'e', 'i', 'a'), 1486387)

  /**
    * What does the secret message say? Can you decode it?
    * For the decoding use the 'frenchCode' Huffman tree defined above.
    */
  val secret: List[Bit] = List(0, 0, 1, 1, 1, 0, 1, 0, 1, 1, 1, 0, 0, 1, 1, 0, 1, 0, 0, 1, 1, 0, 1, 0, 1, 1, 0, 0, 1, 1, 1, 1, 1, 0, 1, 0, 1, 1, 0, 0, 0, 0, 1, 0, 1, 1, 1, 0, 0, 1, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 1)

  /**
    * Write a function that returns the decoded secret
    */
  def decodedSecret: List[Char] = decode(frenchCode, secret)


  // Part 4a: Encoding using Huffman tree

  /**
    * This function encodes `text` using the code tree `tree`
    * into a sequence of bits.
    */
  def encode(tree: CodeTree)(text: List[Char]): List[Bit] = {

    def charExistsInBranch(branch: CodeTree, char: Char): Boolean = branch match {
      case Leaf(c, _) => c == char
      case Fork(_, _, c, _) => c.contains(char)
    }

    def subencode(subtree: CodeTree, text: List[Char], code: List[Bit]): List[Bit] = {
      if (text.isEmpty) code
      else subtree match {
        case Leaf(c, _) => subencode(tree, text.tail, code)
        case Fork(l, r, _, _) =>
          if (charExistsInBranch(l, text.head))
            subencode(l, text, code ::: List(0))
          else
            subencode(r, text, code ::: List(1))
      }
    }

    subencode(tree, text, Nil)
  }

  // Part 4b: Encoding using code table

  type CodeTable = List[(Char, List[Bit])]

  /**
    * This function returns the bit sequence that represents the character `char` in
    * the code table `table`.
    */
  def codeBits(table: CodeTable)(char: Char): List[Bit] = table match {
    case Nil => throw new NoSuchElementException
    case (c, code) :: xs => if (c == char) code else codeBits(xs)(char)
  }

  /**
    * Given a code tree, create a code table which contains, for every character in the
    * code tree, the sequence of bits representing that character.
    *
    * Hint: think of a recursive solution: every sub-tree of the code tree `tree` is itself
    * a valid code tree that can be represented as a code table. Using the code tables of the
    * sub-trees, think of how to build the code table for the entire tree.
    */
  def convert(tree: CodeTree): CodeTable = {

    def convertRec(tree: CodeTree, currentCode: List[Bit]): CodeTable = tree match {
      case Leaf(c, _) => List((c, currentCode))
      case Fork(l, r, _, _) => convertRec(l, currentCode ::: List(0)) ::: convertRec(r, currentCode ::: List(1))
    }

    convertRec(tree, List())
  }

  /**
    * This function takes two code tables and merges them into one. Depending on how you
    * use it in the `convert` method above, this merge method might also do some transformations
    * on the two parameter code tables.
    */
  def mergeCodeTables(a: CodeTable, b: CodeTable): CodeTable = a ::: b

  /**
    * This function encodes `text` according to the code tree `tree`.
    *
    * To speed up the encoding process, it first converts the code tree to a code table
    * and then uses it to perform the actual encoding.
    */
  def quickEncode(tree: CodeTree)(text: List[Char]): List[Bit] = {

    val codeTable: CodeTable = convert(tree)

    def quickEncodeRec(text: List[Char], code: List[Bit]): List[Bit] = text match {
      case Nil => code
      case x :: xs => quickEncodeRec(text.tail, code ::: codeBits(codeTable)(x))
    }

    quickEncodeRec(text, Nil)
  }
}
