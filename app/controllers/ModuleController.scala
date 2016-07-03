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
import it.innove.play.pdf.PdfGenerator
import be.objectify.deadbolt.scala.DeadboltActions
import security.MyDeadboltHandler

class ModuleController @Inject() (repo: ModuleRepository, val messagesApi: MessagesApi)
                                 (implicit ec: ExecutionContext) extends Controller with I18nSupport{

  val asociaciones = scala.collection.immutable.Map[String, String]("0" -> "Ninguno", "1" -> "Asociacion 1", "2" -> "Asociacion 2")

  val newForm: Form[CreateModuleForm] = Form {
    mapping(
      "name" -> nonEmptyText,
      "president" -> longNumber,
      "description" -> text,
      "asociacion" -> longNumber
    )(CreateModuleForm.apply)(CreateModuleForm.unapply)
  }

  def index = Action.async { implicit request =>
    repo.list().map { res =>
      Ok(views.html.module_index(new MyDeadboltHandler, res))
    }
  }

  def addGet = Action { implicit request =>
    Ok(views.html.module_add(new MyDeadboltHandler, newForm, asociaciones))
  }

  def index_pdf = Action {
    val generator = new PdfGenerator
    Ok(generator.toBytes(views.html.reporte_productores(), "http://localhost:9000/")).as("application/pdf")
  }

  def add = Action.async { implicit request =>
    newForm.bindFromRequest.fold(
      errorForm => {
        Future.successful(Ok(views.html.module_add(new MyDeadboltHandler, errorForm, asociaciones)))
      },
      res => {
        repo.create(res.name, res.president, res.description, res.asociacion, asociaciones(res.asociacion.toString)).map { resNew =>
          Redirect(routes.ModuleController.show(resNew.id))
        }
      }
    )
  }

  def getModules = Action.async {
    repo.list().map { res =>
      Ok(Json.toJson(res))
    }
  }

  def getModulesReport = Action.async {
  	repo.list().map { res =>
      Ok(Json.toJson(res))
    }
  }

  // update required
  val updateForm: Form[UpdateModuleForm] = Form {
    mapping(
      "id" -> longNumber,
      "name" -> nonEmptyText,
      "president" -> longNumber,
      "description" -> text,
      "asociacion" -> longNumber
    )(UpdateModuleForm.apply)(UpdateModuleForm.unapply)
  }

  // to copy
  def show(id: Long) = Action.async { implicit request =>
    repo.getById(id).map{ res =>
      Ok(views.html.module_show(new MyDeadboltHandler, res(0)))
    }
  }

  // update required
  def getUpdate(id: Long) = Action.async {
    repo.getById(id).map { res =>
      val anyData = Map("id" -> id.toString().toString(), "name" -> res.toList(0).name,
                        "president" -> res.toList(0).president.toString(),
                        "description" -> res.toList(0).description.toString(),
                        "asociacion" -> res.toList(0).asociacion.toString(),
                        "asociacionName" -> res.toList(0).asociacionName.toString()
                       )
      Ok(views.html.module_update(updateForm.bind(anyData), asociaciones))
    }
  }

  // delete required
  def delete(id: Long) = Action.async {
    repo.delete(id).map { res =>
      Redirect(routes.ModuleController.index)
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
        Future.successful(Ok(views.html.module_update(errorForm, asociaciones)))
      },
      res => {
        repo.update(res.id, res.name, res.president, res.description, res.asociacion, asociaciones(res.asociacion.toString)).map { _ =>
          Redirect(routes.ModuleController.show(res.id))
        }
      }
    )
  }

}

case class CreateModuleForm(name: String, president: Long, description: String, asociacion: Long)

// Update required
case class UpdateModuleForm(id: Long, name: String, president: Long, description: String, asociacion: Long)
