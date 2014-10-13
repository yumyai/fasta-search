package core

import akka.util.Timeout

import scala.concurrent.{Future, Await, ExecutionContext}
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global

import akka.actor.{ActorRef, Actor}
import akka.pattern.{ask, pipe}

import core.GeneActor._
import db.{Gene, ProductionDB}

object GeneActor {

  case class Search(query: String)
  case class BatchSymbolQuery(query: List[String])

}


class GeneActor(db: ActorRef) extends Actor with ProductionDB {

  implicit val timeout = Timeout(5 seconds)

  def receive: Receive = {
    case b@BatchSymbolQuery(query) => {
      val future = for {
        x <- (db ? b).mapTo[List[Gene]]
      } yield x
      future pipeTo sender
    }

    case Search(query) => {
      // TODO: Implement this
      "Nothing"
    }
  }
}