package db


case class Protein(symbol: String, sequence:String, id: Option[Int] = None)

trait ProteinComponent {
  this: GeneComponent with DriverComponent =>

  import driver.simple._

  class Proteins(tag: Tag) extends Table[Protein](tag, "PROTEINS") {

    def id = column[Int]("ID", O.PrimaryKey, O.AutoInc)

    def symbol = column[String]("SYMBOL", O.NotNull)

    def sequence = column[String]("SEQUENCE")

    def * = (symbol, sequence, id.?) <> (Protein.tupled, Protein.unapply)

//    def geneID = column[Int]("GENE_ID")
//
//    def gene = foreignKey("GENE_FK", geneID, genes)(_.id, onUpdate=ForeignKeyAction.Restrict, onDelete=ForeignKeyAction.Cascade)

  }

  val proteins = TableQuery[Proteins]

}
