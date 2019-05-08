package game.board


import scala.collection.immutable.Set

class BoardData {


  def conformMove(position:Int,lifter:Map[Int,Int]) :Int = if(lifter.contains(position)) lifter(position) else position


  def random(start:Int,end:Int):Int = start + util.Random.nextInt(end-start)


  def genSnakePoints(startPoint: Set[Int]):Set[(Int,Int)] = startPoint.map(i => i -> random(1,i))


  def genLadderPoints(startPoint: Set[Int]):Set[(Int,Int)] = startPoint.map(i => i -> random(i,100))


  def random(count:Int): Set[Int] = {
    var container = Set.empty[Int]
    do container += util.Random.nextInt(100) while(container.size <= count-1)
  }

  def removeDuplicate(set1:Set[Int],set2:Set[Int]): Set[Int] = set1.intersect(set2)

}
