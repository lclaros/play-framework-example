package models

import play.api.libs.json._

case class Role (id: Long, roleName: String, roleCode: String)
object Role {
  implicit val RoleFormat = Json.format[Role]
}
