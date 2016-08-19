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

class RequestRowProductorController @Inject() (repo: RequestRowProductorRepository, repoRequestRow: RequestRowRepository, 
                                               repoProduct: ProductRepository, repoProductor: ProductorRepository,
                                               repoUnit: MeasureRepository, val messagesApi: MessagesApi)
                                 (implicit ec: ExecutionContext) extends Controller with I18nSupport {

  val newForm: Form[CreateRequestRowProductorForm] = Form {
    mapping(
      "requestRowId" -> longNumber,
      "productId" -> longNumber,
      "productorId" -> longNumber,
      "quantity" -> number,
      "price" -> of[Double],
      "status" -> text,
      "measureId" -> longNumber,
      "payType" -> text
    )(CreateRequestRowProductorForm.apply)(CreateRequestRowProductorForm.unapply)
  }


  val payTypes = scala.collection.immutable.Map[String, String]("En Efectivo" -> "En Efectivo", "A Credito" -> "A Credito")
  var measures = getMeasureMap()
  var requestRows = getRequestRowsMap(0)
  var products = getProducstMap(0)
  var productors = getProductorsMap()
  var updatedRow: RequestRowProductor = _
  var parentId: Long = _
  var requestRow: RequestRow = _
  val productorType = "productor"

  def index = Action.async { implicit request => 
    repo.list().map { res =>
      Ok(views.html.requestRowProductor_index(new MyDeadboltHandler, res))
    }
  }

  def getRequestRowObj(id: Long): RequestRow = {
    Await.result(repoRequestRow.getById(id).map { res =>
      res(0)
      }, 100.millis)
  }

  def getMeasureMap(): Map[String, String] = {
    Await.result(repoUnit.getListNames().map{ case (res1) => 
      val cache = collection.mutable.Map[String, String]()
      res1.foreach{ case (key: Long, value: String) => 
        cache put (key.toString(), value)
      }
      
      cache.toMap
    }, 3000.millis)
  }


  def addGet(requestRowId: Long) = Action { implicit request =>
    parentId = requestRowId
    requestRow = getRequestRowObj(requestRowId)
    requestRows = getRequestRowsMap(requestRowId)
    products = getProducstMap(requestRow.productId)
    productors = getProductorsMap()
    measures = getMeasureMap()
    Ok(views.html.requestRowProductor_add(new MyDeadboltHandler, parentId, searchProductorForm, newForm, requestRows,
                                          products, productors, measures, payTypes))
  }

  def add = Action.async { implicit request =>
    newForm.bindFromRequest.fold(
      errorForm => {
        println(errorForm)
        Future.successful(Ok(views.html.requestRowProductor_add(new MyDeadboltHandler, parentId, searchProductorForm, errorForm,
                              requestRows, products, productors, measures, payTypes)))
      },
      res => {
        repo.create(  
                      res.requestRowId, res.productId, products(res.productId.toString()),
                      res.productorId, productors(res.productorId.toString()),
                      res.quantity, res.price, res.status, res.measureId,
                      measures(res.measureId.toString()), res.payType, productorType
                    ).map { resNew =>
          repoProductor.updateTotalDebt(res.productorId, res.price);
          Redirect(routes.RequestRowProductorController.show(resNew.id))
        }
      }
    )
  }

  def getRequestRowProductors = Action.async {
    repo.list().map { res =>
      Ok(Json.toJson(res))
    }
  }

  // update required
  val updateForm: Form[UpdateRequestRowProductorForm] = Form {
    mapping(
      "id" -> longNumber,
      "requestRowId" -> longNumber,
      "productId" -> longNumber,
      "productorId" -> longNumber,
      "quantity" -> number,
      "price" -> of[Double],
      "status" -> text,
      "measureId" -> longNumber,
      "payType" -> text

    )(UpdateRequestRowProductorForm.apply)(UpdateRequestRowProductorForm.unapply)
  }

  // to copy
  def show(id: Long) = Action.async { implicit request =>
    repo.getById(id).map { res =>
      Ok(views.html.requestRowProductor_show(new MyDeadboltHandler, res(0)))
    }
  }

  // update required
  def getUpdate(id: Long) = Action.async { implicit request =>
    repo.getById(id).map {case (res) =>
      updatedRow = res(0)
      val anyData = Map("id" -> id.toString().toString(), "requestRowId" -> updatedRow.requestRowId.toString(),
                                "productId" -> updatedRow.productId.toString(), "productorId" -> updatedRow.productorId.toString(),
                                "quantity" -> updatedRow.quantity.toString(), "price" -> updatedRow.price.toString(), "status" -> updatedRow.status.toString(), 
                                "measureId" -> updatedRow.measureId.toString(), "payType" -> updatedRow.payType.toString())
      requestRows = getRequestRowsMap(updatedRow.requestRowId)
      requestRow = getRequestRowObj(updatedRow.requestRowId)
      products = getProducstMap(requestRow.productId)
      productors = getProductorsMapById(updatedRow.productId)

      Ok(views.html.requestRowProductor_update(new MyDeadboltHandler, updatedRow,
                updateForm.bind(anyData), requestRows, products, productors, measures, payTypes))

    }
  }

  def getRequestRowsMap(requestRowId: Long): Map[String, String] = {
    Await.result(repoRequestRow.getById(requestRowId).map { res => 
      val cache = collection.mutable.Map[String, String]()
      res.foreach { requestRow => 
        cache put (requestRow.id.toString, requestRow.id.toString() + ": " + requestRow.productName)
      }
      cache.toMap
    }, 3000.millis)
  }

  def getProducstMap(id: Long): Map[String, String] = {
    Await.result(repoProduct.getById(id).map { res1 => 
      val cache = collection.mutable.Map[String, String]()
      res1.foreach{ product => 
        cache put (product.id.toString(), product.name)
      }
      cache.toMap
    }, 3000.millis)
  }

  def getProductorsMap(): Map[String, String] = {
    Await.result(repoProductor.getListNames().map{ case (res1) => 
      val cache = collection.mutable.Map[String, String]()
      res1.foreach{ case (key: Long, value: String) => 
        cache put (key.toString(), value)
      }
      
      cache.toMap
    }, 3000.millis)
  }

  def getProductorsMapById(productId: Long): Map[String, String] = {
    Await.result(repoProductor.getById(productId).map{ case res1 => 
      val cache = collection.mutable.Map[String, String]()
      res1.foreach { product => 
        cache put (product.id.toString, product.name)
      }      
      cache.toMap
    }, 3000.millis)
  }

  // update required
  def getAccept(id: Long) = Action.async {
    repo.acceptById(id).map {case (res) =>
      repoProduct.updateAmount(res(0).productId, - res(0).quantity);
      Redirect(routes.RequestRowController.show(res(0).requestRowId))
    }
  }

// update required
  def getSend(id: Long) = Action.async {
    repo.sendById(id).map {case (res) =>
      Redirect(routes.RequestRowController.show(res(0).requestRowId))
    }
  }

// update required
  def getFinish(id: Long) = Action.async {
    repo.finishById(id).map {case (res) =>
      Redirect(routes.RequestRowController.show(res(0).requestRowId))
    }
  }

  def getParentId(id: Long): Long = {
    Await.result(repo.getById(id).map { res =>
      res(0).requestRowId
    }, 3000.millis)
  }

  // delete required
  def delete(id: Long) = Action.async {
    var requestRowId = getParentId(id)
    repo.delete(id).map { res =>
      Redirect(routes.RequestRowController.show(requestRowId))// review this to go to the requestRow view
    }
  }

  // to copy
  def getById(id: Long) = Action.async {
    repo.getById(id).map { res =>
      Ok(Json.toJson(res))
    }
  }

  // to copy
  def requestRowProductorsByProductor(id: Long) = Action.async {
    repo.requestRowProductorsByProductor(id).map { res =>
      Ok(Json.toJson(res))
    }
  }

  def getByRequestRow(id: Long) = Action.async {
    repo.requestRowProductorsByRequestRow(id).map { res =>
      Ok(Json.toJson(res))
    }
  }

  // update required
  def updatePost = Action.async { implicit request =>
    updateForm.bindFromRequest.fold(
      errorForm => {
        Future.successful(Ok(views.html.requestRowProductor_update(new MyDeadboltHandler, updatedRow,
                                            errorForm, requestRows, products, productors, measures, payTypes)))
      },
      res => {
        repo.update(res.id, res.requestRowId, res.productId, products(res.productId.toString), res.productorId,
                    productors(res.productorId.toString), res.quantity, res.price, res.status, res.measureId,
                      measures(res.measureId.toString()), res.payType, productorType).map { _ =>
          Redirect(routes.RequestRowProductorController.show(res.id))
        }
      }
    )
  }

  val searchProductorForm: Form[SearchProductorForm] = Form {
    mapping(
      "search" -> text
    )(SearchProductorForm.apply)(SearchProductorForm.unapply)
  }

  def searchProductorPost = Action.async{ implicit request =>
    searchProductorForm.bindFromRequest.fold(
      errorForm => {
        Future.successful(Ok(views.html.requestRowProductor_add(new MyDeadboltHandler, parentId, searchProductorForm,
                          newForm, requestRows, products, productors, measures, payTypes)))
      },
      res => {
        repoProductor.searchProductor(res.search).map { resProductors =>
          val cache = collection.mutable.Map[String, String]()
          resProductors.map { productor => 
            cache put (productor.id.toString(), productor.account.toString + ": " +productor.name.toString)
          }
          productors = cache.toMap
          Ok(views.html.requestRowProductor_add(new MyDeadboltHandler, parentId, searchProductorForm, newForm, requestRows, products, productors, measures, payTypes))
        }
      }
    )
  }



}

case class CreateRequestRowProductorForm(requestRowId: Long, productId: Long, productorId: Long, quantity: Int, price: Double, status: String, measureId: Long, payType: String)

case class UpdateRequestRowProductorForm(id: Long, requestRowId: Long, productId: Long, productorId: Long, quantity: Int, price: Double, status: String, measureId: Long, payType: String)