package models

import play.libs.Scala
import be.objectify.deadbolt.core.models.Subject

/**
 *
 * @author Steve Chaloner (steve@objectify.be)
 */

class UserSecurity(val userName: String) extends Subject
{
  var rol = "admin";

  var rolesList: Map[String, List[SecurityRole]] = Map("Admin" -> 
      List(
            new SecurityRole("measureCreate"), new SecurityRole("measureList"), new SecurityRole("measureShow"), new SecurityRole("measureEdit"), new SecurityRole("measureDelete"),
            new SecurityRole("productCreate"), new SecurityRole("productList"), new SecurityRole("productShow"), new SecurityRole("productEdit"), new SecurityRole("productDelete"),
            new SecurityRole("proveedorCreate"), new SecurityRole("proveedorList"), new SecurityRole("proveedorShow"), new SecurityRole("proveedorEdit"), new SecurityRole("proveedorDelete"),
            new SecurityRole("moduleCreate"), new SecurityRole("moduleList"), new SecurityRole("moduleShow"), new SecurityRole("moduleEdit"), new SecurityRole("moduleDelete"),
            new SecurityRole("productorCreate"), new SecurityRole("productorList"), new SecurityRole("productorShow"), new SecurityRole("productorEdit"), new SecurityRole("productorDelete"),
            new SecurityRole("userCreate"), new SecurityRole("userList"), new SecurityRole("userShow"), new SecurityRole("userEdit"), new SecurityRole("userDelete"),
            new SecurityRole("accountCreate"), new SecurityRole("accountList"), new SecurityRole("accountShow"), new SecurityRole("accountEdit"), new SecurityRole("accountDelete")
            //new SecurityRole("transactionCreate"), new SecurityRole("transactionList"), new SecurityRole("transactionShow"), new SecurityRole("transactionEdit"), new SecurityRole("transactionDelete"), 
            //new SecurityRole("reportCreate"), new SecurityRole("reportList"), new SecurityRole("reportShow"), new SecurityRole("reportEdit"), new SecurityRole("reportDelete")
          ), "Veterinario" ->
      List(
            new SecurityRole("veterinario"), new SecurityRole("report")
          ), "Almacen" ->
      List(
            new SecurityRole("almacen"), new SecurityRole("report")
          ), "contabilidad" ->
      List(
            new SecurityRole("contabilidad"), new SecurityRole("report")
          ), "Insumo" ->
      List(
            new SecurityRole("insumo"), new SecurityRole("report")
          )
    )

  def getRoles: java.util.List[SecurityRole] = {
    Scala.asJava(rolesList(rol))
  }

  def getPermissions: java.util.List[UserPermission] = {
    Scala.asJava(List(new UserPermission("printers.edit")))
  }

  def getIdentifier: String = userName
}
