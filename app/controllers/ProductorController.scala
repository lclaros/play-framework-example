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
import play.api.data.format.Formats._ 
import be.objectify.deadbolt.scala.DeadboltActions
import security.MyDeadboltHandler

class ProductorController @Inject() (
                                    repo: ProductorRepository, repoRequests: RequestRowProductorRepository,
                                    repoModule: ModuleRepository, repoDiscount: DiscountDetailRepository,
                                      val messagesApi: MessagesApi)
                                 (implicit ec: ExecutionContext) extends Controller with I18nSupport{

  var modules = getModuleNamesMap()
  var interval = 30
  var updatedRow: Productor = _

  def getModuleNamesMap(): Map[String, String] = {
    Await.result(repoModule.getListNames().map{ case (res1) => 
      val cache = collection.mutable.Map[String, String]()
      res1.foreach{ case (key: Long, value: String) => 
        cache put (key.toString(), value)
      }
      cache.toMap
    }, 3000.millis)
  }

  def getTotal(): Int = {
    Await.result(repo.getTotal().map { case (res1) => 
      res1
    }, 3000.millis)
  }

  def searchProductor(search: String): Seq[Productor] = {
    Await.result(repo.searchProductor(search).map{ res => 
      res
    }, 1000.millis)
  }


  val newForm: Form[CreateProductorForm] = Form {
    mapping(
      "name" -> nonEmptyText,
      "carnet" -> number.verifying(min(0), max(9999999)),
      "telefono" -> number.verifying(min(0), max(9999999)),
      "direccion" -> nonEmptyText,
      "account" -> text,
      "module" -> longNumber
    )(CreateProductorForm.apply)(CreateProductorForm.unapply)
  }

  def addGet() = Action.async { implicit request =>
    repo.list(0 * interval, interval).map { res =>
      modules = getModuleNamesMap()
      Ok(views.html.productor_add(new MyDeadboltHandler, newForm, modules))
    }
  }

  var total: Int = _
  var currentPage: Int = _
  var productors: Seq[Productor] = Seq[Productor]()

  def index(start: Int) = Action.async { implicit request =>
    if (start == 0) {
        Future(Ok(views.html.productor_index(new MyDeadboltHandler, newForm, searchForm, productors, total, currentPage, interval)))
      } else {
        repo.list((start - 1) * interval, interval).map { res =>
          productors = res
          total = getTotal()
          currentPage = start
          Ok(views.html.productor_index(new MyDeadboltHandler, newForm, searchForm, productors, total, currentPage, interval))
        }        
      }
  }

  def searchProductorPost = Action.async { implicit request =>
    var total = getTotal()
    var currentPage = 1
    searchForm.bindFromRequest.fold(
      errorForm => {
        Future.successful(Ok(views.html.productor_index(new MyDeadboltHandler, newForm, searchForm, productors, total, currentPage, interval)))
      },
      res => {
        productors = searchProductor(res.search)
        modules = getModuleNamesMap()
        var total = getTotal()
        var currentPage = 1
        Future(Ok(views.html.productor_index(new MyDeadboltHandler, newForm, searchForm, productors, total, currentPage, interval)))
      }
    )
  }

  def index_pdf = Action {
    val generator = new PdfGenerator
    Ok(generator.toBytes(views.html.reporte_productores(), "http://localhost:9000/")).as("application/pdf")
  }

  def add = Action.async { implicit request =>
    var total = getTotal()
    var currentPage = 1
    newForm.bindFromRequest.fold(
      errorForm => {
        Future.successful(Ok(views.html.productor_add(new MyDeadboltHandler, errorForm, modules)))
      },
      res => {
        repo.create (res.name, res.carnet, res.telefono, res.direccion,
                    res.account, res.module, modules(res.module.toString)).map { resNew =>
          Redirect(routes.ProductorController.show(resNew.id))
        }
      }
    )
  }

  def getProductorsByModule(id: Long) = Action.async {
    repo.list(0, interval).map { res =>
      Ok(Json.toJson(res))
    }
  }

  def getProductores(page: Int) = Action.async {
    repo.list((page - 1) * interval, interval).map { res =>
      Ok(Json.toJson(res))
    }
  }

  def getProductoresReport = Action.async {
    repo.list(0, interval).map { res =>
      Ok(Json.toJson(res))
    }
  }

  // update required
  val updateForm: Form[UpdateProductorForm] = Form {
    mapping(
      "id" -> longNumber,
      "name" -> nonEmptyText,
      "carnet" -> number,
      "telefono" -> number,
      "direccion" -> nonEmptyText,
      "account" -> text,
      "module" -> longNumber,
      "totalDebt" -> of[Double],
      "numberPayment" -> number,
      "position" -> text
    )(UpdateProductorForm.apply)(UpdateProductorForm.unapply)
  }

  val searchForm: Form[SearchProductorForm] = Form {
    mapping(
      "search" -> text
    )(SearchProductorForm.apply)(SearchProductorForm.unapply)
  }

  def getRequests(id: Long): Seq[RequestRowProductor] = {
    Await.result(repoRequests.listByProductor(id).map {res => 
      res
      }, 1000.millis)
  }

  def getDiscounts(id: Long): Seq[DiscountDetail] = {
    Await.result(repoDiscount.listByProductor(id).map {res => 
      res
      }, 1000.millis)
  }


  // to copy
  def show(id: Long) = Action.async { implicit request =>
    val requests = getRequests(id)
    val discounts = getDiscounts(id)
    repo.getById(id).map { res =>
      Ok(views.html.productor_show(new MyDeadboltHandler, res(0), requests, discounts))
    }
  }


  // update required
  def getUpdate(id: Long) = Action.async { implicit request =>
    modules = getModuleNamesMap()
    repo.getById(id).map { res =>
      updatedRow = res(0)
      val anyData = Map(
        "id" -> id.toString().toString(),
        "name" -> updatedRow.name,
        "carnet" -> updatedRow.carnet.toString(),
        "telefono" -> updatedRow.telefono.toString(),
        "direccion" -> updatedRow.direccion,
        "account" -> updatedRow.account.toString(),
        "module" -> updatedRow.module.toString(),
        "totalDebt" -> updatedRow.totalDebt.toString(),
        "numberPayment" -> updatedRow.numberPayment.toString(),
        "position" -> updatedRow.position.toString()
        )
      Ok(views.html.productor_update(new MyDeadboltHandler, updatedRow, updateForm.bind(anyData), modules))
    }
  }

  // delete required
  def delete(id: Long) = Action.async { implicit request =>
    var total = getTotal()
    var currentPage = 1
    repo.delete(id).map { res =>
      Redirect(routes.ProductorController.index(1))
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
        Future.successful(Ok(views.html.productor_update(new MyDeadboltHandler, updatedRow,errorForm, modules)))
      },
      res => {
        repo.update(
                      res.id, res.name, res.carnet, res.telefono,
                      res.direccion, res.account, res.module,
                      modules(res.module.toString()), "Association Name",
                      res.totalDebt, res.numberPayment,
                      res.position
                    ).map { _ =>
          Redirect(routes.ProductorController.show(res.id))
        }
      }
    )
  }
}

case class CreateProductorForm(
                                name: String, carnet: Int, telefono: Int,
                                direccion: String, account: String, module: Long
                              )

case class UpdateProductorForm(
                                id: Long, name: String, carnet: Int, telefono: Int,
                                direccion: String, account: String, module: Long,
                                totalDebt: Double, numberPayment: Int, position: String
                              )

case class SearchProductorForm (search: String)
