package db

/**
 * Created by preecha on 10/22/14 AD.
 */
case class Gene(symbol: String, sequence:String, id: Option[Int] = None)

trait GeneComponent {
  this: DriverComponent =>

  import driver.simple._

  class Genes(tag: Tag) extends Table[Gene](tag, "GENES") {

    def id = column[Int]("ID", O.PrimaryKey, O.AutoInc)

    def symbol = column[String]("SYMBOL", O.NotNull)

    def sequence = column[String]("SEQUENCE")

    def * = (symbol, sequence, id.?) <> (Gene.tupled, Gene.unapply)


  }

  val genes = TableQuery[Genes]

}