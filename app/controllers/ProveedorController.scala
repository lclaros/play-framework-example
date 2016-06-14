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

  def index = Action {
    Ok(views.html.proveedor_index(newForm))
  }

  def addProveedor = Action.async { implicit request =>
    newForm.bindFromRequest.fold(
      errorForm => {
        Future.successful(Ok(views.html.proveedor_index(errorForm)))
      },
      proveedor => {
        repo.create(proveedor.nombre, proveedor.telefono, proveedor.direccion, proveedor.contacto, proveedor.account).map { _ =>
          Redirect(routes.ProveedorController.index)
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
  def show(id: Long) = Action {
    Ok(views.html.proveedor_show())
  }

  // to copy
  def profile(id: Long) = Action {
    Ok(views.html.proveedor_show())
  }

  // update required
  def getUpdate(id: Long) = Action.async {
    repo.getById(id).map { res =>
      val anyData = Map("id" -> id.toString().toString(), "nombre" -> res.toList(0).nombre, "telefono" -> res.toList(0).telefono.toString(), "direccion" -> res.toList(0).direccion, "contacto" -> res.toList(0).contacto, "account" -> res.toList(0).account.toString())
      Ok(views.html.proveedor_update(updateForm.bind(anyData)))
    }
  }

  // delete required
  def delete(id: Long) = Action.async {
    repo.delete(id).map { res =>
      Ok(views.html.proveedor_index(newForm))
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
        Future.successful(Ok(views.html.proveedor_update(errorForm)))
      },
      res => {
        repo.update(res.id, res.nombre, res.telefono, res.direccion, res.contacto, res.account).map { _ =>
          Redirect(routes.ProveedorController.index)
        }
      }
    )
  }
}

case class CreateProveedorForm(nombre: String, telefono: Int, direccion: String, contacto: String, account: Long)

case class UpdateProveedorForm(id: Long, nombre: String, telefono: Int, direccion: String, contacto: String, account: Long)