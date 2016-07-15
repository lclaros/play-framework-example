package models

import play.api.libs.json._

case class Company (id: Long, name: String, president: String, description: String )

object Company {
  implicit val CompanyFormat = Json.format[Company]
}
