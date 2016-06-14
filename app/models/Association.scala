package models

import play.api.libs.json._

case class Association(id: Long, nombre: String, cuenta: Long)

object Association {
  implicit val associationFormat = Json.format[Association]	
}
