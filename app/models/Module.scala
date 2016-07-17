package models

import play.api.libs.json._

case class Module (
										id: Long, name: String, president: Long, description: String,
										association: Long, associationName: String
									)
object Module {
  implicit val ModuleFormat = Json.format[Module]
}
