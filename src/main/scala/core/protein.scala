package core

import akka.util.Timeout
import scala.slick.jdbc.JdbcBackend.Database
import scala.concurrent.{Future, Await, ExecutionContext}
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global

import akka.actor.{ActorRef, Actor}
import akka.pattern.{ask, pipe}

import core.ProteinActor._
import db.{ProductionDB, DAL, Protein}

object ProteinActor {
  case class ProteinSearch(query: String)
  case class BatchProteinQuery(query: List[String])
}


class ProteinActor(db: ActorRef) extends Actor with ProductionDB {

  implicit val timeout = Timeout(5 seconds)

  def receive: Receive = {
    case b@BatchProteinQuery(query) => {
      val future = for {
        x <- (db ? b).mapTo[List[Protein]]
      } yield x
      future pipeTo sender
    }

    // TODO: Implement this
//    case Search(query) => {
//      "Nothing"
//    }
  }
}