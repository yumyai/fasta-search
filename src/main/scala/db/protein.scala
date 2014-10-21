package db


case class Protein(symbol: String, sequence:String, id: Option[Int] = None)

trait ProteinComponent {
  this: DriverComponent =>

  import driver.simple._

  class Proteins(tag: Tag) extends Table[Protein](tag, "GENES") {

    def id = column[Option[Int]]("ID", O.PrimaryKey, O.AutoInc)

    def symbol = column[String]("SYMBOL", O.NotNull)

    def sequence = column[String]("SEQUENCE")

    def * = (symbol, sequence, id) <> (Protein.tupled, Protein.unapply)

  }

  val proteins = TableQuery[Proteins]



}
