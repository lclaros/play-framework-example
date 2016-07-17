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

class CompanyController @Inject() (repo: CompanyRepository, val messagesApi: MessagesApi)
                                 (implicit ec: ExecutionContext) extends Controller with I18nSupport{

  val associationes = scala.collection.immutable.Map[String, String]("0" -> "Ninguno", "1" -> "Association 1", "2" -> "Association 2")

  // update required
  val updateForm: Form[UpdateCompanyForm] = Form {
    mapping(
      "id" -> longNumber,
      "name" -> text,
      "president" -> text,
      "description" -> text
    )(UpdateCompanyForm.apply)(UpdateCompanyForm.unapply)
  }

  var updatedRow: Company = _
  // to copy
  def show() = Action.async { implicit request =>
    repo.getFirst().map { res =>
      if (res.size > 0) {
        updatedRow = res(0)
      } else {
        updatedRow = Company(0, "Nombre de Empresa", "Nombre Presidente", "Descripcion de la Empresa")
      }
      Ok(views.html.company_show(new MyDeadboltHandler, updatedRow))
    }
  }

  // update required
  def getUpdate = Action.async { implicit request =>
    repo.getFirst().map { res =>
      var anyData: Map[String, String] = Map[String, String]()
      if (res.size == 0) {
         anyData = Map(
                          "id" -> "0",
                          "name" -> "Nombre Empresa",
                          "president" -> "Nombre Presidente",
                          "description" -> "Descripcion de la empresa"
                         )
      } else {
        updatedRow = res(0)
        anyData = Map(
                          "id" -> updatedRow.id.toString,
                          "name" -> updatedRow.name.toString,
                          "president" -> updatedRow.president.toString,
                          "description" -> updatedRow.description.toString
                         )
      }
      Ok(views.html.company_update(new MyDeadboltHandler, updatedRow, updateForm.bind(anyData)))
    }
  }

  // update required
  def updatePost = Action.async { implicit request =>
    updateForm.bindFromRequest.fold(
      errorForm => {
        Future.successful(Ok(views.html.company_update(new MyDeadboltHandler, updatedRow, errorForm)))
      },
      res => {
        if (res.id > 0) {
          repo.update(res.id, res.name, res.president, res.description).map { _ =>
            Redirect(routes.CompanyController.show())
          }
        } else {
          repo.create(res.name, res.president, res.description).map { _ =>
            Redirect(routes.CompanyController.show())
          }
        }
      }
    )
  }
}

// Update required
case class UpdateCompanyForm(id: Long, name: String, president: String, description: String)
