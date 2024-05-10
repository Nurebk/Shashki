package Model

import akka.http.scaladsl.model.headers.Date
import org.json4s.{CustomSerializer, DefaultFormats, Formats, MappingException}
import org.json4s.JsonAST.JString
import slick.jdbc.MySQLProfile.api._
import slick.lifted.MappedToBase.mappedToIsomorphism

import java.sql.Timestamp
object Auditorium extends Enumeration{
  type Auditorium = Value
  val Bachelor_course,Magistracy,Doctoral_studies,all=Value

  implicit val languageColumnType: BaseColumnType[Auditorium] =
    MappedColumnType.base[Auditorium, String](_.toString, Auditorium.withName)



}
object EnumSerializer extends CustomSerializer[Enumeration#Value](format => (
  {
    case JString(s) =>
      // Проверяем, есть ли среди значений PaymentType
      if (Auditorium.values.exists(_.toString == s)) {
        Auditorium.withName(s)
      } else {
        throw new MappingException(s"Unknown enumeration value: $s")
      }
    case value =>
      throw new MappingException(s"Can't convert $value to Enumeration")
  },
  {
    case enumValue: Enumeration#Value =>
      JString(enumValue.toString)
  }
))

object JsonFormatss {
  implicit val formats: Formats = DefaultFormats + EnumSerializer
}
case class ModelEvent(id_event:Int,
                      name_event:String,
                      content_event:String,
                      comments:Int,
                      golos:Int,
                      location:String,
                      date:Timestamp,
                      creator:String,
                      auditorium: Auditorium.Value
                     )extends Product with Serializable



class EventTable(tag: Tag) extends Table[ModelEvent](tag, "event") {
  def id_event = column[Int]("id_event")
  def name_event = column[String]("name_event")
  def content_event = column[String]("content_event")
  def comments=column[Int]("comments")
  def golos=column[Int]("golos")
  def location = column[String]("location")
  def date = column[Timestamp]("date")
  def creator = column[String]("creator")
  def auditorium=column[Auditorium.Auditorium]("auditorium")

  def * = (id_event, name_event, content_event, comments,golos, location, date, creator, auditorium).mapTo[ModelEvent]
}






