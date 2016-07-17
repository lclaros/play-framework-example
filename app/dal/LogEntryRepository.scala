package dal

import javax.inject.{ Inject, Singleton }
import play.api.db.slick.DatabaseConfigProvider
import slick.driver.JdbcProfile

import models.LogEntry

import scala.concurrent.{ Future, ExecutionContext }

/**
 * A repository for people.
 *
 * @param dbConfigProvider The Play db config provider. Play will inject this for you.
 */
@Singleton
class LogEntryRepository @Inject() (dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) {
  private val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import driver.api._

  private class LogEntrysTable(tag: Tag) extends Table[LogEntry](tag, "LogEntry") {

    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
    def date = column[String]("date")
    def action = column[String]("action")
    def tableName_1 = column[String]("tableName_1")
    def userId = column[Long]("userId")
    def * = (id, date, action, tableName_1, userId) <> ((LogEntry.apply _).tupled, LogEntry.unapply)
  }

  private val tableQ = TableQuery[LogEntrysTable]

  def create(date: String, action: String, tableName_1: String, userId: Long): Future[LogEntry] = db.run {
    (tableQ.map(p => (p.date, p.action, p.tableName_1, p.userId))
      returning tableQ.map(_.id)
      into ((dateAge, id) => LogEntry(id, dateAge._1, dateAge._2, dateAge._3, dateAge._4))
    ) += (date, action, tableName_1, userId)
  }

  def list(): Future[Seq[LogEntry]] = db.run {
    tableQ.result
  }

  // to cpy
  def getById(id: Long): Future[Seq[LogEntry]] = db.run {
    tableQ.filter(_.id === id).result
  }

  // update required to copy
  def update(id: Long, date: String, action: String, tableName_1: String, userId: Long): Future[Seq[LogEntry]] = db.run {
    val q = for { c <- tableQ if c.id === id } yield c.date
    db.run(q.update(date))
    val q2 = for { c <- tableQ if c.id === id } yield c.action
    db.run(q2.update(action))
    val q3 = for { c <- tableQ if c.id === id } yield c.tableName_1
    db.run(q3.update(tableName_1))
    val q4 = for { c <- tableQ if c.id === id } yield c.userId
    db.run(q4.update(userId))

    tableQ.filter(_.id === id).result
  }

  // delete required
  def delete(id: Long): Future[Seq[LogEntry]] = db.run {
    val q = tableQ.filter(_.id === id)
    val action = q.delete
    val affectedRowsCount: Future[Int] = db.run(action)
    tableQ.result
  }
}
