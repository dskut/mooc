object Sheet {
  println("Welcome to the Scala worksheet")       //> Welcome to the Scala worksheet

  4 + 6                                           //> res0: Int(10) = 10

  def pascal(c: Int, r: Int): Int = {
    if (c == 0) 1
    else if (c == r) 1
    else pascal(c - 1, r - 1) + pascal(c, r - 1)
  }                                               //> pascal: (c: Int, r: Int)Int

  pascal(0, 2)                                    //> res1: Int = 1
  pascal(1, 2)                                    //> res2: Int = 2
  pascal(1, 3)                                    //> res3: Int = 3

  def balance(chars: List[Char]): Boolean = {

    def iter(chars: List[Char], opened: Int, lastClosed: Boolean): Boolean = {
      if (chars.isEmpty) opened == 0 && lastClosed
      else if (chars.head == '(') iter(chars.tail, opened + 1, false)
      else if (chars.head == ')') iter(chars.tail, opened - 1, true)
      else iter(chars.tail, opened, lastClosed)
    }

    iter(chars, 0, true)
  }                                               //> balance: (chars: List[Char])Boolean

  balance("(if (zero? x) max (/ 1 x))".toList)    //> res4: Boolean = true
  balance("I told him (that it’s not (yet) done). (But he wasn’t listening)".toList)
                                                  //> res5: Boolean = true
  balance("I told him (that it’s not (yet) done). (But he wasn’t listening".toList)
                                                  //> res6: Boolean = false

  balance(":-)".toList)                           //> res7: Boolean = false
  balance("())(".toList)                          //> res8: Boolean = false

  def countChange(money: Int, coins: List[Int]): Int = {
    if (coins.isEmpty) 0
    else if (money == 0) 0
    else if (money < coins.head) countChange(money, coins.tail)
    else if (money == coins.head) 1 + countChange(money, coins.tail)
    else countChange(money, coins.tail) + countChange(money - coins.head, coins)
  }                                               //> countChange: (money: Int, coins: List[Int])Int

  countChange(4, List(1, 2))                      //> res9: Int = 3
  countChange(300, List(5, 10, 20, 50, 100, 200, 500))
                                                  //> res10: Int = 1022
  countChange(301, List(5, 10, 20, 50, 100, 200, 500))
                                                  //> res11: Int = 0
  countChange(300, List(500, 5, 50, 100, 20, 200, 10))
                                                  //> res12: Int = 1022

}