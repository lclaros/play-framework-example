package dal

import javax.inject.{ Inject, Singleton }
import play.api.db.slick.DatabaseConfigProvider
import slick.driver.JdbcProfile

import models.ProductRequest

import scala.concurrent.{ Future, ExecutionContext, Await }
import scala.concurrent.duration._

/**
 * A repository for people.
 *
 * @param dbConfigProvider The Play db config provider. Play will inject this for you.
 */
@Singleton
class ProductRequestRepository @Inject() (dbConfigProvider: DatabaseConfigProvider, repoRequestRow: RequestRowRepository, repoProduct: ProductRepository)(implicit ec: ExecutionContext) {
  private val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import driver.api._

  private class ProductRequestTable(tag: Tag) extends Table[ProductRequest](tag, "productRequest") {

    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
    def date = column[String]("date")
    def veterinario = column[Long]("veterinario")
    def veterinarioName = column[String]("veterinarioName")
    def storekeeper = column[Long]("storekeeper")
    def storekeeperName = column[String]("storekeeperName")
    def status = column[String]("status")
    def detail = column[String]("detail")
    def type_1 = column[String]("type")
    def * = (id, date, veterinario, veterinarioName, storekeeper, storekeeperName, status, detail, type_1) <> ((ProductRequest.apply _).tupled, ProductRequest.unapply)
  }

  private val tableQ = TableQuery[ProductRequestTable]

  def create(date: String, veterinario: Long, veterinarioName: String, storekeeper: Long, storekeeperName: String, status: String, detail: String, type_1: String): Future[ProductRequest] = db.run {
    (tableQ.map(p => (p.date, p.veterinario, p.veterinarioName, p.storekeeper, p.storekeeperName, p.status, p.detail, p.type_1))
      returning tableQ.map(_.id)
      into ((nameAge, id) => ProductRequest(id, nameAge._1, nameAge._2, nameAge._3, nameAge._4, nameAge._5, nameAge._6, nameAge._7, nameAge._8))
    ) += (date, veterinario, veterinarioName, storekeeper, storekeeperName, status, detail, type_1)
  }

  def list(): Future[Seq[ProductRequest]] = db.run {
    tableQ.result
  }

  def listByVeterinario(id: Long): Future[Seq[ProductRequest]] = db.run {
    tableQ.filter(_.veterinario === id).result
  }

  def listByStorekeeper(id: Long): Future[Seq[ProductRequest]] = db.run {
    tableQ.filter(_.storekeeper === id).result
  }

  def listByInsumoUser(id: Long): Future[Seq[ProductRequest]] = db.run {
    tableQ.filter(_.veterinario === id).result
  }

  def getListNames(): Future[Seq[(Long, String)]] = db.run {
    tableQ.take(200).map(s => (s.id, s.date)).result
  }

    // to cpy
  def getById(id: Long): Future[Seq[ProductRequest]] = db.run {
    tableQ.filter(_.id === id).result
  }

  // update required to copy
  def update(id: Long, date: String, veterinario: Long, veterinarioName: String, storekeeper: Long, storekeeperName: String, status: String, detail: String, type_1: String): Future[Seq[ProductRequest]] = db.run {
    val q = for { c <- tableQ if c.id === id } yield c.date
    db.run(q.update(date))
    val q2 = for { c <- tableQ if c.id === id } yield c.veterinario
    db.run(q2.update(veterinario))
    val q21 = for { c <- tableQ if c.id === id } yield c.veterinarioName
    db.run(q21.update(veterinarioName))
    val q3 = for { c <- tableQ if c.id === id } yield c.storekeeper
    db.run(q3.update(storekeeper))
    val q31 = for { c <- tableQ if c.id === id } yield c.storekeeperName
    db.run(q31.update(storekeeperName))
    val q4 = for { c <- tableQ if c.id === id } yield c.status
    db.run(q4.update(status))
    val q5 = for { c <- tableQ if c.id === id } yield c.detail
    db.run(q5.update(detail))
    val q6 = for { c <- tableQ if c.id === id } yield c.type_1
    db.run(q6.update(type_1))
    tableQ.filter(_.id === id).result
  }

  // Update the status to enviado status
  def sendById(id: Long): Future[Seq[ProductRequest]] = db.run {
    val q = for { c <- tableQ if c.id === id } yield c.status
    db.run(q.update("enviado"))
    tableQ.filter(_.id === id).result
  }

  def runUpdateChildren(id: Long) = {
    Await.result(repoRequestRow.getByParentId(id).map { rowList => 
      rowList.foreach { row => 
        if (row.status == "enviado") {
          repoRequestRow.fillById(row.id, row.productId, row.quantity)
        }
      }
    }, 3000.millis)
  }

  // Update the status to finalizado status
  def finishById(id: Long): Future[Seq[ProductRequest]] = db.run {
    getById(id).map { pRequest =>
      if (pRequest(0).status == "enviado") {
        runUpdateChildren(id)
      }
      if (pRequest(0).status == "enviado") {
        val q = for { c <- tableQ if c.id === id } yield c.status
        db.run(q.update("finalizado"))
      }
    }
    tableQ.filter(_.id === id).result
  }

  def removeChildren(id: Long) = {
    Await.result(repoRequestRow.getByParentId(id).map { rowList => 
      rowList.foreach { row => 
        repoRequestRow.delete(row.id)
      }
    }, 3000.millis)
  }

  // delete required
  def delete(id: Long): Future[Seq[ProductRequest]] = db.run {
    
    removeChildren(id)
    val q = tableQ.filter(_.id === id)
    val action = q.delete
    val affectedRowsCount: Future[Int] = db.run(action)
    // remove all children here

    tableQ.result
  }
}
