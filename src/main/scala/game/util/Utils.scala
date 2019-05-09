package game.util

import scala.collection.immutable.Set

object Utils {

  def random(count:Int): Set[Int] = {
    var container = Set.empty[Int]
    do container += util.Random.nextInt(99) while(container.size <= count-1)
  }

  def removeDuplicate(set1:Set[Int],set2:Set[Int]): Set[Int] = set1.intersect(set2)

  def random(start:Int,end:Int):Int = start + util.Random.nextInt(end-start)

}
