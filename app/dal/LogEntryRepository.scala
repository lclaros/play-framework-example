package dal

import scala.concurrent.duration._
import javax.inject.{ Inject, Singleton }
import play.api.db.slick.DatabaseConfigProvider
import slick.driver.JdbcProfile

import models.LogEntry
import models.LogEntryShow

import scala.concurrent.{ Future, ExecutionContext, Await }

/**
 * A repository for people.
 *
 * @param dbConfigProvider The Play db config provider. Play will inject this for you.
 */
@Singleton
class LogEntryRepository @Inject() (dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) {

  val ACCOUNT = "Cuenta"
  val ASSOCIATION = "Asociacion"
  val COMPANY = "Configuracion"
  val DISCOUNT_DETAIL = "Detalle de Descuento"
  val DISCOUNT_REPORT = "Reporte de Descuento"
  val MEASURE = "Unidad de Medida"
  val MODULE = "Modulo"
  val PRODUCT = "Producto"
  val PRODUCT_INV = "Ingreso de Producto"
  val PRODUCT_REQUEST = "Pedido"
  val PRODUCT_REQUEST_BY_INSUMO = "Pedido"
  val PRODUCTOR = "Productor"
  val PROVEEDOR = "Proveedor"
  val REPORTE = "Reporte"
  val REQUEST_ROW = "Detalle de Reporte"
  val REQUEST_ROW_PRODUCTOR = "Entrega de Producto al productor"
  val ROLE = "Rol"
  val TRANSACTION = "Transaccion"
  val USER = "Usuario"
  val USER_PERMISSION = "Permiso"
  val USER_ROLE = "Assignacion de Permiso"

  val CREATE = "Creado"
  val UPDATE = "Actualizado"
  val LOGIN = "Login"

  private val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import driver.api._

  private class LogEntrysTable(tag: Tag) extends Table[LogEntry](tag, "logEntry") {
    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
    def action = column[String]("action")
    def tableName1 = column[String]("tableName1")
    def userId = column[Long]("userId")
    def userName = column[String]("userName")
    def description = column[String]("description")
    def * = (id, action, tableName1, userId, userName, description) <> ((LogEntry.apply _).tupled, LogEntry.unapply)
  }

  private class LogEntrysShowTable(tag: Tag) extends Table[LogEntryShow](tag, "logEntry") {
    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
    def action = column[String]("action")
    def tableName1 = column[String]("tableName1")
    def userId = column[Long]("userId")
    def userName = column[String]("userName")
    def description = column[String]("description")
    def createdAt = column[String]("createdAt")
    def * = (id, action, tableName1, userId, userName, description, createdAt) <> ((LogEntryShow.apply _).tupled, LogEntryShow.unapply)
  }

  private val tableQ = TableQuery[LogEntrysTable]
  private val tableQShow = TableQuery[LogEntrysShowTable]

  def create(action: String, tableName1: String, userId: Long, userName: String, description: String): Future[LogEntry] = db.run {
    val description_1 = tableName1 + " (" + description + ") fue " + action + " por " + userName
    (tableQ.map(p => (p.action, p.tableName1, p.userId, p.userName, p.description))
      returning tableQ.map(_.id)
      into ((nameAge, id) => LogEntry(id, nameAge._1, nameAge._2, nameAge._3, nameAge._4, nameAge._5))
    ) += (action, tableName1, userId, userName, description_1)
  }

  def createLogEntry(action: String, tableName1: String, userId: Long, userName: String, description: String) = {
    Await.result(create(action, tableName1, userId, userName, description).map(res => print("DONE")), 3000.millis)
  }

  def list(): Future[Seq[LogEntryShow]] = db.run {
    tableQShow.result
  }

  def getById(id: Long): Future[Seq[LogEntryShow]] = db.run {
    tableQShow.filter(_.id === id).result
  }
}
