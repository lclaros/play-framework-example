package models

import play.api.libs.json._

case class Association(id: Long, name: String)

object Association {
  implicit val associationFormat = Json.format[Association]	
}
