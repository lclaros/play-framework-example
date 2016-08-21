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
                                               repoProduct: ProductRepository, repoModule: ModuleRepository, repoDriver: ProductorRepository, repoProductor: ProductorRepository,
                                               repoUnit: MeasureRepository, val messagesApi: MessagesApi)
                                 (implicit ec: ExecutionContext) extends Controller with I18nSupport {

  val newForm: Form[CreateRequestRowProductorForm] = Form {
    mapping(
      "requestRowId" -> longNumber,
      "productId" -> longNumber,
      "productorId" -> longNumber,
      "quantity" -> number,
      "price" -> of[Double],
      "totalValue" -> of[Double],
      "paid" -> of[Double],
      "credit" -> of[Double],
      "status" -> text,
      "measureId" -> longNumber,
      "observation" -> text
    )(CreateRequestRowProductorForm.apply)(CreateRequestRowProductorForm.unapply)
  }

  val newDriverForm: Form[CreateRequestRowDriverForm] = Form {
    mapping(
      "requestRowId" -> longNumber,
      "productId" -> longNumber,
      "productorId" -> longNumber,
      "quantity" -> number,
      "totalValue" -> of[Double],
      "paid" -> of[Double],
      "credit" -> of[Double],
      "status" -> text,
      "measureId" -> longNumber,
      "observation" -> text
    )(CreateRequestRowDriverForm.apply)(CreateRequestRowDriverForm.unapply)
  }

  var measures = getMeasureMap()
  var requestRows = getRequestRowsMap(0)
  var products = getProducts(0)
  var productors = getProductors()
  var modules = getModules()
  var drivers = getDrivers()
  var updatedRow: RequestRowProductor = _
  var parentId: Long = _
  var requestRow: RequestRow = _
  val productorType = "productor"
  val moduleType = "module"
  val driverType = "driver"

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
    products = getProducts(requestRow.productId)
    productors = getProductors()
    measures = getMeasureMap()
    Ok(views.html.requestRowProductor_add(new MyDeadboltHandler, parentId, searchProductorForm, newForm, requestRows,
                                          products, productors, measures))
  }

  def addModuleGet(requestRowId: Long) = Action { implicit request =>
    parentId = requestRowId
    requestRow = getRequestRowObj(requestRowId)
    requestRows = getRequestRowsMap(requestRowId)
    products = getProducts(requestRow.productId)
    modules = getModules()
    measures = getMeasureMap()
    Ok(views.html.requestRowModule_add(new MyDeadboltHandler, parentId, searchModuleForm, newForm, requestRows,
                                          products, modules, measures))
  }

  def addDriverGet(requestRowId: Long) = Action { implicit request =>
    parentId = requestRowId
    requestRow = getRequestRowObj(requestRowId)
    requestRows = getRequestRowsMap(requestRowId)
    products = getProducts(requestRow.productId)
    drivers = getDrivers()
    measures = getMeasureMap()
    Ok(views.html.requestRowDriver_add(new MyDeadboltHandler, parentId, searchDriverForm, newDriverForm, requestRows,
                                          products, drivers, measures))
  }
  def add = Action.async { implicit request =>
    newForm.bindFromRequest.fold(
      errorForm => {
        println(errorForm)
        Future.successful(Ok(views.html.requestRowProductor_add(new MyDeadboltHandler, parentId, searchProductorForm, errorForm,
                              requestRows, products, productors, measures)))
      },
      res => {
        repo.create(  
                      res.requestRowId, res.productId, products(res.productId.toString()),
                      res.productorId, productors(res.productorId.toString()),
                      res.quantity, res.price, res.totalValue, res.paid, res.credit, res.status, res.measureId,
                      measures(res.measureId.toString()), productorType, res.observation
                    ).map { resNew =>
          repoProductor.updateTotalDebt(res.productorId, res.credit);
          Redirect(routes.RequestRowProductorController.show(resNew.id))
        }
      }
    )
  }

  def addModule = Action.async { implicit request =>
    newForm.bindFromRequest.fold(
      errorForm => {
        Future.successful(Ok(views.html.requestRowModule_add(new MyDeadboltHandler, parentId, searchModuleForm, errorForm,
                              requestRows, products, modules, measures)))
      },
      res => {
        repo.create(  
                      res.requestRowId, res.productId, products(res.productId.toString()),
                      res.productorId, modules(res.productorId.toString()),
                      res.quantity, res.price, res.totalValue, res.paid, res.credit,
                      res.status, res.measureId, measures(res.measureId.toString()), moduleType, res.observation
                    ).map { resNew =>
          //repoModule.updateTotalDebt(res.productorId, res.credit);
          Redirect(routes.RequestRowProductorController.show(resNew.id))
        }
      }
    )
  }

  def addDriver = Action.async { implicit request =>
    newDriverForm.bindFromRequest.fold(
      errorForm => {
        println(errorForm)
        Future.successful(Ok(views.html.requestRowDriver_add(new MyDeadboltHandler, parentId, searchDriverForm, errorForm,
                              requestRows, products, drivers, measures)))
      },
      res => {
        repo.create(  
                      res.requestRowId, res.productId, products(res.productId.toString()),
                      res.productorId, drivers(res.productorId.toString()),
                      res.quantity, 0, res.totalValue, res.paid, res.credit,
                      res.status, res.measureId, measures(res.measureId.toString()),
                      driverType, res.observation
                    ).map { resNew =>
          //repoProductor.updateTotalDebt(res.productorId, res.credit);
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
      "totalValue" -> of[Double],
      "paid" -> of[Double],
      "credit" -> of[Double],
      "status" -> text,
      "measureId" -> longNumber,
      "observation" -> text
    )(UpdateRequestRowProductorForm.apply)(UpdateRequestRowProductorForm.unapply)
  }

  val updateDriverForm: Form[UpdateRequestRowDriverForm] = Form {
    mapping(
      "id" -> longNumber,
      "requestRowId" -> longNumber,
      "productId" -> longNumber,
      "productorId" -> longNumber,
      "quantity" -> number,
      "totalValue" -> of[Double],
      "paid" -> of[Double],
      "credit" -> of[Double],
      "status" -> text,
      "measureId" -> longNumber,
      "observation" -> text
    )(UpdateRequestRowDriverForm.apply)(UpdateRequestRowDriverForm.unapply)
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
                                "quantity" -> updatedRow.quantity.toString(), "price" -> updatedRow.price.toString(), 
                                "totalValue" -> updatedRow.price.toString(), "paid" -> updatedRow.price.toString(),
                                "credit" -> updatedRow.price.toString(), "status" -> updatedRow.status.toString(), 
                                "measureId" -> updatedRow.measureId.toString(), "observation" -> updatedRow.observation)
      requestRows = getRequestRowsMap(updatedRow.requestRowId)
      requestRow = getRequestRowObj(updatedRow.requestRowId)
      products = getProducts(requestRow.productId)
      productors = getProductorById(updatedRow.productId)

      Ok(views.html.requestRowProductor_update(new MyDeadboltHandler, updatedRow,
                updateForm.bind(anyData), requestRows, products, productors, measures))

    }
  }

  // update required
  def getModuleUpdate(id: Long) = Action.async { implicit request =>
    repo.getById(id).map {case (res) =>
      updatedRow = res(0)
      val anyData = Map("id" -> id.toString().toString(), "requestRowId" -> updatedRow.requestRowId.toString(),
                                "productId" -> updatedRow.productId.toString(), "productorId" -> updatedRow.productorId.toString(),
                                "quantity" -> updatedRow.quantity.toString(), "price" -> updatedRow.price.toString(), 
                                "totalValue" -> updatedRow.price.toString(), "paid" -> updatedRow.price.toString(),
                                "credit" -> updatedRow.price.toString(), "status" -> updatedRow.status.toString(), 
                                "measureId" -> updatedRow.measureId.toString(), "observation" -> updatedRow.observation)
      requestRows = getRequestRowsMap(updatedRow.requestRowId)
      requestRow = getRequestRowObj(updatedRow.requestRowId)
      products = getProducts(requestRow.productId)
      modules = getModuleById(updatedRow.productId)
      Ok(views.html.requestRowModule_update(new MyDeadboltHandler, updatedRow,
                updateForm.bind(anyData), requestRows, products, modules, measures))
    }
  }

  // update required
  def getDriverUpdate(id: Long) = Action.async { implicit request =>
    repo.getById(id).map {case (res) =>
      updatedRow = res(0)
      val anyData = Map("id" -> id.toString().toString(), "requestRowId" -> updatedRow.requestRowId.toString(),
                                "productId" -> updatedRow.productId.toString(), "productorId" -> updatedRow.productorId.toString(),
                                "quantity" -> updatedRow.quantity.toString(), 
                                "totalValue" -> updatedRow.price.toString(), "paid" -> updatedRow.price.toString(),
                                "credit" -> updatedRow.price.toString(), "status" -> updatedRow.status.toString(), 
                                "measureId" -> updatedRow.measureId.toString(), "observation" -> updatedRow.observation)
      requestRows = getRequestRowsMap(updatedRow.requestRowId)
      requestRow = getRequestRowObj(updatedRow.requestRowId)
      products = getProducts(requestRow.productId)
      drivers = getDriverById(updatedRow.productId)

      Ok(views.html.requestRowDriver_update(new MyDeadboltHandler, updatedRow,
                updateDriverForm.bind(anyData), requestRows, products, drivers, measures))

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

  def getProducts(id: Long): Map[String, String] = {
    Await.result(repoProduct.getById(id).map { res1 => 
      val cache = collection.mutable.Map[String, String]()
      res1.foreach{ product => 
        cache put (product.id.toString(), product.name)
      }
      cache.toMap
    }, 3000.millis)
  }

  def getModules(id: Long): Map[String, String] = {
    Await.result(repoModule.getById(id).map { res1 => 
      val cache = collection.mutable.Map[String, String]()
      res1.foreach{ product => 
        cache put (product.id.toString(), product.name)
      }
      cache.toMap
    }, 3000.millis)
  }

  def getDrivers(id: Long): Map[String, String] = {
    Await.result(repoProduct.getById(id).map { res1 => 
      val cache = collection.mutable.Map[String, String]()
      res1.foreach{ product => 
        cache put (product.id.toString(), product.name)
      }
      cache.toMap
    }, 3000.millis)
  }

  def getProductors(): Map[String, String] = {
    Await.result(repoProductor.getListNames().map{ case (res1) => 
      val cache = collection.mutable.Map[String, String]()
      res1.foreach{ case (key: Long, value: String) => 
        cache put (key.toString(), value)
      }
      
      cache.toMap
    }, 3000.millis)
  }

  def getModules(): Map[String, String] = {
    Await.result(repoModule.getListNames().map{ case (res1) => 
      val cache = collection.mutable.Map[String, String]()
      res1.foreach{ case (key: Long, value: String) => 
        cache put (key.toString(), value)
      }
      
      cache.toMap
    }, 3000.millis)
  }

  def getDrivers(): Map[String, String] = {
    Await.result(repoProductor.getListNames().map{ case (res1) => 
      val cache = collection.mutable.Map[String, String]()
      res1.foreach{ case (key: Long, value: String) => 
        cache put (key.toString(), value)
      }
      
      cache.toMap
    }, 3000.millis)
  }

  def getProductorById(productId: Long): Map[String, String] = {
    Await.result(repoProductor.getById(productId).map{ case res1 => 
      val cache = collection.mutable.Map[String, String]()
      res1.foreach { product => 
        cache put (product.id.toString, product.name)
      }      
      cache.toMap
    }, 3000.millis)
  }

  def getModuleById(moduleId: Long): Map[String, String] = {
    Await.result(repoModule.getById(moduleId).map{ case res1 => 
      val cache = collection.mutable.Map[String, String]()
      res1.foreach { module => 
        cache put (module.id.toString, module.name)
      }      
      cache.toMap
    }, 3000.millis)
  }

  def getDriverById(driverId: Long): Map[String, String] = {
    Await.result(repoDriver.getById(driverId).map{ case res1 => 
      val cache = collection.mutable.Map[String, String]()
      res1.foreach { driver => 
        cache put (driver.id.toString, driver.name)
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

  def updatePost = Action.async { implicit request =>
    updateForm.bindFromRequest.fold(
      errorForm => {
        Future.successful(Ok(views.html.requestRowProductor_update(new MyDeadboltHandler, updatedRow,
                                            errorForm, requestRows, products, productors, measures)))
      },
      res => {
        repo.update(res.id, res.requestRowId, res.productId, products(res.productId.toString), res.productorId,
                    productors(res.productorId.toString), res.quantity, res.price, res.totalValue, res.paid, 
                    res.credit, res.status, res.measureId, measures(res.measureId.toString()),
                    productorType, res.observation).map { _ =>
          Redirect(routes.RequestRowProductorController.show(res.id))
        }
      }
    )
  }

  def updateModulePost = Action.async { implicit request =>
    updateForm.bindFromRequest.fold(
      errorForm => {
        Future.successful(Ok(views.html.requestRowModule_update(new MyDeadboltHandler, updatedRow,
                                            errorForm, requestRows, products, modules, measures)))
      },
      res => {
        repo.update(res.id, res.requestRowId, res.productId, products(res.productId.toString), res.productorId,
                    modules(res.productorId.toString), res.quantity, res.price, res.totalValue, res.paid, 
                    res.credit, res.status, res.measureId, measures(res.measureId.toString()),
                    moduleType, res.observation).map { _ =>
          Redirect(routes.RequestRowProductorController.show(res.id))
        }
      }
    )
  }

  def updateDriverPost = Action.async { implicit request =>
    updateDriverForm.bindFromRequest.fold(
      errorForm => {
        Future.successful(Ok(views.html.requestRowDriver_update(new MyDeadboltHandler, updatedRow,
                                            errorForm, requestRows, products, drivers, measures)))
      },
      res => {
        repo.update(res.id, res.requestRowId, res.productId, products(res.productId.toString), res.productorId,
                    drivers(res.productorId.toString), res.quantity, 0, res.totalValue, res.paid, 
                    res.credit, res.status, res.measureId, measures(res.measureId.toString()),
                    driverType, res.observation).map { _ =>
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

  val searchModuleForm: Form[SearchModuleForm] = Form {
    mapping(
      "search" -> text
    )(SearchModuleForm.apply)(SearchModuleForm.unapply)
  }

  val searchDriverForm: Form[SearchDriverForm] = Form {
    mapping(
      "search" -> text
    )(SearchDriverForm.apply)(SearchDriverForm.unapply)
  }

  def searchProductorPost = Action.async { implicit request =>
    searchProductorForm.bindFromRequest.fold(
      errorForm => {
        Future.successful(Ok(views.html.requestRowProductor_add(new MyDeadboltHandler, parentId, searchProductorForm,
                          newForm, requestRows, products, productors, measures)))
      },
      res => {
        repoProductor.searchProductor(res.search).map { resProductors =>
          val cache = collection.mutable.Map[String, String]()
          resProductors.map { productor => 
            cache put (productor.id.toString(), productor.account.toString + ": " +productor.name.toString)
          }
          productors = cache.toMap
          Ok(views.html.requestRowProductor_add(new MyDeadboltHandler, parentId, searchProductorForm, newForm, requestRows, products, productors, measures))
        }
      }
    )
  }
  
  def searchModulePost = Action.async { implicit request =>
    searchModuleForm.bindFromRequest.fold(
      errorForm => {
        Future.successful(Ok(views.html.requestRowModule_add(new MyDeadboltHandler, parentId, searchModuleForm,
                          newForm, requestRows, products, productors, measures)))
      },
      res => {
        repoModule.searchModule(res.search).map { resModules =>
          val cache = collection.mutable.Map[String, String]()
          resModules.map { module => 
            cache put (module.id.toString(), module.name.toString)
          }
          productors = cache.toMap
          Ok(views.html.requestRowModule_add(new MyDeadboltHandler, parentId, searchModuleForm, newForm,
            requestRows, products, productors, measures))
        }
      }
    )
  }

  def searchDriverPost = Action.async { implicit request =>
    searchDriverForm.bindFromRequest.fold(
      errorForm => {
        Future.successful(Ok(views.html.requestRowDriver_add(new MyDeadboltHandler, parentId, searchDriverForm,
                          newDriverForm, requestRows, products, productors, measures)))
      },
      res => {
        repoDriver.searchProductor(res.search).map { resDrivers =>
          val cache = collection.mutable.Map[String, String]()
          resDrivers.map { driver => 
            cache put (driver.id.toString(), driver.name.toString)
          }
          productors = cache.toMap
          Ok(views.html.requestRowDriver_add(new MyDeadboltHandler, parentId, searchDriverForm, newDriverForm,
            requestRows, products, productors, measures))
        }
      }
    )
  }

}

case class CreateRequestRowProductorForm(requestRowId: Long, productId: Long,
                productorId: Long, quantity: Int,
                price: Double, totalValue: Double, paid: Double, credit: Double, status: String,
                measureId: Long, observation: String)

case class CreateRequestRowDriverForm(requestRowId: Long, productId: Long,
                productorId: Long, quantity: Int,
                totalValue: Double, paid: Double, credit: Double, status: String,
                measureId: Long, observation: String)

case class UpdateRequestRowProductorForm(id: Long, requestRowId: Long, productId: Long,
                productorId: Long, quantity: Int,
                price: Double, totalValue: Double, paid: Double, credit: Double, status: String,
                measureId: Long, observation: String)

case class UpdateRequestRowDriverForm(id: Long, requestRowId: Long, productId: Long,
                productorId: Long, quantity: Int,
                totalValue: Double, paid: Double, credit: Double, status: String,
                measureId: Long, observation: String)
