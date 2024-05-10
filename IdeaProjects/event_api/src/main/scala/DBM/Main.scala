package DBM

import akka.actor.{ActorSystem, Props}
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import de.heikoseeberger.akkahttpjson4s.Json4sSupport
import org.json4s.{DefaultFormats, jackson}
import slick.jdbc.MySQLProfile.api._
import Routing._
import akka.http.scaladsl.server.Directives._
import amqp._
import scala.concurrent.duration._
import scala.concurrent.{Await, ExecutionContextExecutor, Future}


object DBM {
  val db: Database = Database.forConfig("mysqlDB")
}

object Main extends Json4sSupport {
  implicit val system: ActorSystem = ActorSystem("web-service")
  implicit val materializer: ActorMaterializer = ActorMaterializer()
  implicit val executionContext: ExecutionContextExecutor = system.dispatcher
  implicit val serialization = jackson.Serialization
  implicit val formats = DefaultFormats

  val amqpActor = system.actorOf(Props(new AmqpActor("X:routing.topic", "event_api")), "amqpActor")

  def main(args: Array[String]): Unit = {

    val allRoutes = RouteEvent.route ~
      RouteComment.route ~ RouteVoting.route

    val bindingFuture = Http().bindAndHandle(allRoutes, "localhost", 8081)
    println(s"Server online at http://localhost:8081/\nPress RETURN to stop...")
    //    val insertionResult: Future[Int] = RepoGroup.insertData(new ModelGroup(id_Group = 1, name_Group = "VISSI", facultet_Group = 1, adviser_Group = 1, Language.Kazakh, TypeOfTraning.Day, 3, 1, 1, TraningStage.Bachelor_course))
    //    val i=new ModelEvent(1,"Event","Content",true,1,1,"Local",Timestamp.valueOf("2023-01-01 12:00:00"),"Asyl",Auditorium.all)
    //    val insertionResult: Future[Int] = RepoEvent.insertData(i)
    //    insertionResult.onComplete {
    //      case Success(result) =>
    //        println(s"Insertion successful. Rows affected: $result")
    //      case Failure(ex) =>
    //        println(s"Insertion failed with error: $ex")
    //    }
    //    RepoGroup.deleteData("2")
    //    RepoEvent.getAll()
    scala.io.StdIn.readLine()
    // Add a delay or read a line to keep the server running
    //    scala.io.StdIn.readLine()

    // Gracefully shut down the server
    bindingFuture
      .flatMap { binding =>
        println(s"Unbinding from ${binding.localAddress}")
        binding.unbind()
      }
      .onComplete { _ =>
        println("Terminating server and actor system...")
        system.terminate()
      }



    // Optionally, you can add a delay here to allow time for cleanup before terminating the actor system
    Await.result(system.whenTerminated, 10.seconds)
  }
}
