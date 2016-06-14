package models

import play.api.libs.json._

case class UnitMeasure(id: Long, name: String, quantity: Int, description: String)

object UnitMeasure {
  implicit val UnitMeasureFormat = Json.format[UnitMeasure]
}
