package game

import akka.actor.ActorRef

object CommonCaseObject {

  case object Roll

  case object Stop

  case class Move(position:Int)

  case class CurrentPlayer(player :ActorRef)
}
