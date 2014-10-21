package core

import akka.actor.Actor
import core.DBActor._
import core.ProteinActor.BatchSymbolQuery
import db.{DAL, ProductionDB}
import scala.slick.jdbc.JdbcBackend.Database


import scala.slick.driver.H2Driver
import scala.slick.jdbc.JdbcBackend._

/**
 * Created by preecha on 10/11/14 AD.
 */

object DBActor {

  case class InsertFASTA(fasta: String)

  case class PrepareDB()

}

class DBActor extends Actor with ProductionDB {
  // Actor with DB. Most of these are delegated to model, which in turn delegate to dal.


  def insertFASTA(fname: String) = {
    println("Inserting FASTA")
    model.insertFASTA(fname)
  }

  def prepareDB() = {
    println("Prepareing DB")
    model.create()
  }


  def receive: Receive = {

    case message:PrepareDB => {
      prepareDB()
      sender ! true
    }

    case InsertFASTA(fname) => {
      insertFASTA(fname)
      sender ! true
    }

    case BatchSymbolQuery(query: List[String]) => {
      sender ! model.batchSymbolQuery(query)
    }

    case _ => {
      println("Illegal message at DbActor")
    }
  }
}

trait DBDAO { this: Actor =>

  val dal: DAL
  val db: Database

}

trait TestDBDAO {

  val dal = new DAL(H2Driver)
  val db = Database.forURL("jdbc:h2:mem:test1", driver = "org.h2.Driver")

  implicit val implicitSession = db.createSession
}

trait ProductionDAO {

}