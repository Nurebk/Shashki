package Routing

import akka.http.scaladsl.server.Directives._
import de.heikoseeberger.akkahttpjson4s.Json4sSupport
import Model._
import Model.JsonFormatss
import org.json4s.{DefaultFormats, jackson}
import Repository._

object RouteVoting extends Json4sSupport {
  implicit val serialization = jackson.Serialization
  implicit val formats = JsonFormatss.formats

  val route =
    pathPrefix("Voting") {
      concat(
        pathEnd {
          concat(
            get {
              complete(RepoVoting.getAll())
            },
            post {
              entity(as[ModelVoting]) { voting =>
                complete(RepoVoting.insertData(voting))
              }
            }
          )
        },
        path(Segment) { id_voting =>
          concat(
            get {
              complete(RepoVoting.getById(id_voting))
            },
            put {
              entity(as[ModelVoting]) { updatedEvent =>
                complete(RepoVoting.updateData(updatedEvent))
              }
            },
            delete {
              complete(RepoVoting.deleteData(id_voting))
            }
          )
        }


      )
    }
}