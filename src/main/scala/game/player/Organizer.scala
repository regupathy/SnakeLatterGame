package game.player

import akka.actor.{Actor, ActorRef, PoisonPill, Props}
import akka.routing.{ActorRefRoutee, Broadcast, RoundRobinRoutingLogic, Router}
import game.board.BoardController


object Organizer{
  def props() = Props[Organizer]
  case class Win(who:ActorRef)
  case object ContinueGame
}

class Organizer extends Actor {
import Organizer._
  var boardController:ActorRef =_

  override def preStart(): Unit = {
    boardController = context.actorOf(BoardController.prop,"")
    context.watch(boardController)
  }

  override def postStop(): Unit = {
    playerRouter.route(Player.RollDice,self)
  }

  override def receive: Receive = {
    case Win(who) =>
      playerRouter.route(Broadcast(PoisonPill),self)
      sender() ! PoisonPill
      self ! PoisonPill
    case ContinueGame =>
      playerRouter.route(Player.RollDice,sender())
  }

  private[this] final val playerRouter: Router = createMasterRouter(3);

  def createMasterRouter(playerCount:Int): Router =  {
    val routees = Vector.fill(playerCount) {
      val r = context.actorOf(Player.props())
      context.watch(r)
      ActorRefRoutee(r)
    }
    Router(RoundRobinRoutingLogic(), routees)
  }

}
