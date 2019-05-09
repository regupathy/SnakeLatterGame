package game.board

import akka.actor.{Actor, ActorRef, Props}
import game.player.Organizer
import game.util.Utils

import scala.collection.immutable.Set
import scala.collection.{immutable, mutable}

//=================================================================
//              Companion Object
//=================================================================

object BoardController{
  def props(ref:ActorRef) : Props = Props(new BoardController(ref))
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

  private[this] val playerPositions : mutable.HashMap[ActorRef, Int] = mutable.HashMap.empty[ActorRef,Int]

  private[this] var latterOnBoard = immutable.Map.empty[Int,Int]

  private[this] var snakeOnBoard = immutable.Map.empty[Int,Int]

  //=================================================================
  //              Life cycle method
  //=================================================================

  override def preStart(): Unit = {
    latterOnBoard = latterOnBoard(5)
    println("=====================================================")
    println("            Latter on Board")
    println()
    println(latterOnBoard.toString())
    snakeOnBoard = snakeOnBoard(5)
    println("=====================================================")
    println("            Snake on Board")
    println()
    println(snakeOnBoard.toString())
    println("=====================================================")
    println()
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
    var playerGot: String = ""
    if (nextPosition <= 100) {
      val nextPosition2 = conformMove(nextPosition, snakeOnBoard)
        if(nextPosition != nextPosition2) playerGot=" caught by Snake"
      val nextPosition3 = conformMove(nextPosition2, latterOnBoard)
        if(nextPosition2 != nextPosition3) playerGot=" helped by Latter"
      updatePosition(player, nextPosition3)
      log(player.path.name,currentPosition,point,nextPosition3,playerGot)
      if (nextPosition3 == 100) Organizer.Win(player) else Organizer.ContinueGame
    }else
      Organizer.ContinueGame
  }

  def latterOnBoard(count:Int):immutable.Map[Int,Int] = {
    val set:Set[Int] = Utils.random(count)
    snakeOnBoard.keySet.foreach(a => if(set.contains(a)) set - a)
    genLadderPoints(set)
  }

  def snakeOnBoard(count:Int):immutable.Map[Int,Int] = {
    val set:Set[Int] = Utils.random(count)
    latterOnBoard.keySet.foreach(a => if(set.contains(a)) set-a)
    genSnakePoints(set)
  }

  //=================================================================
  //              Helper Functions
  //=================================================================

  def conformMove(position:Int,lifter:Map[Int,Int]) :Int = if(lifter.contains(position)) lifter(position) else position

  def genSnakePoints(startPoint: Set[Int]):Map[Int,Int] = startPoint.map(i => i -> Utils.random(1,i)).toMap

  def genLadderPoints(startPoint: Set[Int]):Map[Int,Int] = startPoint.map(i => i -> Utils.random(i,100)).toMap

  def getPosition(key:ActorRef):Int = if(playerPositions.contains(key)) playerPositions(key) else 1

  def updatePosition(player:ActorRef,position:Int): Option[Int] = {
    playerPositions.put(player,position)
  }

  def log(name:String,cp:Int,point:Int,newpoint:Int,got:String): Unit ={
    println("Player "+ name + " in " + cp +" got Dice point : " + point +" and moved to "+newpoint +" "+got)
  }
}
