package controllers

import scala.concurrent.duration._
import play.api._
import play.api.mvc._
import play.api.i18n._
import play.api.data.Form
import play.api.data.Forms._
import play.api.data.validation.Constraints._
import play.api.libs.json.Json
import models._
import dal._

import scala.concurrent.{ ExecutionContext, Future, Await }

import javax.inject._

class UserController @Inject() (repo: UserRepository, val messagesApi: MessagesApi)
                                 (implicit ec: ExecutionContext) extends Controller with I18nSupport {

  val newForm: Form[CreateUserForm] = Form {
    mapping(
      "nombre" -> nonEmptyText,
      "carnet" -> number,
      "telefono" -> number,
      "direccion" -> text,
      "sueldo" -> number,
      "type_1" -> text,
      "login" -> text,
      "password" -> text
    )(CreateUserForm.apply)(CreateUserForm.unapply)
  }

  val loginForm: Form[LoginForm] = Form {
    mapping(
      "user" -> nonEmptyText,
      "password" -> nonEmptyText
    )(LoginForm.apply)(LoginForm.unapply)
  }

  val types = scala.collection.immutable.Map[String, String]("Veterinario" -> "Veterinario", "Insumo" -> "Insumo", "Admin" -> "Admin", "Almacen" -> "Almacen")

  def index = Action {
    Ok(views.html.user_index(newForm, types))
  }

  def profile() = Action { implicit request =>
    Await.result(repo.getById(request.session.get("userId").getOrElse("0").toLong).map { res2 =>
        if (res2.length > 0) {
          if (res2(0).type_1.toLowerCase == "admin") {
            Redirect("/")
          } else if (res2(0).type_1.toLowerCase == "veterinario") {
            Redirect(routes.VeterinarioController.profile(res2(0).id))
          } else if (res2(0).type_1.toLowerCase == "insumo") {
            Redirect(routes.InsumoUserController.profile(res2(0).id))
          } else if (res2(0).type_1.toLowerCase == "almacen") {
            Redirect(routes.StorekeeperController.profile(res2(0).id))
          } else {
            Ok(views.html.storekeeper_profile2(res2(0)))
            Redirect("/error")
          }
        } else {
          Redirect("/login")
        }
      }, 2000.millis)
  }

  def profileById(userId: Long) = Action { implicit request =>
    Await.result(repo.getById(userId).map { res2 =>
        if (res2.length > 0) {
          if (res2(0).type_1.toLowerCase == "admin") {
            Redirect("/")
          } else if (res2(0).type_1.toLowerCase == "veterinario") {
            Redirect(routes.VeterinarioController.profile(res2(0).id))
          } else if (res2(0).type_1.toLowerCase == "insumo") {
            Redirect(routes.InsumoUserController.profile(res2(0).id))
          } else if (res2(0).type_1.toLowerCase == "almacen") {
            Redirect(routes.StorekeeperController.profile(res2(0).id))
          } else {
            Ok(views.html.storekeeper_profile2(res2(0)))
            Redirect("/error")
          }
        } else {
          Redirect("/login")
        }
      }, 2000.millis)
  }



  def add = Action.async { implicit request =>
    newForm.bindFromRequest.fold(
      errorForm => {
        Future.successful(Ok(views.html.user_index(errorForm, types)))
      },
      res => {
        repo.create(res.nombre, res.carnet, res.telefono, res.direccion, res.sueldo, res.type_1, res.login, res.password).map { _ =>
          Redirect(routes.UserController.index)
        }
      }
    )
  }

  def getUsers = Action.async {
  	repo.list().map { res =>
      Ok(Json.toJson(res))
    }
  }

  // Update required
  val updateForm: Form[UpdateUserForm] = Form {
    mapping(
      "id" -> longNumber,
      "nombre" -> nonEmptyText,
      "carnet" -> number.verifying(min(0), max(9999999)),
      "telefono" -> number.verifying(min(0), max(9999999)),
      "direccion" -> nonEmptyText,
      "sueldo" -> number,
      "type_1" -> text,
      "login" -> text,
      "password" -> text
    )(UpdateUserForm.apply)(UpdateUserForm.unapply)
  }

  // to copy
  def show(id: Long) = Action {
    Ok(views.html.user_show())
  }

  // update required
  def getUpdate(id: Long) = Action.async {
    repo.getById(id).map { res =>
      val anyData = Map("id" -> id.toString().toString(), "nombre" -> res.toList(0).nombre, "carnet" -> res.toList(0).carnet.toString(),
                        "telefono" -> res.toList(0).telefono.toString(), "direccion" -> res.toList(0).direccion,
                        "sueldo" -> res.toList(0).sueldo.toString(), "type_1" -> res.toList(0).type_1.toString(),
                        "login" -> res.toList(0).login.toString(), "password" -> res.toList(0).password.toString())
      Ok(views.html.user_update(updateForm.bind(anyData), types))
    }
  }

  // delete required
  def delete(id: Long) = Action.async {
    repo.delete(id).map { res =>
      Ok(views.html.user_index(newForm, types))
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
        Future.successful(Ok(views.html.user_update(errorForm, types)))
      },
      res => {
        repo.update(res.id, res.nombre, res.carnet, res.telefono, res.direccion, res.sueldo, res.type_1, res.login, res.password).map { _ =>
          Redirect(routes.UserController.index)
        }
      }
    )
  }
}

case class CreateUserForm(nombre: String, carnet: Int, telefono: Int, direccion: String, sueldo: Int, type_1: String, login: String, password: String)

case class UpdateUserForm(id: Long, nombre: String, carnet: Int, telefono: Int, direccion: String, sueldo: Int, type_1: String, login: String, password: String)