package models

import play.api.libs.json._

case class LogEntry(id: Long, date: String, action: String, tableName_1: String, userId: Long)

object LogEntry {
  implicit val LogEntryFormat = Json.format[LogEntry]
}
