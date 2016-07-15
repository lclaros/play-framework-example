package dal

import javax.inject.{ Inject, Singleton }
import play.api.db.slick.DatabaseConfigProvider
import slick.driver.JdbcProfile

import models.Measure

import scala.concurrent.{ Future, ExecutionContext }

/**
 * A repository for people.
 *
 * @param dbConfigProvider The Play db config provider. Play will inject this for you.
 */
@Singleton
class MeasureRepository @Inject() (dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) {
  private val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import driver.api._

  private class MeasuresTable(tag: Tag) extends Table[Measure](tag, "Measure") {

    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
    def name = column[String]("name")
    def quantity = column[Int]("quantity")
    def description = column[String]("description")
    def * = (id, name, quantity, description) <> ((Measure.apply _).tupled, Measure.unapply)
  }

  private val tableQ = TableQuery[MeasuresTable]

  def create(name: String, quantity: Int, description: String): Future[Measure] = db.run {
    (tableQ.map(p => (p.name, p.quantity, p.description))
      returning tableQ.map(_.id)
      into ((nameAge, id) => Measure(id, nameAge._1, nameAge._2, nameAge._3))
    ) += (name, quantity, description)
  }

  def list(): Future[Seq[Measure]] = db.run {
    tableQ.result
  }

  // to cpy
  def getById(id: Long): Future[Seq[Measure]] = db.run {
    tableQ.filter(_.id === id).result
  }

  // update required to copy
  def update(id: Long, name: String, quantity: Int, description: String): Future[Seq[Measure]] = db.run {
    val q = for { c <- tableQ if c.id === id } yield c.name
    db.run(q.update(name))
    val q2 = for { c <- tableQ if c.id === id } yield c.quantity
    db.run(q2.update(quantity))
    val q3 = for { c <- tableQ if c.id === id } yield c.description
    db.run(q3.update(description))
    tableQ.filter(_.id === id).result
  }

  // delete required
  def delete(id: Long): Future[Seq[Measure]] = db.run {
    val q = tableQ.filter(_.id === id)
    val action = q.delete
    val affectedRowsCount: Future[Int] = db.run(action)
    tableQ.result
  }

  def getListNames(): Future[Seq[(Long, String)]] = db.run {
    tableQ.filter(_.id < 10L).map(s => (s.id, s.name)).result
  }

}
