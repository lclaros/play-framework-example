package dal

import javax.inject.{ Inject, Singleton }
import play.api.db.slick.DatabaseConfigProvider
import slick.driver.JdbcProfile

import models.RequestRowProductor

import scala.concurrent.{ Future, ExecutionContext }

/**
 * A repository for people.
 *
 * @param dbConfigProvider The Play db config provider. Play will inject this for you.
 */
@Singleton
class RequestRowProductorRepository @Inject() (dbConfigProvider: DatabaseConfigProvider, repoInsum: ProductRepository)(implicit ec: ExecutionContext) {
  private val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import driver.api._

  private class RequestRowProductorTable(tag: Tag) extends Table[RequestRowProductor](tag, "requestRowProductor") {

    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
    def requestRowId = column[Long]("requestRowId")
    def productId = column[Long]("productId")
    def productName = column[String]("productName")
    def productorId = column[Long]("productorId")
    def productorName = column[String]("productorName")
    def quantity = column[Int]("quantity")
    def price = column[Double]("price")
    def paid = column[Double]("paid")
    def status = column[String]("status")
    def * = (id, requestRowId, productId, productName, productorId, productorName, quantity, price, paid, status) <> ((RequestRowProductor.apply _).tupled, RequestRowProductor.unapply)
  }

  private val tableQ = TableQuery[RequestRowProductorTable]

  def create(requestRowId: Long, productId: Long, productName: String, productorId: Long, productorName: String, quantity: Int, price: Double, status: String): Future[RequestRowProductor] = db.run {
    (tableQ.map(p => (p.requestRowId, p.productId, p.productName, p.productorId, p.productorName, p.quantity, p.price, p.paid, p.status))
      returning tableQ.map(_.id)
      into ((nameAge, id) => RequestRowProductor(id, nameAge._1, nameAge._2, nameAge._3, nameAge._4, nameAge._5, nameAge._6, nameAge._7, nameAge._8, nameAge._9))
    ) += (requestRowId, productId, productName, productorId, productorName, quantity, price, 0, status)
  }

  def list(): Future[Seq[RequestRowProductor]] = db.run {
    tableQ.result
  }

  def listByQuantity(): Future[Seq[RequestRowProductor]] = db.run {
    tableQ.filter(_.quantity > 0).result
  }

    // to cpy
  def getById(id: Long): Future[Seq[RequestRowProductor]] = db.run {
    tableQ.filter(_.id === id).result
  }

    // to cpy
  def requestRowProductorsByProductor(id: Long): Future[Seq[RequestRowProductor]] = db.run {
    tableQ.filter(_.productorId === id).result
  }

    // to cpy
  def requestRowProductorsByRequestRow(id: Long): Future[Seq[RequestRowProductor]] = db.run {
    tableQ.filter(_.requestRowId === id).result
  }

  def listByParent(id: Long): Future[Seq[RequestRowProductor]] = db.run {
    tableQ.filter(_.requestRowId === id).result
  }

  def listByProductor(id: Long): Future[Seq[RequestRowProductor]] = db.run {
    tableQ.filter(_.productorId === id).result
  }

  def getListNames(): Future[Seq[(Long, String)]] = db.run {
    tableQ.take(200).map(s => (s.id, s.productorId.toString())).result
  }

  // update required to copy
  def update(id: Long, requestRowId: Long, productId: Long, productName: String, productorId: Long, productorName: String, quantity: Int, price: Double, status: String): Future[Seq[RequestRowProductor]] = db.run {
    val q2 = for { c <- tableQ if c.id === id } yield c.requestRowId
    db.run(q2.update(requestRowId))
    val q = for { c <- tableQ if c.id === id } yield c.productId
    db.run(q.update(productId))
    val q1 = for { c <- tableQ if c.id === id } yield c.productName
    db.run(q1.update(productName))
    val q3 = for { c <- tableQ if c.id === id } yield c.productorId
    db.run(q3.update(productorId))
    val q31 = for { c <- tableQ if c.id === id } yield c.productorName
    db.run(q31.update(productorName))
    val q4 = for { c <- tableQ if c.id === id } yield c.quantity
    db.run(q4.update(quantity))
    val q5 = for { c <- tableQ if c.id === id } yield c.price
    db.run(q5.update(price))
    val q6 = for { c <- tableQ if c.id === id } yield c.status
    db.run(q6.update(status))
    tableQ.filter(_.id === id).result
  }

  // Update the status to enviado status
  def sendById(id: Long): Future[Seq[RequestRowProductor]] = db.run {
    val q = for { c <- tableQ if c.id === id } yield c.status
    db.run(q.update("enviado"))
    tableQ.filter(_.id === id).result
  }

  // Update the status to enviado status
  def acceptById(id: Long): Future[Seq[RequestRowProductor]] = db.run {
    val q = for { c <- tableQ if c.id === id } yield c.status
    db.run(q.update("aceptado"))
    tableQ.filter(_.id === id).result
  }

  // Update the status to enviado status
  def updatePaid(id: Long, monto: Double): Future[Seq[RequestRowProductor]] = db.run {
    val q = for { c <- tableQ if c.id === id } yield c.paid
    getById(id).map { row =>
      db.run(q.update(row(0).paid + monto))
    }
    tableQ.filter(_.id === id).result
  }

  // Update the status to finalizado status
  def finishById(id: Long): Future[Seq[RequestRowProductor]] = db.run {
    val q = for { c <- tableQ if c.id === id } yield c.status
    db.run(q.update("finalizado"))
    tableQ.filter(_.id === id).result
  }

  // delete required
  def delete(id: Long): Future[Seq[RequestRowProductor]] = db.run {
    getById(id).map { row =>
      repoInsum.updateInventary(row(0).productId, row(0).quantity)
      val q = tableQ.filter(_.id === id)
      val action = q.delete
      val affectedRowsCount: Future[Int] = db.run(action)
      
    }
    tableQ.result
  }
}
