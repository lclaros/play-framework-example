package models

import play.api.libs.json._

case class Product(
										id: Long, nombre: String, cost: Double,
										percent: Double, price: Double, descripcion: String,
										unitMeasure: Long, unitMeasureName: String, currentAmount: Int
									)

object Product {
  implicit val insumoFormat = Json.format[Product]
}