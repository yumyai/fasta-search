package db

import scala.slick.driver.{SQLiteDriver, H2Driver}
import scala.slick.jdbc.JdbcBackend.Database

/**
 * Created by preecha on 10/9/14 AD.
 */

//class Model(dal: DAL, db: Database) {
//  import dal._
//  import dal.driver.simple._
//}

class Model(name: String, dal: DAL, db: Database) {

  implicit val implicitSession = db.createSession


  // These methods are just delgate to dal.

  def create() = dal.create
  
  def batchSymbolQuery(queries: List[String]): List[Gene] = dal.batchFindSymbol(queries)

  def insertFASTA(fname: String): Unit = dal.insertFASTA(fname)

  //TODO: Close connection
  //def closeSession()

}

trait DBConfig {
  val model: Model
}


trait ProductionDB extends DBConfig {
  val model = new Model("H2", new DAL(H2Driver), Database.forURL("jdbc:h2:mem:test1", driver = "org.h2.Driver"))
}
