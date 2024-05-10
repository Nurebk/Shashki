package repository

//package repository
//
//import org.mongodb.scala.bson.collection.immutable.Document
//import org.mongodb.scala.model.Filters._
//import org.mongodb.scala.model.Updates._
//import org.mongodb.scala.result.UpdateResult
//import org.mongodb.scala.{MongoCollection, MongoDatabase}
//import scala.concurrent.{ExecutionContext, Future}
//import scala.jdk.CollectionConverters.CollectionHasAsScala
//import model.FaculityAuditories
//
//object FaculityAuditoriesRepository {
//  implicit val ec: ExecutionContext = scala.concurrent.ExecutionContext.global
//  private val collectionName = "faculityAuditories"
//  private val faculityAuditoriesCollection: MongoCollection[Document] =
//    MongoDBConnection.database.getCollection(collectionName)
//
//  def getAllFaculityAuditories(): Future[List[FaculityAuditories]] = {
//    faculityAuditoriesCollection.find().toFuture().map { docs =>
//      docs.map(FaculityAuditories.fromDocument).toList
//    }
//  }
//
//  def getFaculityAuditoriesById(faculityAuditoriesId: Int): Future[Option[FaculityAuditories]] = {
//    val filter = equal("faculityAuditoriesId", faculityAuditoriesId)
//    faculityAuditoriesCollection.find(filter).headOption().map {
//      case Some(doc) => Some(FaculityAuditories.fromDocument(doc))
//      case None => None
//    }
//  }
//
//  def addFaculityAuditories(faculityAuditories: FaculityAuditories): Future[String] = {
//    val document = FaculityAuditories.toDocument(faculityAuditories)
//    faculityAuditoriesCollection.insertOne(document).toFuture().map { _ =>
//      s"Факультетская аудитория с ID ${faculityAuditories.faculityAuditoriesId} добавлена в базу данных."
//    }
//  }
//
//  def updateFaculityAuditories(faculityAuditoriesId: Int, updatedFaculityAuditories: FaculityAuditories): Future[String] = {
//    val filter = equal("faculityAuditoriesId", faculityAuditoriesId)
//    val update = set("auditoriaName", updatedFaculityAuditories.auditoriaName) ++
//      set("capacity", updatedFaculityAuditories.capacity)
//    faculityAuditoriesCollection.updateOne(filter, update).toFuture().map { result =>
//      if (result.wasAcknowledged() && result.getModifiedCount > 0)
//        s"Факультетская аудитория с ID $faculityAuditoriesId обновлена в базе данных."
//      else
//        "Обновление не удалось: аудитория не найдена или есть проблемы с данными."
//    }
//  }
//
//  def deleteFaculityAuditories(faculityAuditoriesId: Int): Future[String] = {
//    val filter = equal("faculityAuditoriesId", faculityAuditoriesId)
//    faculityAuditoriesCollection.deleteOne(filter).toFuture().map { result =>
//      if (result.wasAcknowledged() && result.getDeletedCount > 0)
//        s"Факультетская аудитория с ID $faculityAuditoriesId удалена из базы данных."
//      else
//        s"Факультетская аудитория с ID $faculityAuditoriesId не найдена в базе данных."
//    }
//  }
//}
