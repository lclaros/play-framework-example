package dal

import javax.inject.{ Inject, Singleton }
import play.api.db.slick.DatabaseConfigProvider
import slick.driver.JdbcProfile

import models.ProductRequestByInsumo

import scala.concurrent.{ Future, ExecutionContext, Await }
import scala.concurrent.duration._

/**
 * A repository for people.
 *
 * @param dbConfigProvider The Play db config provider. Play will inject this for you.
 */
@Singleton
class ProductRequestByInsumoRepository @Inject() (
                                                  dbConfigProvider: DatabaseConfigProvider,
                                                  repoRequestRow: RequestRowRepository,
                                                  repoProduct: ProductRepository
                                                  )(implicit ec: ExecutionContext) {
  private val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import driver.api._

  private class ProductRequestByInsumoTable(tag: Tag) extends Table[ProductRequestByInsumo](tag, "productRequest") {

    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
    def date = column[String]("date")
    def user = column[Long]("user")
    def userName = column[Long]("userName")
    def module = column[Long]("module")
    def moduleName = column[String]("moduleName")
    def status = column[String]("status")
    def detail = column[String]("detail")
    def type_1 = column[String]("type")
    def * = (id, date, user, module, moduleName, status, detail, type_1) <> ((ProductRequestByInsumo.apply _).tupled, ProductRequestByInsumo.unapply)
  }

  private val tableQ = TableQuery[ProductRequestByInsumoTable]

  def create(date: String, user: Long, module: Long, moduleName: String, status: String, detail: String, type_1: String): Future[ProductRequestByInsumo] = db.run {
    (tableQ.map(p => (p.date, p.user, p.module, p.moduleName, p.status, p.detail, p.type_1))
      returning tableQ.map(_.id)
      into ((nameAge, id) => ProductRequestByInsumo(id, nameAge._1, nameAge._2, nameAge._3, nameAge._4, nameAge._5, nameAge._6, nameAge._7))
    ) += (date, user, module, moduleName, status, detail, type_1)
  }

  def list(): Future[Seq[ProductRequestByInsumo]] = db.run {
    tableQ.result
  }

  def listByUser(id: Long): Future[Seq[ProductRequestByInsumo]] = db.run {
    tableQ.filter(_.user === id).result
  }

  def listByModule(id: Long): Future[Seq[ProductRequestByInsumo]] = db.run {
    tableQ.filter(_.module === id).result
  }

  def listByInsumoUser(id: Long): Future[Seq[ProductRequestByInsumo]] = db.run {
    tableQ.filter(_.user === id).result
  }

  def getListNames(): Future[Seq[(Long, String)]] = db.run {
    tableQ.take(200).map(s => (s.id, s.date)).result
  }

    // to cpy
  def getById(id: Long): Future[Seq[ProductRequestByInsumo]] = db.run {
    tableQ.filter(_.id === id).result
  }

  // update required to copy
  def update(id: Long, date: String, user: Long, module: Long, moduleName: String, status: String, detail: String, type_1: String): Future[Seq[ProductRequestByInsumo]] = db.run {
    val q = for { c <- tableQ if c.id === id } yield c.date
    db.run(q.update(date))
    val q2 = for { c <- tableQ if c.id === id } yield c.user
    db.run(q2.update(user))
    val q3 = for { c <- tableQ if c.id === id } yield c.module
    db.run(q3.update(module))
    val q31 = for { c <- tableQ if c.id === id } yield c.moduleName
    db.run(q31.update(moduleName))
    val q4 = for { c <- tableQ if c.id === id } yield c.status
    db.run(q4.update(status))
    val q5 = for { c <- tableQ if c.id === id } yield c.detail
    db.run(q5.update(detail))
    val q6 = for { c <- tableQ if c.id === id } yield c.type_1
    db.run(q6.update(type_1))
    tableQ.filter(_.id === id).result
  }

  // Update the status to enviado status
  def sendById(id: Long): Future[Seq[ProductRequestByInsumo]] = db.run {
    val q = for { c <- tableQ if c.id === id } yield c.status
    db.run(q.update("enviado"))
    tableQ.filter(_.id === id).result
  }

  def runUpdateChildren(id: Long) = {
    Await.result(repoRequestRow.getByParentId(id).map { rowList => 
      rowList.foreach { row => 
        if (row.status == "enviado") {
          repoRequestRow.fillById(row.id, row.productId, row.quantity).map {case (res) =>
            repoProduct.updateAmount(res(0).productId, - res(0).quantity);
          }
        }
      }
    }, 3000.millis)
  }

  // Update the status to finalizado status
  def finishById(id: Long): Future[Seq[ProductRequestByInsumo]] = db.run {
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

  // delete required
  def delete(id: Long): Future[Seq[ProductRequestByInsumo]] = db.run {
    val q = tableQ.filter(_.id === id)
    val action = q.delete
    val affectedRowsCount: Future[Int] = db.run(action)
    
    tableQ.result
  }
}
