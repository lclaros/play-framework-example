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
import scala.collection.mutable.ListBuffer
import java.util.LinkedHashMap
import collection.mutable
import scala.collection.mutable.ArrayBuffer

import javax.inject._

class TransactionController @Inject() (repo: TransactionRepository, repoVete: UserRepository, repoSto: UserRepository, val messagesApi: MessagesApi)
                                 (implicit ec: ExecutionContext) extends Controller with I18nSupport{

  val newForm: Form[CreateTransactionForm] = Form {
    mapping(
      "date" -> text,
      "type_1" -> text,
      "description" -> text,
      "receivedBy" -> longNumber,
      "autorizedBy" -> longNumber
    )(CreateTransactionForm.apply)(CreateTransactionForm.unapply)
  }

  var users = getUsersMap()
  def getUsersMap(): Map[String, String] = {
    Await.result(repoVete.getListNames().map{ case (res1) => 
      val cache = collection.mutable.Map[String, String]()
      res1.foreach{ case (key: Long, value: String) => 
        cache put (key.toString(), value)
      }
      println(cache)
      cache.toMap
    }, 3000.millis)
  }

  def index = Action {
    Ok(views.html.transaction_index())
  }

  def addGet = Action {
    users = getUsersMap()
    Ok(views.html.transaction_add(newForm, users))
  }
  
  def add = Action.async { implicit request =>
    newForm.bindFromRequest.fold(
      errorForm => {
        Future.successful(Ok(views.html.transaction_index()))
      },
      res => {
        var userId = request.session.get("userId").getOrElse("0").toLong
        var userName = request.session.get("userName").getOrElse("0").toString
        println(userId)
        println(userName)
        repo.create(
                    res.date, res.type_1, res.description, 
                    userId, userName,
                    res.receivedBy, users(res.receivedBy.toString),
                    res.autorizedBy, users(res.autorizedBy.toString)
                    ).map { _ =>
          Ok(views.html.transaction_index())
        }
      }
    )
  }

  def getTransactions = Action.async {
    repo.list().map { res =>
      Ok(Json.toJson(res))
    }
  }

  // update required
  val updateForm: Form[UpdateTransactionForm] = Form {
    mapping(
      "id" -> longNumber,
      "date" -> text,
      "type_1" -> text,
      "description" -> text,
      "receivedBy" -> longNumber,
      "autorizedBy" -> longNumber
    )(UpdateTransactionForm.apply)(UpdateTransactionForm.unapply)
  }

  // to copy
  def show(id: Long) = Action {
    Ok(views.html.transaction_show())
  }

  // update required
  def getUpdate(id: Long) = Action.async {
    users = getUsersMap()
    repo.getById(id).map {case (res) =>
      val anyData = Map(
                        "id" -> id.toString().toString(),
                        "date" -> res.toList(0).date.toString(),
                        "type_1" -> res.toList(0).type_1.toString(),
                        "description" -> res.toList(0).description.toString(),
                        "receivedBy" -> res.toList(0).receivedBy.toString(),
                        "autorizedBy" -> res.toList(0).autorizedBy.toString()
                        )
      Ok(views.html.transaction_update(updateForm.bind(anyData), users))
    }
  }

  // delete required
  def delete(id: Long) = Action.async {
    repo.delete(id).map { res =>
      Ok(views.html.transaction_index())
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
        Future.successful(Ok(views.html.transaction_update(errorForm, users)))
      },
      res => {
        repo.update(
                    res.id, res.date, res.type_1,
                    res.description, res.receivedBy, 
                    users(res.receivedBy.toString),
                    res.autorizedBy, users(res.autorizedBy.toString)
                    ).map { _ =>
          Redirect(routes.TransactionController.index())
        }
      }
    )
  }
}

case class CreateTransactionForm(date: String, type_1: String, description: String, receivedBy: Long, autorizedBy: Long)

case class UpdateTransactionForm(id: Long, date: String, type_1: String, description: String, receivedBy: Long, autorizedBy: Long)