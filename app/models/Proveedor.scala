package models

import play.api.libs.json._

case class Proveedor(id: Long, nombre: String, telefono: Int, direccion: String, contacto: String, account: Long)

object Proveedor {
  implicit val proveedorFormat = Json.format[Proveedor]	
}