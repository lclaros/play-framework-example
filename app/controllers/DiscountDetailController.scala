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

class DiscountDetailController @Inject() (repo: DiscountDetailRepository, repoDiscReport: DiscountReportRepository,
                                          repoProductors: ProductorRepository, val messagesApi: MessagesApi)
                                 (implicit ec: ExecutionContext) extends Controller with I18nSupport{

  val newForm: Form[CreateDiscountDetailForm] = Form {
    mapping(
      "discountReport" -> longNumber,
      "productorId" -> longNumber,
      "status" -> text,
      "discount" -> of[Double]
    )(CreateDiscountDetailForm.apply)(CreateDiscountDetailForm.unapply)
  }

  val setProductorForm: Form[SetProductorForm] = Form {
    mapping(
      "account" -> text
    )(SetProductorForm.apply)(SetProductorForm.unapply)
  }

  val setProductorNameForm: Form[SetProductorNameForm] = Form {
    mapping(
      "name" -> text
    )(SetProductorNameForm.apply)(SetProductorNameForm.unapply)
  }


  var discountsNames = getParentList(0)
  var productorsNames = getProductorsNamesMap()

  def index = Action {
    Ok(views.html.discountDetail_index())
  }

  def add = Action.async { implicit request =>
    newForm.bindFromRequest.fold(
      errorForm => {
        Future.successful(Ok(views.html.discountDetail_add(new MyDeadboltHandler, discountId, newForm, discountsNames, productorsNames)))
      },
      res => {
        var productName = productorsNames(res.productorId.toString)
        if (productName.length > 20)
          productName = productName.substring(0, 20)
        repo.create(
                      res.discountReport, res.productorId,
                      productName, res.status, res.discount
                    ).map { resNew =>
          repoDiscReport.addToTotal(resNew.discountReport, resNew.discount);
          Redirect(routes.DiscountReportController.show(res.discountReport))
        }
      }
    )
  }

  def setProductor = Action.async{ implicit request =>
    setProductorForm.bindFromRequest.fold(
      errorForm => {
        Future.successful(Ok(views.html.discountDetail_add(new MyDeadboltHandler, discountId, newForm, discountsNames, productorsNames)))
      },
      res => {
        repoProductors.searchByAccount(res.account).map { resProductors =>
          val cache = collection.mutable.Map[String, String]()
          resProductors.map { productor => 
            cache put (productor.id.toString(), productor.account.toString + ": " +productor.nombre.toString)
          }
          productorsNames = cache.toMap
          Ok(views.html.discountDetail_add(new MyDeadboltHandler, discountId, newForm, discountsNames, cache.toMap))
        }
      }
    )
  }

  def setProductorName = Action.async{ implicit request =>
    setProductorNameForm.bindFromRequest.fold(
      errorForm => {
        Future.successful(Ok(views.html.discountDetail_add(new MyDeadboltHandler, discountId, newForm, discountsNames, productorsNames)))
      },
      res => {
        repoProductors.searchByName(res.name).map { resProductors =>
          val cache = collection.mutable.Map[String, String]()
          resProductors.map { productor => 
            cache put (productor.id.toString(), productor.account.toString + ": " +productor.nombre.toString)
          }
          productorsNames = cache.toMap
          Ok(views.html.discountDetail_add(new MyDeadboltHandler, discountId, newForm, discountsNames, cache.toMap))
        }
      }
    )
  }

  var discountId: Long = 0

  def addGet(discountIdParam: Long) = Action { implicit request =>
    discountsNames = getParentList(discountIdParam)
    productorsNames = getProductorsNamesMap()
    discountId = discountIdParam
    Ok(views.html.discountDetail_add(new MyDeadboltHandler, discountId, newForm, discountsNames, productorsNames))
  }

  def getDiscountDetails = Action.async {
    repo.list().map { res =>
      Ok(Json.toJson(res))
    }
  }

  def getDiscountDetailsByReport(id: Long) = Action.async {
    repo.listByReport(id).map { res =>
      Ok(Json.toJson(res))
    }
  }
  
  def getDiscountDetailsByProductor(id: Long) = Action.async {
    repo.listByProductor(id).map { res =>
      Ok(Json.toJson(res))
    }
  }

  def getDiscountDetailsByInsumo(id: Long) = Action.async {
    repo.listByInsumo(id).map { res =>
      Ok(Json.toJson(res))
    }
  }

  // update required
  val updateForm: Form[UpdateDiscountDetailForm] = Form {
    mapping(
      "id" -> longNumber,
      "discountReport" -> longNumber,
      "productorId" -> longNumber,
      "status" -> text,
      "discount" -> of[Double]
    )(UpdateDiscountDetailForm.apply)(UpdateDiscountDetailForm.unapply)
  }

  // to copy
  def show(id: Long) = Action.async { implicit request =>
    repo.getById(id).map { res =>
      Ok(views.html.discountDetail_show(new MyDeadboltHandler, res(0)))
    }
  }

  var udpatedRow: DiscountDetail = _

  // update required
  def getUpdate(id: Long) = Action.async { implicit request =>
    repo.getById(id).map { res =>
      val anyData = Map("id" -> id.toString().toString(), "discountReport" -> res.toList(0).discountReport.toString(), "productorId" -> res.toList(0).productorId.toString(), "status" -> res.toList(0).status.toString(), "discount" -> res.toList(0).discount.toString())
      discountsNames = getParentList(res(0).discountReport)
      productorsNames = getProductorsById(res(0).productorId)
      udpatedRow = res(0)
      Ok(views.html.discountDetail_update(new MyDeadboltHandler, udpatedRow, updateForm.bind(anyData), discountsNames, productorsNames))
    }
  }

  def getParentList(parentId: Long): Map[String, String] = {
    Await.result(repoDiscReport.getById(parentId).map{ res => 
      val cache = collection.mutable.Map[String, String]()
      res.foreach{ parent => 
        cache put (parent.id.toString(), parent.id.toString)
      }
      cache.toMap
    }, 100.millis)
  }

  def getProductorsById(productorId: Long): Map[String, String] = {
    Await.result(repoProductors.getById(productorId).map { productors => 
      val cache = collection.mutable.Map[String, String]()
      productors.foreach { productor =>
        cache put (productor.id.toString(), productor.account + ": " + productor.nombre)
      }
      cache.toMap
    }, 500.millis)
  }

  def getProductorsNamesMap(): Map[String, String] = {
    Await.result(repoProductors.list100Productors().map { productors => 
      val cache = collection.mutable.Map[String, String]()
      productors.foreach { productor =>
        cache put (productor.id.toString(), productor.account + ": " + productor.nombre)
      }
      cache.toMap
    }, 2000.millis)
  }

  def getParentId(id: Long): Long = {
    Await.result(repo.getById(id).map { res =>
      res(0).discountReport
      }, 1000.millis)
  }

  def getDiscount(id: Long): Double = {
    Await.result(repo.getById(id).map { res =>
      res(0).discount
      }, 1000.millis)
  }


  // delete required
  def delete(id: Long) = Action.async {
    val parentId = getParentId(id)
    val discount = getDiscount(id)
    repo.delete(id).map { res =>
      println(res);
      repoDiscReport.addToTotal(parentId, -discount);
      Redirect(routes.DiscountReportController.show(parentId))
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
        Future.successful(Ok(views.html.discountDetail_update(new MyDeadboltHandler, udpatedRow, updateForm, discountsNames, productorsNames)))
      },
      res => {
        val oldDiscount = getDiscount(res.id)
        repo.update(res.id, res.discountReport, res.productorId, productorsNames(res.productorId.toString), res.status, res.discount).map { _ =>
          repoDiscReport.addToTotal(res.discountReport, res.discount - oldDiscount);
          Redirect(routes.DiscountDetailController.show(res.id))
        }
      }
    )
  }
}

case class SetProductorForm(account: String)

case class SetProductorNameForm(name: String)

case class CreateDiscountDetailForm(discountReport: Long, productorId: Long, status: String, discount: Double)

case class UpdateDiscountDetailForm(id: Long, discountReport: Long, productorId: Long, status: String, discount: Double)