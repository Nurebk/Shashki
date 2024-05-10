package Routing

import DBM.Main.amqpActor
import akka.http.scaladsl.server.Directives._
import de.heikoseeberger.akkahttpjson4s.Json4sSupport
import Model._
import Model.JsonFormatss
import org.json4s.{DefaultFormats, Formats, NoTypeHints, jackson}
import Repository._
import akka.http.scaladsl.model.StatusCodes
import akka.pattern.ask
import akka.util.Timeout
import amqp.RabbitMQ
import org.json4s._
import org.json4s.native.Serialization
import org.json4s.native.Serialization.write


import scala.concurrent.duration._
import scala.util.Success

object RouteEvent extends Json4sSupport {
  implicit val serialization = jackson.Serialization
  implicit val formats = JsonFormatss.formats

  val route =
    pathPrefix("Event") {
      concat(
        pathEnd {
          concat(
            get {
              complete(RepoEvent.getAll())
            },
            post {
              entity(as[ModelEvent]) { event =>
                onComplete(RepoEvent.insertData(event)) {
                  case Success(eventId) =>
                  {
                    implicit val timeout: Timeout = Timeout(5.seconds)
                    implicit val formats: Formats = Serialization.formats(NoTypeHints)
                    val result = (amqpActor ? RabbitMQ.Tell("univer.News_api.request", write(event)))
                    complete(StatusCodes.Created, s"ID of the new news: ${eventId.toString}")
                  }
                }
              }
            }
          )
        },
        path(Segment) { id_event =>
          concat(
            get {
              complete(RepoEvent.getById(id_event))
            },
            put {
              entity(as[ModelEvent]) { updatedEvent =>
                complete(RepoEvent.updateData(updatedEvent))
              }
            },
            delete {
              complete(RepoEvent.deleteData(id_event))
            }
          )
        }


      )
    }
}