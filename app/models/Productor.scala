package models

import play.api.libs.json._

case class Productor (
											id: Long, name: String, carnet: Int, telefono: Int,
											direccion: String, account: String, module: Long, 
											moduleName: String,
											associationName: String, totalDebt: Double,
											numberPayment: Int, position: String,
											acopio: Int, promedio: Int, excedent: Int, pleno: Int
					 					 )

object Productor {
  implicit val productorFormat = Json.format[Productor]	
}
