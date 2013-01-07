
object test {
  val v = Vector(Vector(1, 2, 3, 4))              //> v  : scala.collection.immutable.Vector[scala.collection.immutable.Vector[Int]
                                                  //| ] = Vector(Vector(1, 2, 3, 4))
  v.indexWhere(v => v.contains(2))                //> res0: Int = 0
  
    val level =
    """ooo-------
      |oSoooo----
      |ooooooooo-
      |-ooooooooo
      |-----ooToo
      |------ooo-""".stripMargin                  //> level  : String = ooo-------
                                                  //| oSoooo----
                                                  //| ooooooooo-
                                                  //| -ooooooooo
                                                  //| -----ooToo
                                                  //| ------ooo-
      
   level                                          //> res1: String = ooo-------
                                                  //| oSoooo----
                                                  //| ooooooooo-
                                                  //| -ooooooooo
                                                  //| -----ooToo
                                                  //| ------ooo-

}