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

class AccountController @Inject() (repo: AccountRepository, val messagesApi: MessagesApi)
                                 (implicit ec: ExecutionContext) extends Controller with I18nSupport{

  val yes_no = scala.collection.immutable.Map[String, String]("NO" -> "NO", "SI" -> "SI")
  val account_type = scala.collection.immutable.Map[String, String]("ACTIVO" -> "ACTIVO", "PASIVO" -> "PASIVO", "PATRIMONIO" -> "PATRIMONIO", "EGRESO" -> "EGRESO", "INGRESO" -> "INGRESO")

  val newForm: Form[CreateAccountForm] = Form {
    mapping(
      "code" -> nonEmptyText,
      "name" -> nonEmptyText,
      "type_1" -> nonEmptyText,
      "negativo" -> nonEmptyText,
      "parent" -> longNumber,
      "description" -> text
    )(CreateAccountForm.apply)(CreateAccountForm.unapply)
  }

  def index = Action.async { implicit request =>
    repo.list().map { res =>
      Ok(views.html.account_index(new MyDeadboltHandler, res))
    }
  }

  def addGet = Action { implicit request =>
    Ok(views.html.account_add(new MyDeadboltHandler, newForm, yes_no, account_type, getAccountNamesMap()))
  }

  def add = Action.async { implicit request =>
    newForm.bindFromRequest.fold(
      errorForm => {
        Future.successful(Ok(views.html.account_add(new MyDeadboltHandler, errorForm, yes_no, account_type, getAccountNamesMap())))
      },
      res => {
        repo.create(res.code, res.name, res.type_1, res.negativo, res.parent, res.description).map { resNew =>
          Redirect(routes.AccountController.show(resNew.id))
        }
      }
    )
  }

  def getAccounts = Action.async {
    repo.list().map { res =>
      Ok(Json.toJson(res))
    }
  }

  def getAccountsReport = Action.async {
  	repo.list().map { res =>
      Ok(Json.toJson(res))
    }
  }

  // update required
  val updateForm: Form[UpdateAccountForm] = Form {
    mapping(
      "id" -> longNumber,
      "code" -> nonEmptyText,
      "name" -> nonEmptyText,
      "type_1" -> nonEmptyText,
      "negativo" -> nonEmptyText,
      "parent" -> longNumber,
      "description" -> text
    )(UpdateAccountForm.apply)(UpdateAccountForm.unapply)
  }

  // to copy
  def show(id: Long) = Action.async { implicit request =>
    repo.getById(id).map { res =>
      Ok(views.html.account_show(new MyDeadboltHandler, res(0)))
    }
  }

  // update required
  def getUpdate(id: Long) = Action.async {
    repo.getById(id).map { res =>
      val anyData = Map("id" -> id.toString().toString(), "code" -> res.toList(0).code, "name" -> res.toList(0).name.toString(), "negativo" -> res.toList(0).negativo.toString(), "parent" -> res.toList(0).parent.toString(), "type_1" -> res.toList(0).type_1.toString(), "negativo" -> res.toList(0).negativo.toString(), "parent" -> res.toList(0).parent.toString(), "description" -> res.toList(0).description)
      Ok(views.html.account_update(updateForm.bind(anyData), yes_no, account_type, getAccountNamesMap()))
    }
  }

  // delete required
  def delete(id: Long) = Action.async {
    repo.delete(id).map { res =>
      Redirect(routes.AccountController.index)
    }
  }

  // to copy
  def getById(id: Long) = Action.async {
    repo.getById(id).map { res =>
      Ok(Json.toJson(res))
    }
  }

  // to copy
  def accountChildren(id: Long) = Action.async {
    repo.getByParent(id).map { res =>
      Ok(Json.toJson(res))
    }
  }

  // update required
  def updatePost = Action.async { implicit request =>
    updateForm.bindFromRequest.fold(
      errorForm => {
        Future.successful(Ok(views.html.account_update(errorForm, yes_no, account_type, getAccountNamesMap())))
      },
      res => {
        repo.update(res.id, res.code, res.name, res.type_1, res.negativo, res.parent, res.description).map { _ =>
          Redirect(routes.AccountController.show(res.id))
        }
      }
    )
  }

  def getAccountNamesMap(): Map[String, String] = {
    val cache = collection.mutable.Map[String, String]()
    cache put ("0", "--- Ninguno ---")
    Await.result(
      repo.getListObjs().map { accountResult => 
      accountResult.foreach {
        account => cache put (account.id.toString, account.code + " ------------- " + account.name)
      }
    }, 1000.millis)
    cache.toMap
  }
  
}

case class CreateAccountForm(code: String, name: String, type_1: String, negativo: String, parent: Long, description: String)

// Update required
case class UpdateAccountForm(id: Long, code: String, name: String, type_1: String, negativo: String, parent: Long, description: String)
