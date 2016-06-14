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

class UnitMeasureController @Inject() (repo: UnitMeasureRepository, val messagesApi: MessagesApi)
                                 (implicit ec: ExecutionContext) extends Controller with I18nSupport{

  val newForm: Form[CreateUnitMeasureForm] = Form {
    mapping(
      "name" -> nonEmptyText,
      "quantity" -> number,
      "description" -> text
    )(CreateUnitMeasureForm.apply)(CreateUnitMeasureForm.unapply)
  }

  def index = Action {
    Ok(views.html.unitMeasure_index(newForm))
  }

  def add = Action.async { implicit request =>
    newForm.bindFromRequest.fold(
      errorForm => {
        Future.successful(Ok(views.html.unitMeasure_index(errorForm)))
      },
      res => {
        repo.create(res.name, res.quantity, res.description).map { _ =>
          Redirect(routes.UnitMeasureController.index)
        }
      }
    )
  }

  def getUnitMeasures = Action.async {
    repo.list().map { res =>
      Ok(Json.toJson(res))
    }
  }

  def getUnitMeasuresReport = Action.async {
  	repo.list().map { res =>
      Ok(Json.toJson(res))
    }
  }

  // update required
  val updateForm: Form[UpdateUnitMeasureForm] = Form {
    mapping(
      "id" -> longNumber,
      "name" -> nonEmptyText,
      "quantity" -> number,
      "description" -> text
    )(UpdateUnitMeasureForm.apply)(UpdateUnitMeasureForm.unapply)
  }

  // to copy
  def show(id: Long) = Action {
    Ok(views.html.unitMeasure_show())
  }

  // update required
  def getUpdate(id: Long) = Action.async {
    repo.getById(id).map { res =>
      val anyData = Map(
                        "id" -> id.toString().toString(),
                        "name" -> res.toList(0).name,
                        "quantity" -> res.toList(0).quantity.toString(),
                        "description" -> res.toList(0).description.toString()
                       )
      Ok(views.html.unitMeasure_update(updateForm.bind(anyData)))
    }
  }

  // delete required
  def delete(id: Long) = Action.async {
    repo.delete(id).map { res =>
      Ok(views.html.unitMeasure_index(newForm))
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
        Future.successful(Ok(views.html.unitMeasure_update(errorForm)))
      },
      res => {
        repo.update(res.id, res.name, res.quantity, res.description).map { _ =>
          Redirect(routes.UnitMeasureController.index)
        }
      }
    )
  }

}

case class CreateUnitMeasureForm(name: String, quantity: Int, description: String)

// Update required
case class UpdateUnitMeasureForm(id: Long, name: String, quantity: Int, description: String)
