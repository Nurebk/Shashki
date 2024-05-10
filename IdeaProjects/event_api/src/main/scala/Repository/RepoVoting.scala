package Repository

import DBM.DBM
import Model._
import slick.jdbc.MySQLProfile.api._
import scala.concurrent.Future

object RepoVoting {
  val VotingQuery:TableQuery[VotingTable] = TableQuery[VotingTable]
  def insertData(data: ModelVoting): Future[Int] = {
    val insertAction = VotingQuery += data
    DBM.db.run(insertAction)
  }

  def updateData(updatedData: ModelVoting): Future[Int] = {
    val updateAction = VotingQuery.filter(_.id_voting === updatedData.id_voting)
      .map(voting => (voting.id_voting, voting.golos))
      .update((updatedData.id_voting, updatedData.golos))
    DBM.db.run(updateAction)
  }

  def getAll(): Future[Seq[ModelVoting]] = {
    val query = VotingQuery.result
    DBM.db.run(query)
  }

  def getById(id: String): Future[Option[ModelVoting]] = {
    val query = VotingQuery.filter(_.id_voting === id.toInt).result.headOption
    DBM.db.run(query)
  }

  def deleteData(id: String): Future[Int] = {
    val deleteAction = VotingQuery.filter(_.id_voting === id.toInt).delete
    DBM.db.run(deleteAction)
  }
}
