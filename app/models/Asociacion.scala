package models

import play.api.libs.json._

case class Asociacion(id: Long, name: String)

object Asociacion {
  implicit val AsociacionFormat = Json.format[Asociacion]
}
