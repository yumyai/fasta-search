package core

import scala.concurrent.{Await, ExecutionContext}
import scala.concurrent.duration._
import ExecutionContext.Implicits.global

import akka.actor.{Props, ActorRefFactory, ActorSystem}
import akka.io.IO
import akka.pattern.ask
import akka.util.Timeout
import spray.can.Http
import core.DBActor._
import api.{Api, RoutedHttpService}
import web.StaticResources

/**
 * Core is type containing the ``system: ActorSystem`` member. This enables us to use it in our
 * apps as well as in our tests.
 */
trait Core {

  protected implicit def system: ActorSystem

}

/**
 * This trait implements ``Core`` by starting the required ``ActorSystem`` and registering the
 * termination handler to stop the system when the JVM exits.
 */
trait BootedCore extends Core with Api with StaticResources { this: scala.App =>
  def system: ActorSystem = ActorSystem("fastaquery")
  def actorRefFactory: ActorRefFactory = system
  val rootService = system.actorOf(Props(new RoutedHttpService(routes ~ staticResources )))

  implicit val timeout = Timeout(5 seconds)

  lazy val aafasta = args(0)
  lazy val nafasta = args(1)

  // Prepare database
  // TODO: This is perfect chance to practice monad foo. Change it to monadic later.

  val prepare = for {
    a <- (db ? PrepareDB()).mapTo[Boolean]
    b <- (db ? InsertAAFASTA(aafasta)).mapTo[Boolean]
    c <- (db ? InsertNAFASTA(nafasta)).mapTo[Boolean]}

  yield a && b && c

  IO(Http)(system) ! Http.Bind(rootService, "0.0.0.0", port = 8081)

  /**
   * Construct the ActorSystem we will use in our application
   */
  //protected implicit  val system : ActorSystem

  /**
   * Ensure that the constructed ActorSystem is shut down when the JVM shuts down
   */
  sys.addShutdownHook(system.shutdown())

}

/**
 * This trait contains the actors that make up our application; it can be mixed in with
 * ``BootedCore`` for running code or ``TestKit`` for unit and integration tests.
 */
trait CoreActors {
  this: Core =>

//  val registration = system.actorOf(Props[RegistrationActor])
//  val messenger    = system.actorOf(Props[MessengerActor])
  val db = system.actorOf(Props[DBActor])
  val protein = system.actorOf(Props(new ProteinActor(db)))
  val gene = system.actorOf(Props(new GeneActor(db)))
}