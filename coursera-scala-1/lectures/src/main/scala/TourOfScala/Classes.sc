/*
 * Syntax for a function with private variables and getters and setters
 */
class Point {
  private var _x = 0
  private var _y = 0
  private val bound = 100

  def x = _x
  def x_= (newValue: Int): Unit = {
    if (newValue < bound) _x = newValue else printWarning
  }

  def y = _y
  def y_= (newValue: Int): Unit = {
    if (newValue < bound) _y = newValue else printWarning
  }

  def printWarning = println("WARNING: out of bounds")
}

def hw1(in1: String, in2: String): String = {
  in1 + in2
}

def hw2 = (in1: String, in2: String) => {
  in1 + in2
}

hw1("a","b")
hw2("a","b")

var p1 = new Point
p1.x = 3
p1.y = 4
p1.x


/*
 * Traits used as
 */

import scala.collection.mutable.ArrayBuffer

trait Pet {
  val name: String
}

class Cat(val name: String) extends Pet
class Dog(val name: String) extends Pet

val dog = new Dog("Harry")
val cat = new Cat("Sally")

val animals = ArrayBuffer.empty[Pet]
animals.append(dog)
animals.append(cat)
animals.foreach(pet => println(pet.name))