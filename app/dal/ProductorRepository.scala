package dal

import javax.inject.{ Inject, Singleton }
import play.api.db.slick.DatabaseConfigProvider
import slick.driver.JdbcProfile

import models.Productor

import scala.concurrent.{ Future, ExecutionContext }

/**
 * A repository for people.
 *
 * @param dbConfigProvider The Play db config provider. Play will inject this for you.
 */
@Singleton
class ProductorRepository @Inject() (dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) {
  private val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import driver.api._

  private class ProductoresTable(tag: Tag) extends Table[Productor](tag, "productor") {
    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
    def nombre = column[String]("nombre")
    def carnet = column[Int]("carnet")
    def telefono = column[Int]("telefono")
    def direccion = column[String]("direccion")
    def account = column[String]("account")
    def module = column[Long]("module")
    def moduleName = column[String]("moduleName")
    def asociacionName = column[String]("asociacionName")
    def totalDebt = column[Double]("totalDebt")
    def numberPayment = column[Int]("numberPayment")
    def position = column[String]("position")
    def * = (
              id, nombre, carnet, telefono, direccion, account, module, moduleName,
              asociacionName, totalDebt, numberPayment, position
            ) <> ((Productor.apply _).tupled, Productor.unapply)
  }

  private val tableQ = TableQuery[ProductoresTable]

  def create(nombre: String, carnet: Int, telefono: Int, direccion: String,
             account: String, module: Long, moduleName: String): Future[Productor] = db.run {
    (tableQ.map (
                  p => (
                        p.nombre, p.carnet, p.telefono, p.direccion, p.account,
                        p.module, p.moduleName, p.asociacionName, p.totalDebt, p.numberPayment, p.position
                       )
                ) returning tableQ.map(_.id) into (
                                                   (nameAge, id) => 
                                                   Productor (
                                                                id, nameAge._1, nameAge._2,
                                                                nameAge._3, nameAge._4, nameAge._5,
                                                                nameAge._6, nameAge._7, nameAge._8,
                                                                nameAge._9, nameAge._10, nameAge._11
                                                              )
                                                   )
    ) += (nombre, carnet, telefono, direccion, account, module, moduleName, "", 0, 0, "Productor")
  }

  def list(start: Int, interval: Int): Future[Seq[Productor]] = db.run {
    tableQ.drop(start).take(interval).result
  }

  // to cpy
  def getById(id: Long): Future[Seq[Productor]] = db.run {
    tableQ.filter(_.id === id).result
  }

  // update required to copy
  def update(
              id: Long, nombre: String, carnet: Int, telefono: Int,
              direccion: String, account: String, module: Long,
              moduleName: String, asociacionName: String,
              totalDebt: Double, numberPayment: Int,
              position: String
            ) : Future[Seq[Productor]] = db.run {

    val q = for { c <- tableQ if c.id === id } yield c.nombre
    db.run(q.update(nombre))
    val q2 = for { c <- tableQ if c.id === id } yield c.carnet
    db.run(q2.update(carnet))
    val q3 = for { c <- tableQ if c.id === id } yield c.telefono
    db.run(q3.update(telefono))
    val q4 = for { c <- tableQ if c.id === id } yield c.account
    db.run(q4.update(account))
    val q5 = for { c <- tableQ if c.id === id } yield c.module
    db.run(q5.update(module))
    val q6 = for { c <- tableQ if c.id === id } yield c.direccion
    db.run(q6.update(direccion))
    val q7 = for { c <- tableQ if c.id === id } yield c.moduleName
    db.run(q7.update(moduleName))
    val q8 = for { c <- tableQ if c.id === id } yield c.totalDebt
    db.run(q8.update(totalDebt))
    val q9 = for { c <- tableQ if c.id === id } yield c.numberPayment
    db.run(q9.update(numberPayment))
    val q10 = for { c <- tableQ if c.id === id } yield c.position
    db.run(q10.update(position))

    tableQ.filter(_.id === id).result
  }

  // delete required
  def delete(id: Long): Future[Seq[Productor]] = db.run {
    val q = tableQ.filter(_.id === id)
    val action = q.delete
    val affectedRowsCount: Future[Int] = db.run(action)
    println("removed " + affectedRowsCount);
    tableQ.result
  }

  def getListNames(): Future[Seq[(Long, String)]] = db.run {
    tableQ.filter(_.id < 10L).map(s => (s.id, s.nombre)).result
  }

  def list100Productors(): Future[Seq[Productor]] = db.run {
    tableQ.take(100).result
  }

  def searchByAccount(acc: String): Future[Seq[Productor]] = db.run {
    tableQ.filter(_.account like "%" + acc + "%").take(100).result
  }

  def searchByName(name: String): Future[Seq[Productor]] = db.run {
    tableQ.filter(_.nombre.toLowerCase like "%" + name.toLowerCase + "%").take(100).result
  }

  def getTotal(): Future[Int] = db.run {
    tableQ.length.result
  }

    // Update the status to enviado status
  def updateTotalDebt(id: Long, monto: Double): Future[Seq[Productor]] = db.run {
    val q = for { c <- tableQ if c.id === id } yield c.totalDebt
    getById(id).map { row =>
      db.run(q.update(row(0).totalDebt + monto))
    }
    tableQ.filter(_.id === id).result
  }

    // Update it when generate the report
  def updateNumberPayment(id: Long, numberPayment: Int): Future[Seq[Productor]] = db.run {
    val q = for { c <- tableQ if c.id === id } yield c.numberPayment
    getById(id).map { row =>
      db.run(q.update(row(0).numberPayment + numberPayment))
    }
    tableQ.filter(_.id === id).result
  }

  def listByTotalDebt(): Future[Seq[Productor]] = db.run {
    tableQ.filter(_.totalDebt > 0.0).result
  }

  def getByAccount2(account: String): Future[Seq[Productor]] = db.run {
    tableQ.filter(_.account like "%" + account + "%").result
  }

  def getByAccount(account: String): Future[Seq[Productor]] = db.run {
    tableQ.filter(_.account like "%" + account + "%").drop(0).take(100).result
  }


}
