package computerdatabase

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.concurrent.duration._

class BasicSimulation extends Simulation {

  val httpProtocol = http
    .baseUrl("http://localhost:8080")
    .acceptHeader("application/json")
    .acceptEncodingHeader("gzip, deflate")
    .userAgentHeader("Mozilla/5.0 (Macintosh; Intel Mac OS X 10.8; rv:16.0) Gecko/20100101 Firefox/16.0")

  val scn = scenario("GetAccount and Transactions") // A scenario is a chain of requests and pauses
    .exec(http("GetAccount")
      .get("/accounts/34567"))
    .pause(1) // Note that Gatling has recorder real time pauses
    .exec(http("GetTransactions")
      .get("/accounts/34567/transactions/12345"))

  //setUp(scn.inject(atOnceUsers(10) during 300s).protocols(httpProtocol))
  setUp(scn.inject(rampUsersPerSec(10) to 500 during 30).protocols(httpProtocol))
}
