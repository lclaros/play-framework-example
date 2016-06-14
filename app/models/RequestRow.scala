package models

import play.api.libs.json._
// I will need to remove the productorId
case class RequestRow(id: Long, requestId: Long, productId: Long, productName: String, quantity: Int, precio: Double, paid: Int, status: String, unitMeasure: Long, unitMeasureName: String)

object RequestRow {
  implicit val RequestRowFormat = Json.format[RequestRow]
}