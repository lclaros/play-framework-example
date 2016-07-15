package dal

import javax.inject.{ Inject, Singleton }
import play.api.db.slick.DatabaseConfigProvider
import slick.driver.JdbcProfile

import models.DiscountDetail
import models.RequestRow
import models.RequestRowProductor
import models.Productor

import scala.concurrent.{ Future, ExecutionContext }

/**
 * A repository for people.
 *
 * @param dbConfigProvider The Play db config provider. Play will inject this for you.
 */
@Singleton
class DiscountDetailRepository @Inject() (dbConfigProvider: DatabaseConfigProvider,  repoRequestRow: RequestRowRepository
                                          , repoProductor: ProductorRepository, repoDiscount: DiscountReportRepository)(implicit ec: ExecutionContext) {
  private val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import driver.api._

  private class DiscountDetailsTable(tag: Tag) extends Table[DiscountDetail](tag, "discountDetail") {

    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
    def discountReport = column[Long]("discountReport")
    def productorId = column[Long]("productorId")
    def productorName = column[String]("productorName")
    def status = column[String]("status")
    def discount = column[Double]("discount")
    def requestRow = column[Long]("requestRow")
    def * = (id, discountReport, productorId, productorName, status, discount, requestRow) <> ((DiscountDetail.apply _).tupled, DiscountDetail.unapply)
  }

  private val tableQ = TableQuery[DiscountDetailsTable]

  def create(discountReport: Long, productorId: Long, productorName: String, status: String, discount: Double): Future[DiscountDetail] = db.run {
    (tableQ.map(p => (p.discountReport, p.productorId, p.productorName, p.status, p.discount, p.requestRow))
      returning tableQ.map(_.id)
      into ((nameAge, id) => DiscountDetail(id, nameAge._1, nameAge._2, nameAge._3, nameAge._4, nameAge._5, nameAge._6))
    ) += (discountReport, productorId, productorName, status, discount, 1L)
  }

  def list(): Future[Seq[DiscountDetail]] = db.run {
    tableQ.result
  }

  def listByInsumo(id: Long): Future[Seq[DiscountDetail]] = db.run {
    tableQ.filter(_.discountReport === id).result
  }

  def listByReport(id: Long): Future[Seq[DiscountDetail]] = db.run {
    tableQ.filter(_.discountReport === id).result
  }

  def listByParent(id: Long): Future[Seq[DiscountDetail]] = db.run {
    tableQ.filter(_.discountReport === id).result
  }

  def listByProductor(id: Long): Future[Seq[DiscountDetail]] = db.run {
    tableQ.filter(_.productorId === id).result
  }

  // to cpy
  def getById(id: Long): Future[Seq[DiscountDetail]] = db.run {
    tableQ.filter(_.id === id).result
  }

// I will need the productor totalDebt / numberPayments = next discount/*prod.totalDebt / prod.numberPayment*//*Instead of price I have to have a price that gives me the next discount*/
// I will need the productor totalDebt / numberPayments = next discount/*prod.totalDebt / prod.numberPayment*//*Instead of price I have to have a price that gives me the next discount*/
def generarReporte(requestRows: Seq[Productor], discountReportId: Long) = {
    var totalDiscount = 0.0
    requestRows.foreach { case (productor) => 
      if (productor.numberPayment > 0 && productor.totalDebt > 0) {
        totalDiscount =  totalDiscount + productor.totalDebt / productor.numberPayment;
        val insertResult = db.run {
          (tableQ.map(discountDetail => (discountDetail.discountReport, discountDetail.productorId, discountDetail.productorName, discountDetail.status, discountDetail.discount, discountDetail.requestRow))
            returning tableQ.map(_.id)
            into ((nameAge, id) => DiscountDetail(id, nameAge._1, nameAge._2, nameAge._3, nameAge._4, nameAge._5, nameAge._6))
          ) += (discountReportId, productor.id, productor.name, "borrador", productor.totalDebt / productor.numberPayment, 0)
        };
        // after insert it we need to decrease the number payment of the productor
        insertResult.map(insertResultRow => repoProductor.updateNumberPayment(productor.id, -1).map(mm => println("DONE")));
      }
    }
    repoDiscount.updateTotal(discountReportId, totalDiscount)
    println(totalDiscount)
  }



  // update required to copy
  def update(id: Long, discountReport: Long, productorId: Long, productorName: String, status: String, discount: Double): Future[Seq[DiscountDetail]] = db.run {
    val q = for { c <- tableQ if c.id === id } yield c.discountReport
    db.run(q.update(discountReport))
    val q2 = for { c <- tableQ if c.id === id } yield c.productorId
    db.run(q2.update(productorId))
    val q31 = for { c <- tableQ if c.id === id } yield c.productorName
    db.run(q31.update(productorName))
    val q3 = for { c <- tableQ if c.id === id } yield c.status
    db.run(q3.update(status))
    val q4 = for { c <- tableQ if c.id === id } yield c.discount
    db.run(q4.update(discount))
    tableQ.filter(_.id === id).result
  }

  // delete required
  def delete(id: Long): Future[Seq[DiscountDetail]] = db.run {
    val q = tableQ.filter(_.id === id)
    val res = q.result
    val action = q.delete
    val affectedRowsCount: Future[Int] = db.run(action);
    res
  }
}
