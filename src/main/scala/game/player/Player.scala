package game.player

import akka.actor.{Actor, Props}
import game.board.BoardController
import game.util.Utils

object Player{
  def props() :Props = Props[Player]
  case object RollDice
}

class Player() extends Actor{
  import Player._

  override def receive: Receive = {
    case RollDice => sender() ! BoardController.Move(Utils.random(1,6))
  }

}
