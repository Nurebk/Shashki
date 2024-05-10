package repository

//import Connection._
//import model._
//import org.mongodb.scala.Document
//import org.mongodb.scala.bson.{BsonArray, BsonDocument, BsonInt32, BsonString, ObjectId}
//
//import scala.collection.immutable.List
//import scala.concurrent.{ExecutionContext, Future}
//import scala.jdk.CollectionConverters.CollectionHasAsScala
//
//object DisciplineRepository {
//
//  implicit val ec: ExecutionContext = scala.concurrent.ExecutionContext.global
//
//  def getAllDisciplines(): Future[List[Discipline]] = {
//    val futureDisciplines = MongoDBConnection.disciplineConnection.find().toFuture()
//
//    futureDisciplines.map { docs =>
//      Option(docs).map { docList =>
//        docList.collect { case doc: Document =>
//          // Вывод содержимого документа для отладки
//          println(s"Обработка документа: $doc")
//
//          val materialsList = Option(doc.get("materials")).collect {
//            case list: java.util.List[BsonString] => list.asScala.map(_.getValue).toList
//          }.getOrElse(List.empty[String])
//
//          Discipline(
//            disciplineId = doc.getString("disciplineId"),
//            disciplineName = doc.getString("disciplineName"),
//            materials = materialsList,
//            finalAssessment = doc.getString("finalAssessment")
//          )
//        }.toList // Преобразование Seq в List
//      }.getOrElse {
//        println("Документы не найдены.")
//        List.empty
//      }
//    }
//  }
//
//  def findByParam(param: String): Future[List[Discipline]] = {
//    val keyValue = param.split("=")
//
//    if (keyValue.length == 2) {
//      val key = keyValue(0)
//      val value = keyValue(1)
//
//      val disciplineDocument = Document(key -> value)
//
//      val futureDisciplines = MongoDBConnection.disciplineCollection.find(disciplineDocument).toFuture()
//
//      futureDisciplines.map { docs =>
//        Option(docs).map { docList =>
//          docList.collect { case doc: Document =>
//            // Вывод содержимого документа для отладки
//            println(s"Обработка документа: $doc")
//
//            val materialsList = Option(doc.get("materials")).collect {
//              case list: java.util.List[BsonString] => list.asScala.map(_.getValue).toList
//            }.getOrElse(List.empty[String])
//
//            Discipline(
//              disciplineId = doc.getString("disciplineId"),
//              disciplineName = doc.getString("disciplineName"),
//              materials = materialsList,
//              finalAssessment = doc.getString("finalAssessment")
//            )
//          }.toList // Преобразование Seq в List
//        }.getOrElse {
//          println("Документы не найдены.")
//          List.empty
//        }
//      }
//    } else Future.failed(new IllegalStateException())
//  }
//
//  def getDisciplineById(disciplineId: String): Future[Option[Discipline]] = {
//    val disciplineDocument = Document("disciplineId" -> disciplineId)
//
//    MongoDBConnection.disciplineCollection.find(disciplineDocument).headOption().map {
//      case Some(doc) =>
//        Some(
//          Discipline(
//            disciplineId = doc.getString("disciplineId"),
//            disciplineName = doc.getString("disciplineName"),
//            materials = Option(doc.getList("materials", classOf[String])).map(_.asScala.toList).getOrElse(List.empty),
//            finalAssessment = doc.getString("finalAssessment")
//          )
//        )
//      case None => None
//    }
//  }
//
//  def addDiscipline(discipline: Discipline): Future[String] = {
//    val disciplineDocument = BsonDocument(
//      "disciplineId" -> Option(discipline.disciplineId),
//      "disciplineName" -> BsonString(discipline.disciplineName),
//      "materials" -> Option(discipline.materials.map(BsonString(_))),
//      "finalAssessment" -> BsonString(discipline.finalAssessment)
//    )
//
//    MongoDBConnection.disciplineConnection.insertOne(disciplineDocument).toFuture().map(_ => s"Дисциплина с идентификатором ${discipline.disciplineId} была добавлена в базу данных.")
//  }
//
//  def deleteDiscipline(disciplineId: String): Future[String] = {
//    val disciplineDocument = Document("disciplineId" -> disciplineId)
//    MongoDBConnection.disciplineCollection.deleteOne(disciplineDocument).toFuture().map(_ => s"Дисциплина с идентификатором $disciplineId была удалена из базы данных.")
//  }
//
//  def updateDiscipline(disciplineId: String, updatedDiscipline: Discipline): Future[String] = {
//    val filter = Document("disciplineId" -> disciplineId)
//
//    val disciplineDocument = BsonDocument(
//      "$set" -> BsonDocument(
//        "disciplineId" -> BsonString(updatedDiscipline.disciplineId),
//        "disciplineName" -> BsonString(updatedDiscipline.disciplineName),
//        "materials" -> Option(updatedDiscipline.materials.map(BsonString(_))),
//        "finalAssessment" -> BsonString(updatedDiscipline.finalAssessment)
//      )
//    )
//
//    MongoDBConnection.disciplineCollection.updateOne(filter, disciplineDocument).toFuture().map { updatedResult =>
//      if (updatedResult.wasAcknowledged() && updatedResult.getModifiedCount > 0) {
//        s"Дисциплина с идентификатором $disciplineId была обновлена в базе данных."
//      } else {
//        s"Обновление дисциплины неудачно: возможны проблемы с базой данных или введенными данными."
//      }
//    }
//  }
//}
