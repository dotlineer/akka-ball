import akka.actor.{Actor, ActorRef, ActorSystem, Props}


class Player(val num: Int, val players: Array[ActorRef]) extends Actor {

  val r = scala.util.Random

  override def receive: PartialFunction[Any, Unit] = {
    case Player.Ball(c) => {
      val playerToThrowTheBallTo = (num + 1 + r.nextInt(players.length - 1)) % players.length

      println("Throwing player: #" + num)
      println("Throw number: " + c)
      println("Throwing to player #" + playerToThrowTheBallTo)
      println()

      players(playerToThrowTheBallTo) ! Player.Ball(c+1)
    }
  }

}


object Player {
  case class Ball(count: Int)
}


object BallPlaySimulation extends App {
  val ourSystem = ActorSystem("MySystem")

  val players: Array[ActorRef] = new Array[ActorRef](3)

  val player0: ActorRef = ourSystem.actorOf(Props(classOf[Player], 0, players))
  val player1: ActorRef = ourSystem.actorOf(Props(classOf[Player], 1, players))
  val player2: ActorRef = ourSystem.actorOf(Props(classOf[Player], 2, players))

  players(0) = player0
  players(1) = player1
  players(2) = player2

  player1.tell(Player.Ball(0), player0)

  Thread.sleep(1000)
  ourSystem.terminate

}