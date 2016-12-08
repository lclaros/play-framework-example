package models

import play.api.libs.json._

case class Driver (
											id: Long, name: String, carnet: Int, telefono: Int,
											direccion: String, account: String, companyId: Long, 
											companyName: String,
											associationName: String, totalDebt: Double,
											totalPayment: Int, position: String,
											acopio: Int, promedio: Int, excedent: Int, pleno: Int
					 					 )

object Driver {
  implicit val DriverFormat = Json.format[Driver]	
}
