package models

import play.api.libs.json._

case class Cliente(id: Long, nombre: String, carnet: Int, id_asociacion: Int)


object Cliente {
  implicit val clienteFormat = Json.format[Cliente]
}