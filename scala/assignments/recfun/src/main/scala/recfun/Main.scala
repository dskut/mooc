package recfun
import common._

object Main {
  def main(args: Array[String]) {
    println("Pascal's Triangle")
    for (row <- 0 to 10) {
      for (col <- 0 to row)
        print(pascal(col, row) + " ")
      println()
    }
  }

  /**
   * Exercise 1
   */
  def pascal(c: Int, r: Int): Int = {
    if (c == 0) 1
    else if (c == r) 1
    else pascal(c - 1, r - 1) + pascal(c, r - 1)
  }

  /**
   * Exercise 2
   */
  def balance(chars: List[Char]): Boolean = {

    def iter(chars: List[Char], opened: Int, lastClosed: Boolean): Boolean = {
      if (chars.isEmpty) opened == 0 && lastClosed
      else if (chars.head == '(') iter(chars.tail, opened + 1, false)
      else if (chars.head == ')') iter(chars.tail, opened - 1, true)
      else iter(chars.tail, opened, lastClosed)
    }

    iter(chars, 0, true)
  }

  /**
   * Exercise 3
   */
  def countChange(money: Int, coins: List[Int]): Int = {
    if (coins.isEmpty) 0
    else if (money == 0) 0
    else if (money < coins.head) countChange(money, coins.tail)
    else if (money == coins.head) 1 + countChange(money, coins.tail)
    else countChange(money, coins.tail) + countChange(money - coins.head, coins)
  }
}
