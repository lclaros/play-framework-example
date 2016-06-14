package dal

import javax.inject.{ Inject, Singleton }
import play.api.db.slick.DatabaseConfigProvider
import slick.driver.JdbcProfile

import models.Cliente

import scala.concurrent.{ Future, ExecutionContext }

/**
 * A repository for people.
 *
 * @param dbConfigProvider The Play db config provider. Play will inject this for you.
 */
@Singleton
class ClienteRepository @Inject() (dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) {
  // We want the JdbcProfile for this provider
  private val dbConfig = dbConfigProvider.get[JdbcProfile]

  // These imports are important, the first one brings db into scope, which will let you do the actual db operations.
  // The second one brings the Slick DSL into scope, which lets you define the table and other queries.
  import dbConfig._
  import driver.api._

  /**
   * Here we define the table. It will have a name of people
   */
  private class ClientesTable(tag: Tag) extends Table[Cliente](tag, "clientes") {

    /** The ID column, which is the primary key, and auto incremented */
    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)

    /** The name column */
    def nombre = column[String]("nombre")

    /** The name column */
    def carnet = column[Int]("carnet")

    /** The age column */
    def id_asociacion = column[Int]("id_asociacion")

    /**
     * This is the tables default "projection".
     *
     * It defines how the columns are converted to and from the Cliente object.
     *
     * In this case, we are simply passing the id, name and page parameters to the Cliente case classes
     * apply and unapply methods.
     */
    def * = (id, nombre, carnet, id_asociacion) <> ((Cliente.apply _).tupled, Cliente.unapply)
  }

  /**
   * The starting point for all queries on the people table.
   */
  private val clientes = TableQuery[ClientesTable]

  /**
   * Create a person with the given name and age.
   *
   * This is an asynchronous operation, it will return a future of the created person, which can be used to obtain the
   * id for that person.
   */
  def create(nombre: String, carnet: Int, id_asociacion: Int): Future[Cliente] = db.run {
    // We create a projection of just the name and age columns, since we're not inserting a value for the id column
    (clientes.map(p => (p.nombre, p.carnet, p.id_asociacion))
      // Now define it to return the id, because we want to know what id was generated for the person
      returning clientes.map(_.id)
      // And we define a transformation for the returned value, which combines our original parameters with the
      // returned id
      into ((nameAge, id) => Cliente(id, nameAge._1, nameAge._2, nameAge._3))
    // And finally, insert the person into the database
    ) += (nombre, carnet, id_asociacion)
  }

  /**
   * List all the people in the database.
   */
  def list(): Future[Seq[Cliente]] = db.run {
    clientes.result
  }
}
