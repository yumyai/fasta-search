package api

import akka.actor.ActorRef
import akka.util.Timeout
import akka.pattern.ask
import spray.routing.directives.RouteDirectives._


import scala.concurrent.{Future, Await, ExecutionContext}
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global

import core.ProteinActor.BatchProteinQuery
import db.Protein
import spray.routing.{MalformedFormFieldRejection, Directives}

import scala.concurrent.ExecutionContext

/**
 * Created by preecha on 10/13/14 AD.
 */
class ProteinService(protein: ActorRef)(implicit executionContext: ExecutionContext)
  extends Directives with DefaultJsonFormats {

//  implicit val geneFormat = jsonFormat2(Gene)
  implicit val timeout = Timeout(5 seconds)


  val proteinroute = {
    path("batchquery") {

      formFields('query.as[String], 'dbtype.as[String]) {(query, dbtype) =>
        // Preparing query
        val queries = query.split("""\n+|\t+|\s+|,""").toList

        if(dbtype != "protein") {
          reject(MalformedFormFieldRejection("dbtype", "Not protein"))
        } else {
          val future = (protein ? BatchProteinQuery(queries)).mapTo[List[Protein]].map(proteins2FASTA _)
          val results = Await.result(future, timeout.duration)
          complete(results)
        }
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