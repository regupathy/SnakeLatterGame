package game.board


import akka.actor.ActorRef
import game.util.Utils

import scala.collection.immutable.Set
import scala.collection.mutable

class BoardData {

  def conformMove(position:Int,lifter:Map[Int,Int]) :Int = if(lifter.contains(position)) lifter(position) else position

  def genSnakePoints(startPoint: Set[Int]):Set[(Int,Int)] = startPoint.map(i => i -> Utils.random(1,i))

  def genLadderPoints(startPoint: Set[Int]):Set[(Int,Int)] = startPoint.map(i => i -> Utils.random(i,100))

  def getPosition(key:ActorRef,map:mutable.HashMap[ActorRef,Int]):Int = if(map.contains(key)) map(key) else 1

  def updatePosition(key:ActorRef,position:Int,map:mutable.HashMap[ActorRef,Int]): Option[Int] = map.put(key,position)

  
}
