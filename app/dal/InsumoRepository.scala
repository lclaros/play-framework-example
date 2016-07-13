package dal

import javax.inject.{ Inject, Singleton }
import play.api.db.slick.DatabaseConfigProvider
import slick.driver.JdbcProfile

import models.Product

import scala.concurrent.{ Future, ExecutionContext }

/**
 * A repository for people.
 *
 * @param dbConfigProvider The Play db config provider. Play will inject this for you.
 */
@Singleton
class ProductRepository @Inject() (dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) {
  private val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import driver.api._

  private class ProductsTable(tag: Tag) extends Table[Product](tag, "product") {

    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
    def nombre = column[String]("nombre")
    def cost = column[Double]("cost")
    def percent = column[Double]("percent")
    def price = column[Double]("price")
    def descripcion = column[String]("descripcion")
    def unitMeasure = column[Long]("unitMeasure")
    def unitMeasureName = column[String]("unitMeasureName")
    def currentAmount = column[Int]("currentAmount")
    def * = (id, nombre, cost, percent, price, descripcion, unitMeasure, unitMeasureName, currentAmount) <> ((Product.apply _).tupled, Product.unapply)
  }

  private val tableQ = TableQuery[ProductsTable]

  def create( 
              nombre: String, cost: Double, percent: Double, price: Double,
              descripcion: String, unitMeasure: Long, unitMeasureName: String,
              currentAmount: Int
            ): Future[Product] = db.run {
    (tableQ.map(
                  p => (
                          p.nombre, p.cost, p.percent, p.price, p.descripcion,
                          p.unitMeasure, p.unitMeasureName, p.currentAmount
                        ) 
                ) returning tableQ.map(_.id) into (
                                                    (nameAge, id) => Product(
                                                                              id, nameAge._1, nameAge._2, nameAge._3,
                                                                              nameAge._4, nameAge._5, nameAge._6,
                                                                              nameAge._7, nameAge._8
                                                                              )
                                                  )
                ) += (nombre, cost, percent, cost + cost * percent, descripcion, unitMeasure, unitMeasureName, currentAmount)
  }

  def list(): Future[Seq[Product]] = db.run {
    tableQ.result
  }

  def getListNames(): Future[Seq[(Long, String)]] = db.run {
    tableQ.filter(_.id < 10L).map(s => (s.id, s.nombre)).result
  }

  def getListNamesById(id: Long): Future[Seq[(Long, String)]] = db.run {
    tableQ.filter(_.id === id).map(s => (s.id, s.nombre)).result
  }

    // to cpy
  def getById(id: Long): Future[Seq[Product]] = db.run {
    tableQ.filter(_.id === id).result
  }

  // update required to copy
  def update( id: Long, nombre: String, cost: Double, percent: Double, price: Double,
              descripcion: String, unitMeasure: Long, unitMeasureName: String,
              currentAmount: Int
            ): Future[Seq[Product]] = db.run {
    val q = for { c <- tableQ if c.id === id } yield c.nombre
    db.run(q.update(nombre))
    val q2 = for { c <- tableQ if c.id === id } yield c.percent
    db.run(q2.update(percent))
    val q3 = for { c <- tableQ if c.id === id } yield c.cost
    db.run(q3.update(cost))
    val q31 = for { c <- tableQ if c.id === id } yield c.price
    db.run(q31.update(cost + cost * percent))
    val q4 = for { c <- tableQ if c.id === id } yield c.descripcion
    db.run(q4.update(descripcion))
    val q5 = for { c <- tableQ if c.id === id } yield c.unitMeasure
    db.run(q5.update(unitMeasure))
    val q51 = for { c <- tableQ if c.id === id } yield c.unitMeasureName
    db.run(q51.update(unitMeasureName))
    val q6 = for { c <- tableQ if c.id === id } yield c.currentAmount
    db.run(q6.update(currentAmount))
    tableQ.filter(_.id === id).result
  }

  // delete required
  def delete(id: Long): Future[Seq[Product]] = db.run {
    val q = tableQ.filter(_.id === id)
    val action = q.delete
    val affectedRowsCount: Future[Int] = db.run(action)
    println("removed " + affectedRowsCount);
    tableQ.result
  }

  def updateAmount(insumoId: Long, amount: Int) = {
    val q = for { c <- tableQ if c.id === insumoId } yield c.currentAmount
    db.run(tableQ.filter(_.id === insumoId).result).map(s=> s.map(insumoObj => 
      db.run(q.update(amount + insumoObj.currentAmount))
    ))
  }

  def updateInventary(insumoId: Long, amount: Int) = {
    val q = for { c <- tableQ if c.id === insumoId } yield c.currentAmount
    db.run(tableQ.filter(_.id === insumoId).result).map(s=> s.map(insumoObj => 
      db.run(q.update(amount + insumoObj.currentAmount))
    ))
  }
}
