package Routing

import akka.http.scaladsl.server.Directives._
import de.heikoseeberger.akkahttpjson4s.Json4sSupport
import Model._
import Model.JsonFormatss
import org.json4s.{DefaultFormats, jackson}
import Repository._

object RouteComment extends Json4sSupport {
  implicit val serialization = jackson.Serialization
  implicit val formats = JsonFormatss.formats

  val route =
    pathPrefix("Comment") {
      concat(
        pathEnd {
          concat(
            get {
              complete(RepoComment.getAll())
            },
            post {
              entity(as[ModelComment]) { comment =>
                complete(RepoComment.insertData(comment))
              }
            }
          )
        },
        path(Segment) { id_comment =>
          concat(
            get {
              complete(RepoComment.getById(id_comment))
            },
            put {
              entity(as[ModelComment]) { updatedEvent =>
                complete(RepoComment.updateData(updatedEvent))
              }
            },
            delete {
              complete(RepoComment.deleteData(id_comment))
            }
          )
        }


      )
    }
}