package models

import play.api.libs.json._

case class ProductRequestByInsumo (
                                  id: Long, date: String, userId: Long, userName: Long, moduleId: Long, moduleName: String,
                                  status: String, detail: String, type_1: String
                                  )

object ProductRequestByInsumo {
  implicit val ProductRequestByInsumoFormat = Json.format[ProductRequestByInsumo]
}