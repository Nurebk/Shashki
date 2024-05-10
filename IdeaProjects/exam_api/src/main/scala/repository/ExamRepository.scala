package repository

import java.util.Date
import scala.concurrent.{ExecutionContext, Future}
import scala.jdk.CollectionConverters.CollectionHasAsScala
import org.mongodb.scala.Document
import org.mongodb.scala.bson.{BsonArray, BsonDateTime, BsonDocument, BsonInt32, BsonString}
import Connection._
import model._

import java.text.SimpleDateFormat

class ExamRepository(implicit ec: ExecutionContext) {

  def getAllExams(): Future[List[Exam]] = {
    val futureExams = MongoDBConnection.examConnection.find().toFuture()

    futureExams.map { docs =>
      Option(docs).map(_.map { doc =>
        Exam(
          examId = doc.getInteger("examId"),
          exam_shedule = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX").parse(doc.getString("exam_shedule")),
          disciplineName = doc.getString("disciplineName"),
          teacher = Option(doc.getList("teacher", classOf[String])).map(_.asScala.toList).getOrElse(List.empty),
          examType = doc.getString("examType"),
          facilityAuditories = doc.getString("facilityAuditories"),
          groupType = doc.getString("groupType"),
          link = doc.getString("link")
        )
      }.toList).getOrElse(List.empty)
    }
  }

  def getExamById(examId: String): Future[Option[Exam]] = {
    val examDocument = Document("examId" -> examId.toInt)

    MongoDBConnection.examConnection.find(examDocument).headOption().map {
      case Some(doc) =>
        Some(
          Exam(
            examId = doc.getInteger("examId"),
            exam_shedule = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX").parse(doc.getString("exam_shedule")),
            disciplineName = doc.getString("disciplineName"),
            teacher = Option(doc.getList("teacher", classOf[String])).map(_.asScala.toList).getOrElse(List.empty),
            examType = doc.getString("examType"),
            facilityAuditories = doc.getString("facilityAuditories"),
            groupType = doc.getString("groupType"),
            link = doc.getString("link")
          )
        )
      case None => None
    }
  }

  def addExam(exam: Exam): Future[String] = {
    val examDocument = BsonDocument(
      "examId" -> BsonInt32(exam.examId),
      "exam_shedule" -> new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX").format(exam.exam_shedule),
      "disciplineName" -> BsonString(exam.disciplineName),
      "teacher" -> BsonArray(exam.teacher.map(BsonString(_))),
      "examType" -> BsonString(exam.examType),
      "facilityAuditories" -> BsonString(exam.facilityAuditories),
      "groupType" -> BsonString(exam.groupType),
      "link" -> BsonString(exam.link)
    )

    MongoDBConnection.examConnection.insertOne(examDocument).toFuture().map(_ => s"Exam with ID ${exam.examId} added to the database.")
  }

  def deleteExam(examId: String): Future[String] = {
    val examDocument = Document("examId" -> examId.toInt)
    MongoDBConnection.examConnection.deleteOne(examDocument).toFuture().map(_ => s"Exam with ID $examId deleted from the database.")
  }

  def updateExam(examId: String, updatedExam: Exam): Future[String] = {
    val filter = Document("examId" -> examId.toInt)

    val examDocument = BsonDocument(
      "$set" -> BsonDocument(
        "examId" -> BsonInt32(updatedExam.examId),
        "exam_shedule" -> new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX").format(updatedExam.exam_shedule),
        "disciplineName" -> BsonString(updatedExam.disciplineName),
        "teacher" -> BsonArray(updatedExam.teacher.map(BsonString(_))),
        "examType" -> BsonString(updatedExam.examType),
        "facilityAuditories" -> BsonString(updatedExam.facilityAuditories),
        "groupType" -> BsonString(updatedExam.groupType),
        "link" -> BsonString(updatedExam.link)
      )
    )

    MongoDBConnection.examConnection.updateOne(filter, examDocument).toFuture().map { updateResult =>
      if (updateResult.wasAcknowledged() && updateResult.getModifiedCount > 0) {
        s"Exam with ID $examId updated in the database."
      } else {
        s"Update of exam with ID $examId failed. Exam not found or an error occurred in the database."
      }
    }
  }
}
