package models

import play.api.libs.json._

case class Measure(id: Long, name: String, quantity: Int, description: String)

object Measure {
  implicit val MeasureFormat = Json.format[Measure]
}
