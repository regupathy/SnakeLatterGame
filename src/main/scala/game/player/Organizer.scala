package game.player

import akka.actor.{Actor, ActorRef, Props}
import akka.routing.{ActorRefRoutee, RoundRobinRoutingLogic, Router}
import game.board.BoardController


object Organizer{
  case class Win(who:ActorRef)
  case object ContinueGame
}

class Organizer extends Actor {

  private var boardController:ActorRef = _

  override def preStart(): Unit = {
      boardController = context.actorOf(BoardController.prop,"")
  }

  override def receive: Receive = {
  }

  private[this] final val playerRouter: Router = createMasterRouter(3);

  def createMasterRouter(playerCount:Int): Router =  {
    val routees = Vector.fill(playerCount) {
      val r = context.actorOf(Player.props(boardController))
      context.watch(r)
      ActorRefRoutee(r)
    }
    Router(RoundRobinRoutingLogic(), routees)
  }

}
