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
import be.objectify.deadbolt.scala.DeadboltActions
import security.MyDeadboltHandler

class TransactionDetailController @Inject() (repo: TransactionDetailRepository, repoTransReport: TransactionRepository,
                                             repoAccounts: AccountRepository, val messagesApi: MessagesApi)
                                  (implicit ec: ExecutionContext) extends Controller with I18nSupport{

  var transactionId: Long = 0
  val newForm: Form[CreateTransactionDetailForm] = Form {
    mapping(
      "transactionId" -> longNumber,
      "accountId" -> longNumber,
      "debit" -> of[Double],
      "credit" -> of[Double]
    )(CreateTransactionDetailForm.apply)(CreateTransactionDetailForm.unapply)
  }

  def index = Action {
    Ok(views.html.transactionDetail_index())
  }

  def add = Action.async { implicit request =>
    newForm.bindFromRequest.fold(
      errorForm => {
        Future.successful(Ok(views.html.transactionDetail_add(errorForm, Map[String, String](), Map[String, String]())))
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

  def addGet(transactionId: Long) = Action {
    val transactionsNames = getTransactionsMap(transactionId)
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
  def show(id: Long) = Action.async { implicit request =>
    repo.getById(id).map { res =>
      Ok(views.html.transactionDetail_show(new MyDeadboltHandler, res(0)))
    }
  }

  // update required
  def getUpdate(id: Long) = Action.async {
    repo.getById(id).map {case (res) =>
      transactionId = res(0).transactionId
      val anyData = Map("id" -> id.toString().toString(), "transactionId" -> res.toList(0).transactionId.toString(), "accountId" -> res.toList(0).accountId.toString(), "debit" -> res.toList(0).debit.toString(), "credit" -> res.toList(0).credit.toString())
      val discountRepMap = getTransactionsMap(res.toList(0).transactionId)
      val proveeMap = getAccountsMap()
      Ok(views.html.transactionDetail_update(transactionId, updateForm.bind(anyData), discountRepMap, proveeMap))
    }
  }

  def getTransactionsMap(id: Long): Map[String, String] = {
    Await.result(repoTransReport.getListNamesById(id).map{ case (res1) => 
      val cache = collection.mutable.Map[String, String]()
      res1.foreach{ case (key: Long, value: String) => 
        cache put (key.toString(), value)
      }
      
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

  def getTransactionIdFromDetail(id: Long): Long = {
    Await.result(repo.getById(id).map {res => res(0).transactionId }, 1000.millis)
  }

  // delete required
  def delete(id: Long) = Action.async {
    transactionId = getTransactionIdFromDetail(id)
    repo.delete(id).map { res =>
      ;
      Redirect(routes.TransactionController.show(transactionId))
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
        Future.successful(Ok(views.html.transactionDetail_update(transactionId, errorForm, Map[String, String](), Map[String, String]())))
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