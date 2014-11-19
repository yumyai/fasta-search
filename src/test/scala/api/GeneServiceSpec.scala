package api

/**
 * Created by preecha on 10/16/14 AD.
 */
import spray.testkit.Specs2RouteTest
import spray.routing.Directives
import org.specs2.mutable.Specification
import spray.http.HttpResponse

class GeneServiceSpec extends Specification with Directives with Specs2RouteTest {
  "The routing infrastructure should support" >> {
    "the most simple and direct route" in {
      Post() ~> complete(HttpResponse()) ~> (_.response) === HttpResponse()
    }

    "Another" in {
      Post() ~> complete(HttpResponse()) ~> (_.response) === HttpResponse()
    }
  }

}
