package core


import akka.testkit.{ImplicitSender, TestKit}
import akka.actor.ActorSystem

import org.specs2.mutable.SpecificationLike
import java.util.UUID

/**
 * Created by preecha on 10/16/14 AD.
 */
class GeneActorSpec extends TestKit(ActorSystem()) with SpecificationLike with CoreActors with Core with ImplicitSender {

  import GeneActor._

  "Gene should" >> {

    "reject invalid email" in {
      gene ! BatchSymbolQuery(List(""))
      success
    }
  }
}
