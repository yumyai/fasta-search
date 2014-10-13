package db

import java.io.File
import scala.collection.JavaConverters._
import org.biojava3.core.sequence.ProteinSequence
import org.biojava3.core.sequence.io.FastaReaderHelper

import scala.slick.driver.JdbcProfile

/**
 * Created by preecha on 10/10/14 AD.
 */

class DAL(val driver:JdbcProfile) extends GeneComponent with GenomeComponent with DriverComponent {

  import driver.simple._

  def create(implicit session: Session) = (genes.ddl ++ genomes.ddl).create


  def insert(gene: Gene)(implicit session: Session) = genes.insert(gene)


  // Genes
  def insertFASTA(fname: String)(implicit session: Session): Unit = {
    FastaReaderHelper.readFastaProteinSequence(new File(fname)).asScala foreach { entry: (String, ProteinSequence) =>
      val gene = Gene(entry._2.getOriginalHeader, entry._2.getSequenceAsString)
      genes.insert(gene)
    }
  }

  def batchFindSymbol(list: List[String])(implicit session: Session): List[Gene] = {
    val results = for {
      gene <- genes if gene.symbol inSet list
    } yield gene

    results.list
  }

  // End gene
}