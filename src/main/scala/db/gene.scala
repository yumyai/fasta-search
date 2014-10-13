package db


import org.biojava3.core.sequence.ProteinSequence
import java.io.File
import org.biojava3.core.sequence.GeneSequence
import org.biojava3.core.sequence.io.{FastaWriterHelper, FastaReaderHelper}


case class Gene(symbol: String, sequence:String, id: Option[Int] = None)

trait GeneComponent {
  this: DriverComponent =>

  import driver.simple._

  class Genes(tag: Tag) extends Table[Gene](tag, "GENES") {

    def id = column[Option[Int]]("ID", O.PrimaryKey, O.AutoInc)

    def symbol = column[String]("SYMBOL", O.NotNull)

    def sequence = column[String]("SEQUENCE")

    def * = (symbol, sequence, id) <> (Gene.tupled, Gene.unapply)

  }

  val genes = TableQuery[Genes]



}
