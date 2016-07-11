package controllers

import play.api._
import play.api.mvc._
import play.api.i18n._
import play.api.data.Form
import play.api.data.Forms._
import play.api.data.validation.Constraints._
import play.api.libs.json.Json
import models._
import dal._

import scala.concurrent.{ ExecutionContext, Future }

import javax.inject._
import be.objectify.deadbolt.scala.DeadboltActions
import security.MyDeadboltHandler

class ProveedorController @Inject() (repo: ProveedorRepository, val messagesApi: MessagesApi)
                                 (implicit ec: ExecutionContext) extends Controller with I18nSupport{

  val newForm: Form[CreateProveedorForm] = Form {
    mapping(
      "nombre" -> nonEmptyText,
      "telefono" -> number,
      "direccion" -> nonEmptyText,
      "contacto" -> nonEmptyText,
      "account" -> longNumber
    )(CreateProveedorForm.apply)(CreateProveedorForm.unapply)
  }

  def index = Action.async { implicit request =>
    repo.list().map { res =>
      Ok(views.html.proveedor_index(new MyDeadboltHandler, res))
    }
  }

  def addGet = Action { implicit request =>
    Ok(views.html.proveedor_add(new MyDeadboltHandler, newForm))
  }

  def addProveedor = Action.async { implicit request =>
    newForm.bindFromRequest.fold(
      errorForm => {
        Future.successful(Ok(views.html.proveedor_add(new MyDeadboltHandler, errorForm)))
      },
      proveedor => {
        repo.create(proveedor.nombre, proveedor.telefono, proveedor.direccion, proveedor.contacto, proveedor.account).map { resNew =>
          Redirect(routes.ProveedorController.show(resNew.id))
        }
      }
    )
  }

  def getProveedores = Action.async {
    repo.list().map { proveedores =>
      Ok(Json.toJson(proveedores))
    }
  }

  // update required
  val updateForm: Form[UpdateProveedorForm] = Form {
    mapping(
      "id" -> longNumber,
      "nombre" -> nonEmptyText,
      "telefono" -> number.verifying(min(0), max(9999999)),
      "direccion" -> nonEmptyText,
      "contacto" -> text,
      "account" -> longNumber
    )(UpdateProveedorForm.apply)(UpdateProveedorForm.unapply)
  }

  // to copy
  def show(id: Long) = Action.async { implicit request =>
    repo.getById(id).map { res =>
      Ok(views.html.proveedor_show(new MyDeadboltHandler, res(0)))
    }
  }

  // to copy
  def profile(id: Long) = Action {
    Redirect(routes.ProveedorController.show(id))
  }

  var updatedRow: Proveedor = _

  // update required
  def getUpdate(id: Long) = Action.async { implicit request =>
    repo.getById(id).map { res =>
      updatedRow = res(0)
      val anyData = Map("id" -> id.toString().toString(), "nombre" -> res.toList(0).nombre, "telefono" -> res.toList(0).telefono.toString(), "direccion" -> res.toList(0).direccion, "contacto" -> res.toList(0).contacto, "account" -> res.toList(0).account.toString())
      Ok(views.html.proveedor_update(new MyDeadboltHandler, updatedRow, updateForm.bind(anyData)))
    }
  }

  // delete required
  def delete(id: Long) = Action.async { implicit request =>
    repo.delete(id).map { res =>
      Redirect(routes.ProveedorController.index)
    }
  }

  // to copy
  def getById(id: Long) = Action.async {
    repo.getById(id).map { res =>
      Ok(Json.toJson(res))
    }
  }

  // update required
  def updatePost = Action.async { implicit request =>
    updateForm.bindFromRequest.fold(
      errorForm => {
        Future.successful(Ok(views.html.proveedor_update(new MyDeadboltHandler, updatedRow, errorForm)))
      },
      res => {
        repo.update(res.id, res.nombre, res.telefono, res.direccion, res.contacto, res.account).map { _ =>
          Redirect(routes.ProveedorController.show(res.id))
        }
      }
    )
  }
}

case class CreateProveedorForm(nombre: String, telefono: Int, direccion: String, contacto: String, account: Long)

case class UpdateProveedorForm(id: Long, nombre: String, telefono: Int, direccion: String, contacto: String, account: Long)