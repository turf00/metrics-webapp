package computerdatabase

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.concurrent.duration._

class BasicSimulation extends Simulation {

  private val rand = scala.util.Random
  private val feeder = Iterator.continually(generateUrls)

  private def generateUrls: Map[String,String] = {
    Map("accountUrl" -> accountUrl(), "transactionUrl" -> transUrl())
  }

  private def transUrl(): String = {
    accountUrl() + "/transactions/" + rand.nextInt(Integer.MAX_VALUE)
  }

  private def accountUrl(): String = {
    "/accounts/" + rand.nextInt(Integer.MAX_VALUE)
  }

  val httpProtocol = http
    .baseUrl("http://localhost:8080")
    .acceptHeader("application/json")
    .acceptEncodingHeader("gzip, deflate")
    .userAgentHeader("Mozilla/5.0 (Macintosh; Intel Mac OS X 10.8; rv:16.0) Gecko/20100101 Firefox/16.0")

  val scn = scenario("GetAccount and Transactions")
    .feed(feeder)
    .exec(http("GetAccount")
      .get("${accountUrl}"))
    .pause(1) // Note that Gatling has recorder real time pauses
    .exec(http("GetTransactions")
      .get("${transactionUrl}"))

  setUp(scn.inject(rampUsersPerSec(100) to 500 during 150).protocols(httpProtocol))
}
