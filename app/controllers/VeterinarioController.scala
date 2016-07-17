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

class VeterinarioController @Inject() (repo: UserRepository, val messagesApi: MessagesApi)
                                 (implicit ec: ExecutionContext) extends Controller with I18nSupport{

  val newForm: Form[CreateVeterinarioForm] = Form {
    mapping(
      "name" -> nonEmptyText,
      "carnet" -> number,
      "telefono" -> number,
      "direccion" -> text,
      "sueldo" -> number,
      "type_1" -> nonEmptyText,
      "login" -> text,
      "password" -> text
    )(CreateVeterinarioForm.apply)(CreateVeterinarioForm.unapply)
  }

  def index = Action {
    Ok(views.html.veterinario_index(newForm))
  }

  def add = Action.async { implicit request =>
    newForm.bindFromRequest.fold(
      errorForm => {
        Future.successful(Ok(views.html.veterinario_index(errorForm)))
      },
      veterinario => {
        repo.create(veterinario.name, veterinario.carnet, veterinario.telefono, veterinario.direccion, veterinario.sueldo, "veterinario", veterinario.login, veterinario.password).map { _ =>
          Redirect(routes.VeterinarioController.index)
        }
      }
    )
  }

  def getVeterinarios = Action.async {
  	repo.listVeterinarios().map { res =>
      Ok(Json.toJson(res))
    }
  }

  val updateForm: Form[UpdateVeterinarioForm] = Form {
    mapping(
      "id" -> longNumber,
      "name" -> nonEmptyText,
      "carnet" -> number.verifying(min(0), max(9999999)),
      "telefono" -> number.verifying(min(0), max(9999999)),
      "direccion" -> nonEmptyText,
      "sueldo" -> number,
      "type_1" -> nonEmptyText,
      "login" -> nonEmptyText,
      "password" -> nonEmptyText
    )(UpdateVeterinarioForm.apply)(UpdateVeterinarioForm.unapply)
  }

  // to copy
  def show(id: Long) = Action {
    Ok(views.html.veterinario_show())
  }

  // to copy
  def profile(id: Long) = Action {
    Ok(views.html.veterinario_profile())
  }

  // update required
  def getUpdate(id: Long) = Action.async {
    repo.getById(id).map { res =>
      val anyData = Map("id" -> id.toString().toString(), "name" -> res.toList(0).name, "carnet" -> res.toList(0).carnet.toString(), "telefono" -> res.toList(0).telefono.toString(), "direccion" -> res.toList(0).direccion, "sueldo" -> res.toList(0).sueldo.toString(), "type_1" -> res.toList(0).type_1.toString(), "login" -> res.toList(0).login.toString(), "password" -> res.toList(0).password.toString())
      Ok(views.html.veterinario_update(updateForm.bind(anyData)))
    }
  }

  // delete required
  def delete(id: Long) = Action.async {
    repo.delete(id).map { res =>
      Ok(views.html.veterinario_index(newForm))
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
        Future.successful(Ok(views.html.veterinario_update(errorForm)))
      },
      res => {
        repo.update(res.id, res.name, res.carnet, res.telefono, res.direccion, res.sueldo, "veterinario", res.login, res.password).map { _ =>
          Redirect(routes.VeterinarioController.index)
        }
      }
    )
  }
}

case class CreateVeterinarioForm(name: String, carnet: Int, telefono: Int, direccion: String, sueldo: Int, type_1: String, login: String, password: String)

case class UpdateVeterinarioForm(id: Long, name: String, carnet: Int, telefono: Int, direccion: String, sueldo: Int, type_1: String, login: String, password: String)