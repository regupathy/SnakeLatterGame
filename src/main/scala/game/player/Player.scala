package game.player

import akka.actor.{Actor, ActorRef, Props}
import game.board.BoardController
import game.util.Utils

object Player{
  def props(ref:ActorRef) :Props = Props(new Player(ref))
  case object RollDice
}

class Player(reporter:ActorRef) extends Actor {
  import Player._

  override def receive: Receive = {
    case RollDice => reporter ! BoardController.Move(Utils.random(1,6))
  }
}
