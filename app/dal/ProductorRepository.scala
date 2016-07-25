package dal

import javax.inject.{ Inject, Singleton }
import play.api.db.slick.DatabaseConfigProvider
import slick.driver.JdbcProfile

import models.Productor
import models.Association

import scala.concurrent.{ Future, ExecutionContext }

/**
 * A repository for people.
 *
 * @param dbConfigProvider The Play db config provider. Play will inject this for you.
 */
@Singleton
class ProductorRepository @Inject() (dbConfigProvider: DatabaseConfigProvider, repoLog: LogEntryRepository)(implicit ec: ExecutionContext) {
  private val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import driver.api._

  private class ProductoresTable(tag: Tag) extends Table[Productor](tag, "productor") {
    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
    def name = column[String]("name")
    def carnet = column[Int]("carnet")
    def telefono = column[Int]("telefono")
    def direccion = column[String]("direccion")
    def account = column[String]("account")
    def module = column[Long]("module")
    def moduleName = column[String]("moduleName")
    def associationName = column[String]("associationName")
    def totalDebt = column[Double]("totalDebt")
    def numberPayment = column[Int]("numberPayment")
    def position = column[String]("position")
    def acopio = column[Int]("acopio")
    def promedio = column[Int]("promedio")
    def excedent = column[Int]("excedent")
    def pleno = column[Int]("pleno")
    def * = (
              id, name, carnet, telefono, direccion, account, module, moduleName,
              associationName, totalDebt, numberPayment, position,
              acopio, promedio, excedent, pleno
            ) <> ((Productor.apply _).tupled, Productor.unapply)
  }

  private class AssociationsTable(tag: Tag) extends Table[Association](tag, "association") {
    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
    def name = column[String]("name")
    def * = (
              id, name
            ) <> ((Association.apply _).tupled, Association.unapply)
  }


  private val tableQ = TableQuery[ProductoresTable]
  private val tableQAssociation = TableQuery[AssociationsTable]

  def create(name: String, carnet: Int, telefono: Int, direccion: String,
             account: String, module: Long, moduleName: String, acopio: Int,
             promedio: Int, excedent: Int, pleno: Int
             , userId: Long, userName: String): Future[Productor] = db.run {
    repoLog.createLogEntry(repoLog.CREATE, repoLog.PRODUCTOR, userId, userName, name);
    (tableQ.map (
                  p => (
                        p.name, p.carnet, p.telefono, p.direccion, p.account,
                        p.module, p.moduleName, p.associationName, p.totalDebt, p.numberPayment,
                        p.position, p.acopio, p.promedio, p.excedent, p.pleno
                       )
                ) returning tableQ.map(_.id) into (
                                                   (nameAge, id) => 
                                                   Productor (
                                                                id, nameAge._1, nameAge._2,
                                                                nameAge._3, nameAge._4, nameAge._5,
                                                                nameAge._6, nameAge._7, nameAge._8,
                                                                nameAge._9, nameAge._10, nameAge._11,
                                                                nameAge._12, nameAge._13, nameAge._14, nameAge._15
                                                              )
                                                   )
    ) += (
            name, carnet, telefono, direccion, account,
            module, moduleName, "", 0, 0, "Productor",
            acopio, promedio, excedent, pleno
          )
  }

  def list(start: Int, interval: Int): Future[Seq[Productor]] = db.run {
    tableQ.drop(start).take(interval).result
  }

  def listByAssociation(id: Long): Future[Seq[Productor]] = db.run {
    tableQ.take(500).result
  }

  def listAssociation(): Future[Seq[Association]] = db.run {
    tableQAssociation.result
  }

  // to cpy
  def getById(id: Long): Future[Seq[Productor]] = db.run {
    tableQ.filter(_.id === id).result
  }

  def getAssociationById(id: Long): Future[Seq[Association]] = db.run {
    tableQAssociation.filter(_.id === id).result
  }

  // update required to copy
  def update(
              id: Long, name: String, carnet: Int, telefono: Int,
              direccion: String, account: String, module: Long,
              moduleName: String, associationName: String,
              totalDebt: Double, numberPayment: Int, acopio: Int,
             promedio: Int, excedent: Int, pleno: Int
             , userId: Long, userName: String
            ) : Future[Seq[Productor]] = db.run {
    repoLog.createLogEntry(repoLog.UPDATE, repoLog.PRODUCTOR, userId, userName, name);
    val q = for { c <- tableQ if c.id === id } yield c.name
    db.run(q.update(name))
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
    
    val q10 = for { c <- tableQ if c.id === id } yield c.acopio
    db.run(q10.update(acopio))
    val q11 = for { c <- tableQ if c.id === id } yield c.promedio
    db.run(q11.update(promedio))
    val q12 = for { c <- tableQ if c.id === id } yield c.excedent
    db.run(q12.update(excedent))
    val q13 = for { c <- tableQ if c.id === id } yield c.pleno
    db.run(q13.update(pleno))

    tableQ.filter(_.id === id).result
  }

  // delete required
  def delete(id: Long): Future[Seq[Productor]] = db.run {
    val q = tableQ.filter(_.id === id)
    val action = q.delete
    val affectedRowsCount: Future[Int] = db.run(action)
    tableQ.result
  }

  def getListNames(): Future[Seq[(Long, String)]] = db.run {
    tableQ.take(200).map(s => (s.id, s.name)).result
  }

  def getListNamesAssociations(): Future[Seq[Association]] = db.run {
    tableQAssociation.result
  }

  def list100Productors(): Future[Seq[Productor]] = db.run {
    tableQ.take(100).result
  }

  def searchByAccount(acc: String): Future[Seq[Productor]] = db.run {
    tableQ.filter(_.account like "%" + acc + "%").take(100).result
  }

  def searchByName(name: String): Future[Seq[Productor]] = db.run {
    tableQ.filter(_.name.toLowerCase like "%" + name.toLowerCase + "%").take(100).result
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

  def searchProductor(search: String): Future[Seq[Productor]] = db.run {
    if (!search.isEmpty) {
      tableQ.filter(p => (p.account like "%" + search + "%") || (p.name like "%" + search + "%")
                          || (p.associationName like "%" + search + "%")).drop(0).take(100).result
    } else {
      tableQ.drop(0).take(100).result
    }    
  }


}
