package Connection

import org.mongodb.scala.{Document, MongoClient, MongoCollection, MongoDatabase}
import Connection._

object MongoDBConnection
{
  private val mongoClient = MongoClient("mongodb://localhost:27017")
  val database: MongoDatabase = mongoClient.getDatabase("Exam")
  val examConnection: MongoCollection[Document] = database.getCollection("Exam")




}
