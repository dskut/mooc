import scala.util.Sorting
import scala.math.Ordering

object temp {
  def pack[T](list :List[T]): List[List[T]] = list match {
    case Nil => Nil
    case x :: xs =>
      val (first, rest) = list.span(y => x == y)
      first :: pack(rest)
  }                                               //> pack: [T](list: List[T])List[List[T]]
  
  val list = List('a', 'b', 'a')                  //> list  : List[Char] = List(a, b, a)
  val sorted = list.sort((x, y) => x < y)         //> sorted  : List[Char] = List(a, a, b)
  val packed = pack(sorted)                       //> packed  : List[List[Char]] = List(List(a, a), List(b))
  val encoded = packed.map(xs => (xs.head, xs.length))
                                                  //> encoded  : List[(Char, Int)] = List((a,2), (b,1))
  val list2 = List(('t', 2), ('e', 1), ('x', 3))  //> list2  : List[(Char, Int)] = List((t,2), (e,1), (x,3))
  list2.sortWith(_._2 < _._2)                     //> res0: List[(Char, Int)] = List((e,1), (t,2), (x,3))
  list2.map(pair => List(pair))                   //> res1: List[List[(Char, Int)]] = List(List((t,2)), List((e,1)), List((x,3)))
  
  val list3 = List(1, 2, 7, 8, 9)                 //> list3  : List[Int] = List(1, 2, 7, 8, 9)
  val (left, right) = list3.span(x => x <= 2)     //> left  : List[Int] = List(1, 2)
                                                  //| right  : List[Int] = List(7, 8, 9)
  
  left ::: 5 :: right                             //> res2: List[Int] = List(1, 2, 5, 7, 8, 9)
                                               
}