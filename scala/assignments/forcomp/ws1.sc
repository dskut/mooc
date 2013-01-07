
object ws1 {
  "Desnis".toLowerCase().groupBy(c => c).toList.map(x => (x._1, x._2.length))
                                                  //> res0: List[(Char, Int)] = List((e,1), (s,2), (n,1), (i,1), (d,1))
}