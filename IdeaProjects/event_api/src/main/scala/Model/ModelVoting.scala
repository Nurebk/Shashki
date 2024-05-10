package Model

import slick.jdbc.MySQLProfile.api._

case class ModelVoting(id_voting:Int,
                       golos:Int
                     )extends Product with Serializable

class VotingTable(tag: Tag) extends Table[ModelVoting](tag, "voting") {
  def id_voting = column[Int]("id_voting", O.PrimaryKey, O.AutoInc)
  def golos = column[Int]("golos")
  def * = (id_voting, golos).mapTo[ModelVoting]
}







