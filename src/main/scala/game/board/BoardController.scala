package game.board

import akka.actor.{Actor, ActorRef, Props}
import game.util.Utils

import scala.collection.mutable
import scala.collection.immutable


object BoardController{
  def prop : Props = Props[BoardController]
  case class Move(point:Int)
}


class BoardController(organizer:ActorRef) extends Actor {
  import BoardController._

  private[this] var playerPositions : mutable.HashMap[ActorRef, Int] = mutable.HashMap.empty[ActorRef,Int]

  private[this] val latterOnBoard = latterOnBoard(5)

  private[this] val snakeOnBoard = snakeOnBoard(5)


  override def receive: Receive = {
    case Move(point:Int) => organizer ! update(point)
  }

  def update(point: Int):Any = {
    val player:ActorRef = sender()


  }

  def latterOnBoard(count:Int):immutable.Map[Int,Int] = {
    val set = Utils.random(count)

  }


  def snakeOnBoard(count:Int):immutable.Map[Int,Int] = {}

}
