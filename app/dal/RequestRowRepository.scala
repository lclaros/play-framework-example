package dal

import javax.inject.{ Inject, Singleton }
import play.api.db.slick.DatabaseConfigProvider
import slick.driver.JdbcProfile

import models.RequestRow

import scala.concurrent.{ Future, ExecutionContext }

/**
 * A repository for people.
 *
 * @param dbConfigProvider The Play db config provider. Play will inject this for you.
 */
@Singleton
class RequestRowRepository @Inject() (dbConfigProvider: DatabaseConfigProvider, repoInsum: ProductRepository)(implicit ec: ExecutionContext) {
  private val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import driver.api._

  private class RequestRowTable(tag: Tag) extends Table[RequestRow](tag, "requestRow") {

    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
    def requestId = column[Long]("requestId")
    def productId = column[Long]("productId")
    def productName = column[String]("productName")
    def quantity = column[Int]("quantity")
    def precio = column[Double]("precio")
    def paid = column[Int]("paid")
    def status = column[String]("status")
    def unitMeasure = column[Long]("unitMeasure")
    def unitMeasureName = column[String]("unitMeasureName")
    def * = (id, requestId, productId, productName, quantity, precio, paid, status, unitMeasure, unitMeasureName) <> ((RequestRow.apply _).tupled, RequestRow.unapply)
  }

  private val tableQ = TableQuery[RequestRowTable]

  def create(requestId: Long, productId: Long, productName: String, quantity: Int, precio: Double, status: String, unitMeasure: Long, unitMeasureName: String): Future[RequestRow] = db.run {
    (tableQ.map(p => (p.requestId, p.productId, p.productName, p.quantity, p.precio, p.paid, p.status, p.unitMeasure, p.unitMeasureName))
      returning tableQ.map(_.id)
      into ((nameAge, id) => RequestRow(id, nameAge._1, nameAge._2, nameAge._3, nameAge._4, nameAge._5, nameAge._6, nameAge._7, nameAge._8, nameAge._9))
    ) += (requestId, productId, productName, quantity, precio, 0, status, unitMeasure, unitMeasureName)
  }

  def list(): Future[Seq[RequestRow]] = db.run {
    tableQ.result
  }

  def listByParent(id: Long): Future[Seq[RequestRow]] = db.run {
    tableQ.filter(_.requestId === id).result
  }

  def listByQuantity(): Future[Seq[RequestRow]] = db.run {
    tableQ.filter(_.quantity > 0).result
  }

    // to cpy
  def getById(id: Long): Future[Seq[RequestRow]] = db.run {
    tableQ.filter(_.id === id).result
  }


  def getListNames(): Future[Seq[(Long, Long)]] = db.run {
    tableQ.filter(_.id < 10L).map(s => (s.id, s.id)).result
  }


  // update required to copy
  def update(id: Long, requestId: Long, productId: Long, productName: String,
               quantity: Int, precio: Double, status: String, unitMeasure: Long,
               unitMeasureName: String
             ): Future[Seq[RequestRow]] = db.run {
    val q2 = for { c <- tableQ if c.id === id } yield c.requestId
    db.run(q2.update(requestId))
    val q3 = for { c <- tableQ if c.id === id } yield c.productId
    db.run(q3.update(productId))
    val q31 = for { c <- tableQ if c.id === id } yield c.productName
    db.run(q31.update(productName))
    val q4 = for { c <- tableQ if c.id === id } yield c.quantity
    db.run(q4.update(quantity))
    val q5 = for { c <- tableQ if c.id === id } yield c.precio
    db.run(q5.update(precio))
    val q6 = for { c <- tableQ if c.id === id } yield c.status
    db.run(q6.update(status))
    val q7 = for { c <- tableQ if c.id === id } yield c.unitMeasure
    db.run(q7.update(unitMeasure))
    val q8 = for { c <- tableQ if c.id === id } yield c.unitMeasureName
    db.run(q8.update(unitMeasureName))
    tableQ.filter(_.id === id).result
  }

  // Update the status to finalizado status
  def fillById(id: Long, productId: Long, quantity: Int): Future[Seq[RequestRow]] = db.run {
    val q = for { c <- tableQ if c.id === id } yield c.status
    db.run(q.update("entregado"))
    // Update the inventory
    repoInsum.updateInventary(productId, -quantity)
    tableQ.filter(_.id === id).result
  }

  def getByParentId(id: Long): Future[Seq[RequestRow]] = db.run {
    tableQ.filter(_.requestId === id).result
  }

  // Update the status to enviado status
  def updatePaid(id: Long, monto: Int): Future[Seq[RequestRow]] = db.run {
    val q = for { c <- tableQ if c.id === id } yield c.paid
    getById(id).map { row =>
      db.run(q.update(row(0).paid + monto))
    }
    tableQ.filter(_.id === id).result
  }

  // delete required
  def delete(id: Long): Future[Seq[RequestRow]] = db.run {
    getById(id).map { row =>
      if (row(0).status == "entregado") {
        repoInsum.updateInventary(row(0).productId, row(0).quantity)
      }
      val q = tableQ.filter(_.id === id)
      val action = q.delete
      val affectedRowsCount: Future[Int] = db.run(action)
    }
    tableQ.result
  }

}
