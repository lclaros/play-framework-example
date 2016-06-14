package dal

import javax.inject.{ Inject, Singleton }
import play.api.db.slick.DatabaseConfigProvider
import slick.driver.JdbcProfile

import models.Reporte

import scala.concurrent.{ Future, ExecutionContext }

/**
 * A repository for people.
 *
 * @param dbConfigProvider The Play db config provider. Play will inject this for you.
 */
@Singleton
class ReporteRepository @Inject() (dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) {
  private val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import driver.api._

  private class ReportesTable(tag: Tag) extends Table[Reporte](tag, "reportes") {

    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
    def monto = column[Int]("monto")
    def account = column[Int]("account")
    def cliente = column[Int]("cliente")
    def * = (id, monto, account, cliente) <> ((Reporte.apply _).tupled, Reporte.unapply)
  }

  private val reportes = TableQuery[ReportesTable]

  def create(monto: Int, account: Int, cliente: Int): Future[Reporte] = db.run {
    (reportes.map(p => (p.monto, p.account, p.cliente))
      returning reportes.map(_.id)
      into ((nameAge, id) => Reporte(id, nameAge._1, nameAge._2, nameAge._3))
    ) += (monto, account, cliente)
  }

  def list(): Future[Seq[Reporte]] = db.run {
    reportes.result
  }
}
