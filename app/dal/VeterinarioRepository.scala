//package dal
//
//import javax.inject.{ Inject, Singleton }
//import play.api.db.slick.DatabaseConfigProvider
//import slick.driver.JdbcProfile
//
//import models.User
//
//import scala.concurrent.{ Future, ExecutionContext }
//
///**
// * A repository for people.
// *
// * @param dbConfigProvider The Play db config provider. Play will inject this for you.
// */
//@Singleton
//class UserRepository @Inject() (dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) {
//  private val dbConfig = dbConfigProvider.get[JdbcProfile]
//
//  import dbConfig._
//  import driver.api._
//
//  private class UsersTable(tag: Tag) extends Table[User](tag, "user") {
//
//    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
//    def name = column[String]("name")
//    def carnet = column[Int]("carnet")
//    def telefono = column[Int]("telefono")
//    def direccion = column[String]("direccion")
//    def sueldo = column[Int]("sueldo")
//    def type_1 = column[String]("type")
//    def * = (id, name, carnet, telefono, direccion, sueldo, type_1) <> ((User.apply _).tupled, User.unapply)
//  }
//
//  private val tableQ = TableQuery[UsersTable]
//
//  def create(name: String, carnet: Int, telefono: Int, direccion: String, sueldo: Int): Future[User] = db.run {
//    (tableQ.map(p => (p.name, p.carnet, p.telefono, p.direccion, p.sueldo, p.type_1))
//      returning tableQ.map(_.id)
//      into ((nameAge, id) => User(id, nameAge._1, nameAge._2, nameAge._3, nameAge._4, nameAge._5, nameAge._6))
//    ) += (name, carnet, telefono, direccion, sueldo, "User")
//  }
//
//  def getListNames(): Future[Seq[(Long, String)]] = db.run {
//    tableQ.filter(_.type_1 === "User").map(s => (s.id, s.name)).result
//  }
//
//  def list(): Future[Seq[User]] = db.run {
//    tableQ.result
//  }
//
//  // to cpy
//  def getById(id: Long): Future[Seq[User]] = db.run {
//    tableQ.filter(_.id === id).result
//  }
//
//  def getByLogin(user: String, password: String): Future[Seq[User]] = db.run {
//    tableQ.filter(_.name === user).result
//  }
//
//  // update required to copy
//  def update(id: Long, name: String, carnet: Int, telefono: Int, direccion: String, sueldo: Int): Future[Seq[User]] = db.run {
//    val q = for { c <- tableQ if c.id === id } yield c.name
//    db.run(q.update(name))
//    val q2 = for { c <- tableQ if c.id === id } yield c.carnet
//    db.run(q2.update(carnet))
//    val q3 = for { c <- tableQ if c.id === id } yield c.telefono
//    db.run(q3.update(telefono))
//    val q4 = for { c <- tableQ if c.id === id } yield c.sueldo
//    db.run(q4.update(sueldo))
//    tableQ.filter(_.id === id).result
//  }
//
//  // delete required
//  def delete(id: Long): Future[Seq[User]] = db.run {
//    val q = tableQ.filter(_.id === id)
//    val action = q.delete
//    val affectedRowsCount: Future[Int] = db.run(action)
//    tableQ.result
//  }
//}
//