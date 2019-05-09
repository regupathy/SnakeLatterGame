package game.util

import scala.collection.immutable.Set

object Utils {

  def random(count:Int): Set[Int] = {
    var container = Set.empty[Int]
    do {
      val temp = util.Random.nextInt(99)
      if (temp > 10)  container += temp
    } while(container.size <= count-1)
    container
  }

  def removeDuplicate(set1:Set[Int],set2:Set[Int]): Set[Int] = set1.diff(set2)

  def random(start:Int,end:Int):Int = start + util.Random.nextInt(end-start)

}
