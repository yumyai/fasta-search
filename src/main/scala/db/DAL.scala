package db

import java.io.File
import scala.collection.JavaConverters._
import org.biojava3.core.sequence.{DNASequence, ProteinSequence}
import org.biojava3.core.sequence.io.FastaReaderHelper

import scala.slick.driver.JdbcProfile

/**
 * Created by preecha on 10/10/14 AD.
 */

class DAL(val driver:JdbcProfile) extends GeneComponent with DriverComponent with ProteinComponent {

  import driver.simple._

  def create(implicit session: Session) = (genes.ddl ++ proteins.ddl).create

  def insert(gene: Protein)(implicit session: Session): Unit = proteins.insert(gene)

  // Query

  def batchProteinQuery(list: List[String])(implicit session: Session): List[Protein] = {
    val results = for {
      protein <- proteins if protein.symbol inSet list
    } yield protein

    results.list
  }

  def batchProteinQueryLike(list: List[String])(implicit session: Session): List[Protein] = {

    val result = list.map { s: String =>
      (for {
        protein <- proteins if protein.symbol like s"$s%"
      } yield protein).list
    }
    result.flatten
  }

  def batchGeneQuery(list: List[String])(implicit session: Session): List[Gene] = {
    val results = for {
      gene <- genes if gene.symbol inSet list
    } yield gene
    results.list
  }


  def batchGeneQueryLike(list: List[String])(implicit session: Session): List[Gene] = {

    val result = list.map { s: String =>
      (for {
        gene <- genes if gene.symbol like s"$s%"
      } yield gene).list
    }
    result.flatten
  }

  //Import
  def insertAAFASTA(fname: String)(implicit session: Session): Unit = {
    FastaReaderHelper.readFastaProteinSequence(new File(fname)).asScala foreach { entry: (String, ProteinSequence) =>
      val protein = Protein(entry._2.getOriginalHeader, entry._2.getSequenceAsString)
      proteins.insert(protein)
    }
  }

  def insertNAFASTA(fname: String)(implicit session: Session): Unit = {
    FastaReaderHelper.readFastaDNASequence(new File(fname)).asScala foreach { entry: (String, DNASequence) =>
      val gene = Gene(entry._2.getOriginalHeader, entry._2.getSequenceAsString)
      genes.insert(gene)
    }
  }
}