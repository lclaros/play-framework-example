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

class DiscountDetailController @Inject() (repo: DiscountDetailRepository, repoDiscReport: DiscountReportRepository, repoProductors: ProductorRepository, val messagesApi: MessagesApi)
                                 (implicit ec: ExecutionContext) extends Controller with I18nSupport{

  val newForm: Form[CreateDiscountDetailForm] = Form {
    mapping(
      "discountReport" -> longNumber,
      "productorId" -> longNumber,
      "status" -> text,
      "discount" -> of[Double]
    )(CreateDiscountDetailForm.apply)(CreateDiscountDetailForm.unapply)
  }

  val unidades = scala.collection.immutable.Map[String, String]("1" -> "Unidad 1", "2" -> "Unidad 2")
  var discountsNames = getDiscountRepMap()
  var productorsNames = getProductorsNamesMap()

  def index = Action {
    discountsNames = getDiscountRepMap()
    productorsNames = getProductorsNamesMap()
    Ok(views.html.discountDetail_index(newForm, discountsNames, productorsNames))
  }

  def add = Action.async { implicit request =>
    newForm.bindFromRequest.fold(
      errorForm => {
        Future.successful(Ok(views.html.discountDetail_index(errorForm, Map[String, String](), Map[String, String]())))
      },
      res => {
        repo.create(res.discountReport, res.productorId, productorsNames(res.productorId.toString), res.status, res.discount).map { _ =>
          //repoDiscReport.updatediscount(res.discountReport, res.status)
          Redirect(routes.DiscountReportController.show(res.discountReport))
        }
      }
    )
  }

  def addGet = Action {
    discountsNames = getDiscountRepMap()
    productorsNames = getProductorsNamesMap()
    Ok(views.html.discountDetail_add(newForm, discountsNames, productorsNames))
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
  def show(id: Long) = Action {
    Ok(views.html.discountDetail_show())
  }

  // update required
  def getUpdate(id: Long) = Action.async {
    repo.getById(id).map {case (res) =>
      val anyData = Map("id" -> id.toString().toString(), "discountReport" -> res.toList(0).discountReport.toString(), "productorId" -> res.toList(0).productorId.toString(), "status" -> res.toList(0).status.toString(), "discount" -> res.toList(0).discount.toString())
      val discountRepMap = getDiscountRepMap()
      val proveeMap = getProductorsNamesMap()
      Ok(views.html.discountDetail_update(updateForm.bind(anyData), discountRepMap, proveeMap))
    }
  }

  def getDiscountRepMap(): Map[String, String] = {
    Await.result(repoDiscReport.getListNames().map{ case (res1) => 
      val cache = collection.mutable.Map[String, String]()
      res1.foreach{ case (key: Long, value: String) => 
        cache put (key.toString(), value)
      }
      println(cache)
      cache.toMap
    }, 3000.millis)
  }

  def getProductorsNamesMap(): Map[String, String] = {
    Await.result(repoProductors.getListNames().map{ case (res1) => 
      val cache = collection.mutable.Map[String, String]()
      res1.foreach{ case (key: Long, value: String) => 
        cache put (key.toString(), value)
      }
      println(cache)
      cache.toMap
    }, 3000.millis)
  }


  // delete required
  def delete(id: Long) = Action.async {
    repo.delete(id).map { res =>
      println(res);
      //repoDiscReport.updatediscount(res.discountReport, );
      Ok(views.html.discountDetail_index(newForm, Map[String, String](), Map[String, String]()))
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
        Future.successful(Ok(views.html.discountDetail_update(errorForm, Map[String, String](), Map[String, String]())))
      },
      res => {
        repo.update(res.id, res.discountReport, res.productorId, productorsNames(res.productorId.toString), res.status, res.discount).map { _ =>
          Redirect(routes.DiscountDetailController.index)
        }
      }
    )
  }
}

case class CreateDiscountDetailForm(discountReport: Long, productorId: Long, status: String, discount: Double)

case class UpdateDiscountDetailForm(id: Long, discountReport: Long, productorId: Long, status: String, discount: Double)