package api

import akka.actor.ActorRef
import akka.util.Timeout
import akka.pattern.ask
import core.GeneActor._


import scala.concurrent.{Future, Await, ExecutionContext}
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global

import db.Gene
import spray.routing.Directives

import scala.concurrent.ExecutionContext

/**
 * Created by preecha on 10/13/14 AD.
 */
class GeneService(gene: ActorRef)(implicit executionContext: ExecutionContext)
  extends Directives with DefaultJsonFormats {

  //  implicit val geneFormat = jsonFormat2(Gene)
  implicit val timeout = Timeout(5 seconds)


  val generoute = {
    path("batchquery") {
      formFields('query.as[String], 'dbtype.as[String]) { (query, dbtype ) =>
        // Preparing query
        val queries = query.split("""\n+|\t+|\s+|,""").toList

        val future = (gene ? BatchGeneQuery(queries)).mapTo[List[Gene]].map(genes2FASTA _)

        val results = Await.result(future, timeout.duration)

        complete(results)
      }
    }
  }

  val allroute = generoute

  def gene2FASTA(protein:Gene): String = {
    val header = ">" + protein.symbol
    val sequences = protein.sequence.grouped(80)
    (Iterator(header) ++  sequences).mkString("\n")
  }

  def genes2FASTA(genes:List[Gene]): String = {
    genes.map(gene2FASTA _).mkString("\n")
  }
}