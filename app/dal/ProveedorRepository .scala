package dal

import javax.inject.{ Inject, Singleton }
import play.api.db.slick.DatabaseConfigProvider
import slick.driver.JdbcProfile

import models.Proveedor

import scala.concurrent.{ Future, ExecutionContext }

/**
 * A repository for people.
 *
 * @param dbConfigProvider The Play db config provider. Play will inject this for you.
 */
@Singleton
class ProveedorRepository @Inject() (dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) {
  private val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import driver.api._

  private class ProveedoresTable(tag: Tag) extends Table[Proveedor](tag, "proveedor") {

    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
    def nombre = column[String]("nombre")
    def telefono = column[Int]("telefono")
    def direccion = column[String]("direccion")
    def contacto = column[String]("contacto")
    def account = column[Long]("account")
    def * = (id, nombre, telefono, direccion, contacto, account) <> ((Proveedor.apply _).tupled, Proveedor.unapply)
  }

  private val tableQ = TableQuery[ProveedoresTable]

  def create(nombre: String, telefono: Int, direccion: String, contacto: String, account: Long): Future[Proveedor] = db.run {
    (tableQ.map(p => (p.nombre, p.telefono, p.direccion, p.contacto, p.account))
      returning tableQ.map(_.id)
      into ((nameAge, id) => Proveedor(id, nameAge._1, nameAge._2, nameAge._3, nameAge._4, nameAge._5))
    ) += (nombre, telefono, direccion, contacto, account)
  }

  def list(): Future[Seq[Proveedor]] = db.run {
    tableQ.result
  }
  
    // to cpy
  def getById(id: Long): Future[Seq[Proveedor]] = db.run {
    tableQ.filter(_.id === id).result
  }

  // update required to copy
  def update(id: Long, nombre: String, telefono: Int, direccion: String, contacto: String, account: Long): Future[Seq[Proveedor]] = db.run {
    val q = for { c <- tableQ if c.id === id } yield c.nombre
    db.run(q.update(nombre))
    val q3 = for { c <- tableQ if c.id === id } yield c.telefono
    db.run(q3.update(telefono))
    val q2 = for { c <- tableQ if c.id === id } yield c.direccion
    db.run(q2.update(direccion))
    val q4 = for { c <- tableQ if c.id === id } yield c.contacto
    db.run(q4.update(contacto))
    val q5 = for { c <- tableQ if c.id === id } yield c.account
    db.run(q5.update(account))
    tableQ.filter(_.id === id).result
  }

  // delete required
  def delete(id: Long): Future[Seq[Proveedor]] = db.run {
    val q = tableQ.filter(_.id === id)
    val action = q.delete
    val affectedRowsCount: Future[Int] = db.run(action)
    
    tableQ.result
  }

  // get list of names
  def getListNames(): Future[Seq[(Long, String)]] = db.run {
    tableQ.filter(_.id < 10L).map(s => (s.id, s.nombre)).result
  }
}
