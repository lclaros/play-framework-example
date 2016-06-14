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

class InsumoUserController @Inject() (repo: UserRepository, val messagesApi: MessagesApi)
                                 (implicit ec: ExecutionContext) extends Controller with I18nSupport{

  val newForm: Form[CreateInsumoUserForm] = Form {
    mapping(
      "nombre" -> nonEmptyText,
      "carnet" -> number,
      "telefono" -> number,
      "direccion" -> text,
      "sueldo" -> number,
      "type_1" -> text,
      "login" -> text,
      "password" -> text
    )(CreateInsumoUserForm.apply)(CreateInsumoUserForm.unapply)
  }

  def index = Action {
    Ok(views.html.insumoUser_index(newForm))
  }

  def add = Action.async { implicit request =>
    newForm.bindFromRequest.fold(
      errorForm => {
        Future.successful(Ok(views.html.insumoUser_index(errorForm)))
      },
      res => {
        repo.create(res.nombre, res.carnet, res.telefono, res.direccion, res.sueldo, "Insumo", res.login, res.password).map { _ =>
          Redirect(routes.InsumoUserController.index)
        }
      }
    )
  }

  def getInsumoUsers = Action.async {
  	repo.list().map { res =>
      Ok(Json.toJson(res))
    }
  }

  // update required
  val updateForm: Form[UpdateInsumoUserForm] = Form {
    mapping(
      "id" -> longNumber,
      "nombre" -> nonEmptyText,
      "carnet" -> number.verifying(min(0), max(9999999)),
      "telefono" -> number.verifying(min(0), max(9999999)),
      "direccion" -> nonEmptyText,
      "sueldo" -> number,
      "type_1" -> nonEmptyText,
      "login" -> nonEmptyText,
      "password" -> nonEmptyText
    )(UpdateInsumoUserForm.apply)(UpdateInsumoUserForm.unapply)
  }

  // to copy
  def show(id: Long) = Action {
    Ok(views.html.insumoUser_show())
  }

  // to copy
  def profile(id: Long) = Action {
    Ok(views.html.insumoUser_profile())
  }

  // update required
  def getUpdate(id: Long) = Action.async {
    repo.getById(id).map { res =>
      val anyData = Map("id" -> id.toString().toString(), "nombre" -> res.toList(0).nombre, "carnet" -> res.toList(0).carnet.toString(), "telefono" -> res.toList(0).telefono.toString(), "direccion" -> res.toList(0).direccion, "sueldo" -> res.toList(0).sueldo.toString(), "login" -> res.toList(0).login, "password" -> res.toList(0).password.toString())
      Ok(views.html.insumoUser_update(updateForm.bind(anyData)))
    }
  }

  // delete required
  def delete(id: Long) = Action.async {
    repo.delete(id).map { res =>
      Ok(views.html.insumoUser_index(newForm))
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
        Future.successful(Ok(views.html.insumoUser_update(errorForm)))
      },
      res => {
        repo.update(res.id, res.nombre, res.carnet, res.telefono, res.direccion, res.sueldo, "Insumo", res.login, res.password).map { _ =>
          Redirect(routes.InsumoUserController.index)
        }
      }
    )
  }
}

case class CreateInsumoUserForm(nombre: String, carnet: Int, telefono: Int, direccion: String, sueldo: Int, type_1: String, login: String, password: String)

case class UpdateInsumoUserForm(id: Long, nombre: String, carnet: Int, telefono: Int, direccion: String, sueldo: Int, type_1: String, login: String, password: String)