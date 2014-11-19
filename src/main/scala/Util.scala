


import java.sql.DriverManager
import db.Protein
import scala.collection.JavaConverters._

object Util {
  /** A helper function to unload all JDBC drivers so we don't leak memory */
  def unloadDrivers() {
    DriverManager.getDrivers.asScala.foreach { d =>
      DriverManager.deregisterDriver(d)
    }
  }

  def gene2Fasta(gene:Protein): String = {
    val header = ">" + gene.symbol
    val sequences = gene.sequence.grouped(80)
    (Iterator(header) ++  sequences).mkString("\n")
  }
}


// So freaking slow. Screw you functional legions and their ivory tower..
//case class Entry( description: String, sequence: String )
//
//object FastaParser extends RegexParsers {
//
//  lazy val header = """>.*""".r ^^ { _.tail.trim }
//  lazy val seqLine = """[^>].*""".r ^^ { _.trim }
//
//  lazy val sequence = rep1( seqLine ) ^^ { _.mkString }
//
//  lazy val entry = header ~ sequence ^^ {
//    case h ~ s => Entry(h,s)
//  }
//
//  lazy val entries = rep1( entry )
//
//  def parse( input: String ): List[Entry] = {
//    parseAll( entries, input ) match {
//      case Success( es , _ ) => es
//      case x: NoSuccess => throw new Exception(x.toString)
//    }
//  }
//}