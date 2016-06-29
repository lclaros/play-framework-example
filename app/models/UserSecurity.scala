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
                                  new SecurityRole("product"), new SecurityRole("user"),
                                  new SecurityRole("productor"), new SecurityRole("module"),
                                  new SecurityRole("veterinario"), new SecurityRole("storekeeper"),
                                  new SecurityRole("proveedor"), new SecurityRole("admin"),
                                  new SecurityRole("insumo"), new SecurityRole("account"),
                                  new SecurityRole("transaction"), new SecurityRole("report")
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
