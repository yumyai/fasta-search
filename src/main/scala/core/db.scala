package core

import akka.actor.Actor
import core.DBActor._
import core.GeneActor.BatchSymbolQuery
import db.{Gene, ProductionDB}

import scala.slick.driver.H2Driver

/**
 * Created by preecha on 10/11/14 AD.
 */

object DBActor {

  case class InsertFASTA(fasta: String)

  case class PrepareDB()

  // Alot of answer.


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
