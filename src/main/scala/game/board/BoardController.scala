package game.board

import akka.actor.{Actor, Props}
import game.CommonCaseObject


object BoardController{
  def prop : Props = Props[BoardController]
}


class BoardController extends Actor {

  override def receive: Receive = {

    case CommonCaseObject.Roll => ()

  }




}
