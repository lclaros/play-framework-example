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
import it.innove.play.pdf.PdfGenerator
import be.objectify.deadbolt.scala.DeadboltActions
import security.MyDeadboltHandler

class ModuleController @Inject() (repo: ModuleRepository, repoProductor: ProductorRepository, val messagesApi: MessagesApi)
                                 (implicit ec: ExecutionContext) extends Controller with I18nSupport{


  val newForm: Form[CreateModuleForm] = Form {
    mapping(
      "name" -> nonEmptyText,
      "president" -> longNumber,
      "description" -> text,
      "association" -> longNumber
    )(CreateModuleForm.apply)(CreateModuleForm.unapply)
  }

  def index = Action.async { implicit request =>
    repo.list().map { res =>
      Ok(views.html.module_index(new MyDeadboltHandler, res))
    }
  }

  var associationes:Map[String, String] = _

  def getAssociations(): Map[String, String] = {
    Await.result(repoProductor.getListNamesAssociations().map{ res => 
      val cache = collection.mutable.Map[String, String]()
      res.foreach { asociation => 
        cache put (asociation.id.toString, asociation.name)
      }
      cache.toMap
    }, 3000.millis)
  }

  def addGet = Action { implicit request =>
    associationes = getAssociations()
    Ok(views.html.module_add(new MyDeadboltHandler, newForm, associationes))
  }

  def index_pdf = Action {
    val generator = new PdfGenerator
    Ok(generator.toBytes(views.html.reporte_productores(), "http://localhost:9000/")).as("application/pdf")
  }

  def add = Action.async { implicit request =>
    newForm.bindFromRequest.fold(
      errorForm => {
        Future.successful(Ok(views.html.module_add(new MyDeadboltHandler, errorForm, associationes)))
      },
      res => {
        repo.create(res.name, res.president, res.description, res.association, associationes(res.association.toString)).map { resNew =>
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
      "association" -> longNumber
    )(UpdateModuleForm.apply)(UpdateModuleForm.unapply)
  }

  // to copy
  def show(id: Long) = Action.async { implicit request =>
    repo.getById(id).map{ res =>
      Ok(views.html.module_show(new MyDeadboltHandler, res(0)))
    }
  }

  var updatedRow: Module = _

  // update required
  def getUpdate(id: Long) = Action.async { implicit request =>
    repo.getById(id).map { res =>
      updatedRow = res(0)
      val anyData = Map("id" -> id.toString().toString(), "name" -> updatedRow.name,
                        "president" -> updatedRow.president.toString(),
                        "description" -> updatedRow.description.toString(),
                        "association" -> updatedRow.association.toString(),
                        "associationName" -> updatedRow.associationName.toString()
                       )
      Ok(views.html.module_update(new MyDeadboltHandler, updatedRow, updateForm.bind(anyData), associationes))
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
        Future.successful(Ok(views.html.module_update(new MyDeadboltHandler, updatedRow, errorForm, associationes)))
      },
      res => {
        repo.update(res.id, res.name, res.president, res.description, res.association, associationes(res.association.toString)).map { _ =>
          Redirect(routes.ModuleController.show(res.id))
        }
      }
    )
  }
}

case class CreateModuleForm(name: String, president: Long, description: String, association: Long)

// Update required
case class UpdateModuleForm(id: Long, name: String, president: Long, description: String, association: Long)
