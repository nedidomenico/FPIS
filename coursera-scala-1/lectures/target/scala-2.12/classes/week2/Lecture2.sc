def product(f: Int => Int)(a: Int, b: Int): Int = {
  if (a > b) 1
  else f(a) * product(f)(a+1,b)
}

product(x => x * x )(2,3)
def fact(a: Int) : Int = product(x => x)(1, a)

fact(4)

def mapReduce(f: Int => Int, combine: (Int,Int) => Int, zero: Int)(a: Int, b: Int): Int = {
  if (a > b) zero
  else combine(f(a),mapReduce(f,combine, zero)(a+1, b))
}

mapReduce(x => x*x, (a,b) => a*b, 1)(2,3)

