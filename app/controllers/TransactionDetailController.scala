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
import play.api.data.format.Formats._ 


import javax.inject._

class TransactionDetailController @Inject() (repo: TransactionDetailRepository, repoTransReport: TransactionRepository,
                                             repoAccounts: AccountRepository, val messagesApi: MessagesApi)
                                  (implicit ec: ExecutionContext) extends Controller with I18nSupport{

  val newForm: Form[CreateTransactionDetailForm] = Form {
    mapping(
      "transactionId" -> longNumber,
      "accountId" -> longNumber,
      "debit" -> of[Double],
      "credit" -> of[Double]
    )(CreateTransactionDetailForm.apply)(CreateTransactionDetailForm.unapply)
  }

  val unidades = scala.collection.immutable.Map[String, String]("1" -> "Unidad 1", "2" -> "Unidad 2")

  def index = Action {
    val transactionsNames = getTransactionsMap()
    val productorsNames = getAccountsMap()
    Ok(views.html.transactionDetail_index(newForm, transactionsNames, productorsNames))
  }

  def add = Action.async { implicit request =>
    newForm.bindFromRequest.fold(
      errorForm => {
        Future.successful(Ok(views.html.transactionDetail_index(errorForm, Map[String, String](), Map[String, String]())))
      },
      res => {
        repo.create(res.transactionId, res.accountId, res.debit, res.credit).map { value =>
          repoAccounts.updateParentDebitCredit(res.accountId, res.debit, res.credit);
          repoTransReport.getById(res.transactionId).map{ res => repo.updateTransactionParams(value.id, res(0).date)}
          repoAccounts.getById(res.accountId).map{ res => repo.updateAccountParams(value.id, res(0).code, res(0).name)}
          
          Redirect(routes.TransactionController.show(res.transactionId))
        }
      }
    )
  }

  def addGet = Action {
    val transactionsNames = getTransactionsMap()
    val productorsNames = getAccountsMap()
    Ok(views.html.transactionDetail_add(newForm, transactionsNames, productorsNames))
  }

  def getTransactionDetails = Action.async {
    repo.list().map { res =>
      Ok(Json.toJson(res))
    }
  }

  def getTransactionDetailsByTransaction(id: Long) = Action.async {
    repo.listByTransaction(id).map { res =>
      Ok(Json.toJson(res))
    }
  }

  def getTransactionDetailsByAccount(id: Long) = Action.async {
    repo.listByAccount(id).map { res =>
      Ok(Json.toJson(res))
    }
  }

  // update required
  val updateForm: Form[UpdateTransactionDetailForm] = Form {
    mapping(
      "id" -> longNumber,
      "transactionId" -> longNumber,
      "accountId" -> longNumber,
      "debit" -> of[Double],
      "credit" -> of[Double]
    )(UpdateTransactionDetailForm.apply)(UpdateTransactionDetailForm.unapply)
  }

  // to copy
  def show(id: Long) = Action {
    Ok(views.html.transactionDetail_show())
  }

  // update required
  def getUpdate(id: Long) = Action.async {
    repo.getById(id).map {case (res) =>
      val anyData = Map("id" -> id.toString().toString(), "transactionId" -> res.toList(0).transactionId.toString(), "accountId" -> res.toList(0).accountId.toString(), "debit" -> res.toList(0).debit.toString(), "credit" -> res.toList(0).credit.toString())
      val discountRepMap = getTransactionsMap()
      val proveeMap = getAccountsMap()
      Ok(views.html.transactionDetail_update(updateForm.bind(anyData), discountRepMap, proveeMap))
    }
  }

  def getTransactionsMap(): Map[String, String] = {
    Await.result(repoTransReport.getListNames().map{ case (res1) => 
      val cache = collection.mutable.Map[String, String]()
      res1.foreach{ case (key: Long, value: String) => 
        cache put (key.toString(), value)
      }
      println(cache)
      cache.toMap
    }, 3000.millis)
  }

  def getAccountsMap(): Map[String, String] = {
    val cache = collection.mutable.Map[String, String]()
    Await.result(repoAccounts.getListObjsChild().map{ case (res1) => 
      res1.foreach{ res2 => 
        cache put (res2.id.toString(), res2.code + " " + res2.name)
      }
    }, 1000.millis)
    cache.toMap
  }


  // delete required
  def delete(id: Long) = Action.async {
    repo.delete(id).map { res =>
      println(res);
      //repoTransReport.updatediscount(res.transactionId, );
      Ok(views.html.transactionDetail_index(newForm, Map[String, String](), Map[String, String]()))
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
        Future.successful(Ok(views.html.transactionDetail_update(errorForm, Map[String, String](), Map[String, String]())))
      },
      res => {
        repo.update(res.id, res.transactionId, res.accountId, res.debit, res.credit).map { _ =>
          Redirect(routes.TransactionController.show(res.transactionId))
        }
      }
    )
  }
}

case class CreateTransactionDetailForm(transactionId: Long, accountId: Long, debit: Double, credit: Double)

case class UpdateTransactionDetailForm(id: Long, transactionId: Long, accountId: Long, debit: Double, credit: Double)