package Routing

import akka.http.javadsl.server.PathMatchers.segment
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.util.Timeout
import amqp.RabbitMQ
import de.heikoseeberger.akkahttpjson4s.Json4sSupport
import org.json4s.{DefaultFormats, Formats, NoTypeHints, jackson}
import repository.ExamRepository
import model.Exam
import org.json4s.native.Serialization
import org.json4s.native.Serialization.write
import Main.Main._
import scala.concurrent.duration.DurationInt
import scala.util.Success
import akka.pattern.ask

class ExamRoutes(implicit val examRepository: ExamRepository) extends Json4sSupport {
  implicit val serialization = jackson.Serialization
  implicit val formats = DefaultFormats

  val route =
    pathPrefix("exam") {
      concat(
        pathEnd {
          concat(
            get {
              complete(examRepository.getAllExams())
            },
            post {
              entity(as[Exam]) { exam =>
                onComplete(examRepository.addExam(exam)){
                  case Success(eventId) => {
                    implicit val timeout: Timeout = Timeout(5.seconds)
                    implicit val formats: Formats = Serialization.formats(NoTypeHints)
                    amqpActor ? RabbitMQ.Tell("univer.news_api.create_examPOST", write(exam))
                    complete(StatusCodes.Created, s"ID of the new news: ${eventId.toString}")
                  }
                }
              }
            }
          )
        },
        path(Segment) { examId =>
          concat(
            get {
              complete(examRepository.getExamById(examId))
            },
            delete {
              complete(examRepository.deleteExam(examId))
            },
            put {
              entity(as[Exam]) { updatedExam =>
                complete(examRepository.updateExam(examId, updatedExam))
              }
            }
          )
        }
      )
    }
}