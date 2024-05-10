package Model

import akka.http.scaladsl.model.headers.Date
import org.json4s.{CustomSerializer, DefaultFormats, Formats, MappingException}
import org.json4s.JsonAST.JString
import slick.jdbc.MySQLProfile.api._
import slick.lifted.MappedToBase.mappedToIsomorphism

import java.sql.Timestamp



case class ModelComment(id_comment:Int,
                        comment:String,
                        name_student:Int,
                        time:Timestamp
                      )extends Product with Serializable



class CommentTable(tag: Tag) extends Table[ModelComment](tag, "comment") {
  def id_comment = column[Int]("id_comment", O.PrimaryKey, O.AutoInc)
  def comment = column[String]("comment")
  def name_student = column[Int]("name_student")
  def time = column[Timestamp]("time")
  def * = (id_comment, comment,name_student,time).mapTo[ModelComment]
}







