package Repository

import DBM.DBM
import Model._
import slick.jdbc.MySQLProfile.api._

import scala.concurrent.Future
import scala.util.{Failure, Success}


object RepoEvent {
  val EventQuery:TableQuery[EventTable] = TableQuery[EventTable]
  def insertData(data: ModelEvent): Future[Int] = {
    val insertAction = EventQuery += data
    DBM.db.run(insertAction)
  }



  def updateData(updatedData: ModelEvent): Future[Int] = {
    val updateAction = EventQuery.filter(_.id_event === updatedData.id_event)
      .map(event => (event.id_event, event.name_event, event.content_event, event.comments, event.golos, event.location,event.date,event.creator,event.auditorium))
      .update((updatedData.id_event, updatedData.name_event, updatedData.content_event,  updatedData.comments, updatedData.golos, updatedData.location,updatedData.date,updatedData.creator,updatedData.auditorium))

    DBM.db.run(updateAction)
  }

  def getAll(): Future[Seq[ModelEvent]] = {
    val query = EventQuery.result
    DBM.db.run(query)
  }

  def getById(id: String): Future[Option[ModelEvent]] = {
    val query = EventQuery.filter(_.id_event === id.toInt).result.headOption
    DBM.db.run(query)
  }

  def deleteData(id: String): Future[Int] = {
    val deleteAction = EventQuery.filter(_.id_event === id.toInt).delete
    DBM.db.run(deleteAction)
  }


}
