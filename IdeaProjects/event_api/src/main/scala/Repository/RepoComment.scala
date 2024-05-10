package Repository

import DBM.DBM
import Model._
import slick.jdbc.MySQLProfile.api._
import scala.concurrent.Future


object RepoComment {
  val CommentQuery:TableQuery[CommentTable] = TableQuery[CommentTable]
  def insertData(data: ModelComment): Future[Int] = {
    val insertAction = CommentQuery += data
    DBM.db.run(insertAction)
  }



  def updateData(updatedData: ModelComment): Future[Int] = {
    val updateAction = CommentQuery.filter(_.id_comment === updatedData.id_comment)
      .map(event => (event.id_comment, event.comment, event.name_student, event.time))
      .update((updatedData.id_comment, updatedData.comment, updatedData.name_student, updatedData.time))
    DBM.db.run(updateAction)
  }

  def getAll(): Future[Seq[ModelComment]] = {
    val query = CommentQuery.result
    DBM.db.run(query)
  }

  def getById(id: String): Future[Option[ModelComment]] = {
    val query = CommentQuery.filter(_.id_comment === id.toInt).result.headOption
    DBM.db.run(query)
  }

  def deleteData(id: String): Future[Int] = {
    val deleteAction = CommentQuery.filter(_.id_comment === id.toInt).delete
    DBM.db.run(deleteAction)
  }


}
