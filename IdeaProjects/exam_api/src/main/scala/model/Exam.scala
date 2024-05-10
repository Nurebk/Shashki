package model

import akka.http.scaladsl.model.DateTime
import java.util.{Date, Calendar}

import java.util.Date
case class Exam(
               examId:Int,
               exam_shedule: Date ,
               disciplineName: String,
               teacher: List[String],
               examType:String,
               facilityAuditories: String,
               groupType: String,
               link:String,
               )
