package models

import play.libs.Scala
import be.objectify.deadbolt.core.models.Subject

/**
 *
 * @author Steve Chaloner (steve@objectify.be)
 */

class UserSecurity (val userName: String, val rolParam: String, var rolesListParam: List[SecurityRole] = List()) extends Subject
{
  var rol = rolParam

  var rolesList: Map[String, List[SecurityRole]] = Map("admin" -> 
      List(
            new SecurityRole("role"), new SecurityRole("roleCreate"), new SecurityRole("roleList"), new SecurityRole("roleShow"), new SecurityRole("roleEdit"), new SecurityRole("roleDelete"),
            new SecurityRole("measure"), new SecurityRole("measureCreate"), new SecurityRole("measureList"), new SecurityRole("measureShow"), new SecurityRole("measureEdit"), new SecurityRole("measureDelete"),
            new SecurityRole("product"), new SecurityRole("productCreate"), new SecurityRole("productList"), new SecurityRole("productShow"), new SecurityRole("productEdit"), new SecurityRole("productDelete"),
            new SecurityRole("proveedor"), new SecurityRole("proveedorCreate"), new SecurityRole("proveedorList"), new SecurityRole("proveedorShow"), new SecurityRole("proveedorEdit"), new SecurityRole("proveedorDelete"),
            new SecurityRole("module"), new SecurityRole("moduleCreate"), new SecurityRole("moduleList"), new SecurityRole("moduleShow"), new SecurityRole("moduleEdit"), new SecurityRole("moduleDelete"),
            new SecurityRole("productor"), new SecurityRole("productorCreate"), new SecurityRole("productorList"), new SecurityRole("productorShow"), new SecurityRole("productorEdit"), new SecurityRole("productorDelete"),
            new SecurityRole("association"), new SecurityRole("associationXXXCreate"), new SecurityRole("associationXXXList"), new SecurityRole("associationShow"), new SecurityRole("associationXXXEdit"), new SecurityRole("associationXXXDelete"),
            new SecurityRole("user"), new SecurityRole("userCreate"), new SecurityRole("userList"), new SecurityRole("userShow"), new SecurityRole("userEdit"), new SecurityRole("userDelete"),
            new SecurityRole("account"), new SecurityRole("accountCreate"), new SecurityRole("accountList"), new SecurityRole("accountShow"), new SecurityRole("accountEdit"), new SecurityRole("accountDelete"),
            new SecurityRole("transaction"), new SecurityRole("transactionCreate"), new SecurityRole("transactionList"), new SecurityRole("transactionShow"), new SecurityRole("transactionEdit"), new SecurityRole("transactionDelete"),
            new SecurityRole("transactionDetail"), new SecurityRole("transactionDetailCreate"), new SecurityRole("transactionDetailList"), new SecurityRole("transactionDetailShow"), new SecurityRole("transactionDetailEdit"), new SecurityRole("transactionDetailDelete"),
            new SecurityRole("productRequest"), new SecurityRole("productRequestCreate"), new SecurityRole("productRequestList"), new SecurityRole("productRequestShow"), new SecurityRole("productRequestEdit"), new SecurityRole("productRequestDelete"), new SecurityRole("productRequestSend"), new SecurityRole("productRequestAccept"), new SecurityRole("productRequestFinish"),
            new SecurityRole("productRequestByInsumo"), new SecurityRole("productRequestByInsumoCreate"), new SecurityRole("productRequestByInsumoList"), new SecurityRole("productRequestByInsumoShow"), new SecurityRole("productRequestByInsumoEdit"), new SecurityRole("productRequestByInsumoDelete"), new SecurityRole("productRequestByInsumoSend"), new SecurityRole("productRequestByInsumoAccept"), new SecurityRole("productRequestByInsumoFinish"),
            new SecurityRole("requestRow"), new SecurityRole("requestRowCreate"), new SecurityRole("requestRowList"), new SecurityRole("requestRowShow"), new SecurityRole("requestRowEdit"), new SecurityRole("requestRowDelete"),
            new SecurityRole("requestRowByInsumo"), new SecurityRole("requestRowByInsumoCreate"), new SecurityRole("requestRowByInsumoList"), new SecurityRole("requestRowByInsumoShow"), new SecurityRole("requestRowByInsumoEdit"), new SecurityRole("requestRowByInsumoDelete"),
            new SecurityRole("requestRowProductor"), new SecurityRole("requestRowProductorCreate"), new SecurityRole("requestRowProductorList"), new SecurityRole("requestRowProductorShow"), new SecurityRole("requestRowProductorEdit"), new SecurityRole("requestRowProductorDelete"),
            new SecurityRole("discountReport"), new SecurityRole("discountReportCreate"), new SecurityRole("discountReportList"), new SecurityRole("discountReportShow"), new SecurityRole("discountReportEdit"), new SecurityRole("discountReportDelete"), new SecurityRole("discountReportFinalize"),
            new SecurityRole("discountDetail"), new SecurityRole("discountDetailCreate"), new SecurityRole("discountDetailList"), new SecurityRole("discountDetailShow"), new SecurityRole("discountDetailEdit"), new SecurityRole("discountDetailDelete"),
            new SecurityRole("productInv"), new SecurityRole("productInvCreate"), new SecurityRole("productInvList"), new SecurityRole("productInvShow"), new SecurityRole("productInvEdit"), new SecurityRole("productInvDelete"),
            new SecurityRole("report"), new SecurityRole("balanceShow"), new SecurityRole("diaryShow"), new SecurityRole("financeShow"), new SecurityRole("mayorShow"), new SecurityRole("sumasSaldosShow"),
            new SecurityRole("company"), new SecurityRole("companyShow"), new SecurityRole("companyEdit"),
            new SecurityRole("logEntry"), new SecurityRole("logEntryShow"), new SecurityRole("logEntryList")
          ), "veterinario" ->
      List(
            new SecurityRole("productRequest"), new SecurityRole("productRequestCreate"), new SecurityRole("productRequestList"),
            new SecurityRole("productRequestShow"), new SecurityRole("productRequestEdit"), new SecurityRole("productRequestDelete"),
            new SecurityRole("productRequestSend"),
            new SecurityRole("requestRow"), new SecurityRole("requestRowCreate"), new SecurityRole("requestRowList"), new SecurityRole("requestRowShow"), new SecurityRole("requestRowEdit"), new SecurityRole("requestRowDelete"),
            new SecurityRole("requestRowProductor"), new SecurityRole("requestRowProductorCreate"), new SecurityRole("requestRowProductorList"), new SecurityRole("requestRowProductorShow"), new SecurityRole("requestRowProductorEdit"), new SecurityRole("requestRowProductorDelete"),
            new SecurityRole("productor"), new SecurityRole("productorList"), new SecurityRole("productorShow"),
            new SecurityRole("product"), new SecurityRole("productList"), new SecurityRole("productShow")
          ), "almacen" ->
      List(
            new SecurityRole("measure"), new SecurityRole("measureCreate"), new SecurityRole("measureList"), new SecurityRole("measureShow"), new SecurityRole("measureEdit"), new SecurityRole("measureDelete"),
            new SecurityRole("product"), new SecurityRole("productCreate"), new SecurityRole("productList"), new SecurityRole("productShow"), new SecurityRole("productEdit"), new SecurityRole("productDelete"),
            new SecurityRole("proveedor"), new SecurityRole("proveedorCreate"), new SecurityRole("proveedorList"), new SecurityRole("proveedorShow"), new SecurityRole("proveedorEdit"), new SecurityRole("proveedorDelete"),
            new SecurityRole("productor"), new SecurityRole("productorCreate"), new SecurityRole("productorList"), new SecurityRole("productorShow"), new SecurityRole("productorEdit"), new SecurityRole("productorDelete"),
            new SecurityRole("user"), new SecurityRole("userCreate"), new SecurityRole("userList"), new SecurityRole("userShow"), new SecurityRole("userEdit"), new SecurityRole("userDelete"),
            new SecurityRole("transaction"), new SecurityRole("transactionCreate"), new SecurityRole("transactionList"), new SecurityRole("transactionShow"), new SecurityRole("transactionEdit"), new SecurityRole("transactionDelete"),
            new SecurityRole("transactionDetail"), new SecurityRole("transactionDetailCreate"), new SecurityRole("transactionDetailList"), new SecurityRole("transactionDetailShow"), new SecurityRole("transactionDetailEdit"), new SecurityRole("transactionDetailDelete"),
            new SecurityRole("productRequest"), new SecurityRole("productRequestCreate"), new SecurityRole("productRequestList"), new SecurityRole("productRequestShow"), new SecurityRole("productRequestEdit"), new SecurityRole("productRequestDelete"), new SecurityRole("productRequestSend"), new SecurityRole("productRequestAccept"), new SecurityRole("productRequestFinish"),
            new SecurityRole("requestRow"), new SecurityRole("requestRowCreate"), new SecurityRole("requestRowList"), new SecurityRole("requestRowShow"), new SecurityRole("requestRowEdit"), new SecurityRole("requestRowDelete"),
            new SecurityRole("requestRowProductor"), new SecurityRole("requestRowProductorCreate"), new SecurityRole("requestRowProductorList"), new SecurityRole("requestRowProductorShow"), new SecurityRole("requestRowProductorEdit"), new SecurityRole("requestRowProductorDelete"),
            new SecurityRole("discountReport"), new SecurityRole("discountReportCreate"), new SecurityRole("discountReportList"), new SecurityRole("discountReportShow"), new SecurityRole("discountReportEdit"), new SecurityRole("discountReportDelete"), new SecurityRole("discountReportFinalize"),
            new SecurityRole("discountDetail"), new SecurityRole("discountDetailCreate"), new SecurityRole("discountDetailList"), new SecurityRole("discountDetailShow"), new SecurityRole("discountDetailEdit"), new SecurityRole("discountDetailDelete"),
            new SecurityRole("productInv"), new SecurityRole("productInvCreate"), new SecurityRole("productInvList"), new SecurityRole("productInvShow"), new SecurityRole("productInvEdit"), new SecurityRole("productInvDelete"),
            new SecurityRole("company"), new SecurityRole("companyShow")

          ), "contabilidad" ->
      List(
            new SecurityRole("transaction"), new SecurityRole("transactionCreate"), new SecurityRole("transactionList"), new SecurityRole("transactionShow"), new SecurityRole("transactionEdit"), new SecurityRole("transactionDelete"),
            new SecurityRole("transactionDetail"), new SecurityRole("transactionDetailCreate"), new SecurityRole("transactionDetailList"), new SecurityRole("transactionDetailShow"), new SecurityRole("transactionDetailEdit"), new SecurityRole("transactionDetailDelete"),
            new SecurityRole("company"), new SecurityRole("companyShow")
          ), "insumo" ->
      List(
            new SecurityRole("measure"), new SecurityRole("measureCreate"), new SecurityRole("measureList"), new SecurityRole("measureShow"), new SecurityRole("measureEdit"), new SecurityRole("measureDelete"),
            new SecurityRole("product"), new SecurityRole("productCreate"), new SecurityRole("productList"), new SecurityRole("productShow"), new SecurityRole("productEdit"), new SecurityRole("productDelete"),
            new SecurityRole("proveedor"), new SecurityRole("proveedorCreate"), new SecurityRole("proveedorList"), new SecurityRole("proveedorShow"), new SecurityRole("proveedorEdit"), new SecurityRole("proveedorDelete"),
            new SecurityRole("module"), new SecurityRole("moduleCreate"), new SecurityRole("moduleList"), new SecurityRole("moduleShow"), new SecurityRole("moduleEdit"), new SecurityRole("moduleDelete"),
            new SecurityRole("productor"), new SecurityRole("productorCreate"), new SecurityRole("productorList"), new SecurityRole("productorShow"), new SecurityRole("productorEdit"), new SecurityRole("productorDelete"),
            new SecurityRole("user"), new SecurityRole("userList"), new SecurityRole("userShow"),
            new SecurityRole("transaction"), new SecurityRole("transactionCreate"), new SecurityRole("transactionList"), new SecurityRole("transactionShow"), new SecurityRole("transactionEdit"), new SecurityRole("transactionDelete"),
            new SecurityRole("transactionDetail"), new SecurityRole("transactionDetailCreate"), new SecurityRole("transactionDetailList"), new SecurityRole("transactionDetailShow"), new SecurityRole("transactionDetailEdit"), new SecurityRole("transactionDetailDelete"),
            new SecurityRole("productRequest"), new SecurityRole("productRequestCreate"), new SecurityRole("productRequestList"), new SecurityRole("productRequestShow"), new SecurityRole("productRequestEdit"), new SecurityRole("productRequestDelete"),
            new SecurityRole("requestRow"), new SecurityRole("requestRowCreate"), new SecurityRole("requestRowList"), new SecurityRole("requestRowShow"), new SecurityRole("requestRowEdit"), new SecurityRole("requestRowDelete"),
            new SecurityRole("requestRowProductor"), new SecurityRole("requestRowProductorCreate"), new SecurityRole("requestRowProductorList"), new SecurityRole("requestRowProductorShow"), new SecurityRole("requestRowProductorEdit"), new SecurityRole("requestRowProductorDelete"),
            new SecurityRole("productInv"), new SecurityRole("productInvCreate"), new SecurityRole("productInvList"), new SecurityRole("productInvShow"), new SecurityRole("productInvEdit"), new SecurityRole("productInvDelete"),
            new SecurityRole("company"), new SecurityRole("companyShow")            
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
