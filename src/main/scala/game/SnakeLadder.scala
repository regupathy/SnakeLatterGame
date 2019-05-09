package game

import akka.actor.ActorSystem
import game.player.Organizer

object SnakeLadder extends App {
  val system: ActorSystem = ActorSystem("snakeLadder")
  system.actorOf(Organizer.props(),"Organizer")
}
