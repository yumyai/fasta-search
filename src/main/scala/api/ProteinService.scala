package api

import akka.actor.ActorRef
import akka.util.Timeout
import akka.pattern.ask


import scala.concurrent.{Future, Await, ExecutionContext}
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global

import core.ProteinActor.BatchSymbolQuery
import db.Protein
import spray.routing.Directives

import scala.concurrent.ExecutionContext

/**
 * Created by preecha on 10/13/14 AD.
 */
class ProteinService(gene: ActorRef)(implicit executionContext: ExecutionContext)
  extends Directives with DefaultJsonFormats {

//  implicit val geneFormat = jsonFormat2(Gene)
  implicit val timeout = Timeout(5 seconds)


  val proteinroute = {
    path("batchquery") {
      formFields('query.as[String]) { (query) =>
        // Preparing query
        val queries = query.split("""\n+|\t+|\s+|,""").toList

        val future = (gene ? BatchSymbolQuery(queries)).mapTo[List[Protein]].map(proteins2FASTA _)

        val results = Await.result(future, timeout.duration)

        complete(results)


//        complete(s"The color is '$queries'")
      }
    }
  }

  val allroute = proteinroute

  def protein2FASTA(protein:Protein): String = {
    val header = ">" + protein.symbol
    val sequences = protein.sequence.grouped(80)
    (Iterator(header) ++  sequences).mkString("\n")
  }

  def proteins2FASTA(genes:List[Protein]): String = {
    genes.map(protein2FASTA _).mkString("\n")
  }
}