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

class MeasureController @Inject() (repo: MeasureRepository, val messagesApi: MessagesApi)
                                 (implicit ec: ExecutionContext) extends Controller with I18nSupport{

  val newForm: Form[CreateMeasureForm] = Form {
    mapping(
      "name" -> nonEmptyText,
      "quantity" -> number,
      "description" -> text
    )(CreateMeasureForm.apply)(CreateMeasureForm.unapply)
  }

  def addGet = Action { implicit request =>
    Ok(views.html.measure_add(new MyDeadboltHandler, newForm))
  }

  def index = Action.async { implicit request =>
    repo.list().map { res =>
      Ok(views.html.measure_index(new MyDeadboltHandler, res))
    }
  }

  def add = Action.async { implicit request =>
    newForm.bindFromRequest.fold(
      errorForm => {
        Future.successful(Ok(views.html.measure_add(new MyDeadboltHandler, errorForm)))
      },
      res => {
        repo.create(res.name, res.quantity, res.description).map { resNew =>
          Redirect(routes.MeasureController.show(resNew.id))
        }
      }
    )
  }

  def getMeasures = Action.async {
    repo.list().map { res =>
      Ok(Json.toJson(res))
    }
  }

  def getMeasuresReport = Action.async {
  	repo.list().map { res =>
      Ok(Json.toJson(res))
    }
  }

  // update required
  val updateForm: Form[UpdateMeasureForm] = Form {
    mapping(
      "id" -> longNumber,
      "name" -> nonEmptyText,
      "quantity" -> number,
      "description" -> text
    )(UpdateMeasureForm.apply)(UpdateMeasureForm.unapply)
  }

  // to copy
  def show(id: Long) = Action.async { implicit request =>
    repo.getById(id).map { res =>
      Ok(views.html.measure_show(new MyDeadboltHandler, res(0)))
    }
  }

  var updatedRow: Measure = _

  // update required
  def getUpdate(id: Long) = Action.async { implicit request =>
    repo.getById(id).map { res =>
      updatedRow = res(0)
      val anyData = Map(
                        "id" -> id.toString().toString(),
                        "name" -> updatedRow.name,
                        "quantity" -> updatedRow.quantity.toString(),
                        "description" -> updatedRow.description.toString()
                       )
      Ok(views.html.measure_update(new MyDeadboltHandler, updatedRow, updateForm.bind(anyData)))
    }
  }

  // delete required
  def delete(id: Long) = Action.async { implicit request =>
    repo.delete(id).map { res =>
      Redirect(routes.MeasureController.index)
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
        Future.successful(Ok(views.html.measure_update(new MyDeadboltHandler, updatedRow, errorForm)))
      },
      res => {
        repo.update(res.id, res.name, res.quantity, res.description).map { _ =>
          Redirect(routes.MeasureController.show(res.id))
        }
      }
    )
  }

}

case class CreateMeasureForm(name: String, quantity: Int, description: String)

// Update required
case class UpdateMeasureForm(id: Long, name: String, quantity: Int, description: String)
