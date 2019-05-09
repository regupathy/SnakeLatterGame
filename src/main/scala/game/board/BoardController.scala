package game.board

import akka.actor.{Actor, ActorRef, Props}
import game.player.Organizer
import game.util.Utils

import scala.collection.mutable
import scala.collection.immutable
import scala.collection.immutable.Set

//=================================================================
//              Companion Object
//=================================================================

object BoardController{
  def prop : Props = Props[BoardController]
  case class Move(point:Int)
}

//=================================================================
//              class BoardController
//=================================================================

class BoardController(organizer:ActorRef) extends Actor {
  import BoardController._

//=================================================================
//              Actor State
//=================================================================

  private[this] var playerPositions : mutable.HashMap[ActorRef, Int] = mutable.HashMap.empty[ActorRef,Int]

  private[this] var latterOnBoard = immutable.Map.empty[Int,Int]

  private[this] var snakeOnBoard = immutable.Map.empty[Int,Int]

  //=================================================================
  //              Life cycle method
  //=================================================================

  override def preStart(): Unit = {
    latterOnBoard = latterOnBoard(5)
    snakeOnBoard = snakeOnBoard(5)
  }

  //=================================================================
  //              Receiver method
  //=================================================================

  override def receive: Receive = {
    case Move(point:Int) => organizer ! update(point)
  }

  //=================================================================
  //              Internal Functions
  //=================================================================

  def update(point: Int):Any = {
    val player:ActorRef = sender()
    val currentPosition:Int = getPosition(player)
    var nextPosition = currentPosition+point
    if (nextPosition <= 100) {
      nextPosition = conformMove(nextPosition, snakeOnBoard)
      nextPosition = conformMove(nextPosition, latterOnBoard)
      updatePosition(player, nextPosition)
      if (nextPosition == 100) Organizer.Win(player) else Organizer.ContinueGame
    }else
      Organizer.ContinueGame
  }

  def latterOnBoard(count:Int):immutable.Map[Int,Int] = {
    val set:Set[Int] = Utils.random(count)
    val uniqueSet = Utils.removeDuplicate(set,snakeOnBoard.keySet)
    genLadderPoints(uniqueSet)
  }

  def snakeOnBoard(count:Int):immutable.Map[Int,Int] = {
    val set:Set[Int] = Utils.random(count)
    val uniqueSet = Utils.removeDuplicate(set,latterOnBoard.keySet)
    genSnakePoints(uniqueSet)
  }

  //=================================================================
  //              Helper Functions
  //=================================================================

  def conformMove(position:Int,lifter:Map[Int,Int]) :Int = if(lifter.contains(position)) lifter(position) else position

  def genSnakePoints(startPoint: Set[Int]):Set[(Int,Int)] = startPoint.map(i => i -> Utils.random(1,i))

  def genLadderPoints(startPoint: Set[Int]):Set[(Int,Int)] = startPoint.map(i => i -> Utils.random(i,100))

  def getPosition(key:ActorRef):Int = if(playerPositions.contains(key)) playerPositions(key) else 1

  def updatePosition(key:ActorRef,position:Int): Option[Int] = playerPositions.put(key,position)
}
