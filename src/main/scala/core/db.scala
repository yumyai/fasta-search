package core

import akka.actor.Actor
import core.DBActor._
import core.GeneActor.BatchGeneQuery
import core.ProteinActor.BatchProteinQuery
import db.{DAL, ProductionDB}
import scala.slick.jdbc.JdbcBackend.Database


import scala.slick.driver.H2Driver
import scala.slick.jdbc.JdbcBackend._

/**
 * Created by preecha on 10/11/14 AD.
 */

object DBActor {

  case class InsertAAFASTA(fasta: String)

  case class InsertNAFASTA(fasta: String)

  case class PrepareDB()

}

class DBActor extends Actor with ProductionDB with akka.actor.ActorLogging {
  // Actor with DB. Most of these are delegated to model, which in turn delegate to dal.


  def insertAAFASTA(fname: String) = {
    log.info("Inserting AA FASTA")
    model.insertAAFASTA(fname)
  }

  def insertNAFASTA(fname: String) = {
    log.info("Inserting NA FASTA")
    model.insertNAFASTA(fname)
  }

  def prepareDB() = {
    log.info("Prepareing DB")
    model.create()
  }


  def receive: Receive = {

    case message:PrepareDB => {
      prepareDB()
      sender ! true
    }

    case InsertAAFASTA(fname) => {
      insertAAFASTA(fname)
      sender ! true
    }

    case InsertNAFASTA(fname) => {
      insertNAFASTA(fname)
      sender ! true
    }

    case BatchProteinQuery(query: List[String]) => {
      sender ! model.batchProteinQuery(query)
    }

    case BatchGeneQuery(query: List[String]) => {
      sender ! model.batchGeneQuery(query)
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