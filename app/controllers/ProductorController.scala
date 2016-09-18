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
                                    repoModule: ModuleRepository, repoProductor: ProductorRepository, repoDiscount: DiscountDetailRepository,
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
      "module" -> longNumber,
      "acopio" -> number,
      "promedio" -> number,
      "excedent" -> number,
      "pleno" -> number
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

  var associations: Seq[Association] = _

  def index_association() = Action.async { implicit request =>
    repo.listAssociation().map { res =>
      associations = res
      Ok(views.html.association_index(new MyDeadboltHandler, associations))
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

  def getModuleById(id: Long): Module = {
    Await.result(repoModule.getById(id).map(res => res(0)), 2000.millis)
  }

  def add = Action.async { implicit request =>
    var total = getTotal()
    var currentPage = 1
    newForm.bindFromRequest.fold(
      errorForm => {
        Future.successful(Ok(views.html.productor_add(new MyDeadboltHandler, errorForm, modules)))
      },
      res => {
        val module = getModuleById(res.module)
        repo.create ( res.name, res.carnet, res.telefono, res.direccion,
                      res.account, res.module, module.name,
                      res.acopio, res.promedio, res.excedent, res.pleno,
                      request.session.get("userId").get.toLong,
                      request.session.get("userName").get.toString
                    ).map { resNew =>
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
      "acopio" -> number,
      "promedio" -> number,
      "excedent" -> number,
      "pleno" -> number
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

  def getProductorsByAsso(id: Long): Seq[Productor] = {
    Await.result(repoProductor.listByAssociation(id).map {res => 
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

  def showAssociation(id: Long) = Action.async { implicit request =>
    val productoresByAsso = getProductorsByAsso(id)
    repo.getAssociationById(id).map { res =>
      Ok(views.html.association_show(new MyDeadboltHandler, res(0), productoresByAsso))
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
        "acopio" -> updatedRow.acopio.toString(),
        "promedio" -> updatedRow.promedio.toString(),
        "excedent" -> updatedRow.excedent.toString(),
        "pleno" -> updatedRow.pleno.toString()
        )
      Ok(views.html.productor_update(new MyDeadboltHandler, updatedRow, updateForm.bind(anyData), modules))
    }
  }

  // delete required
  def delete(id: Long) = Action.async { implicit request =>
    //var total = getTotal()
    //var currentPage = 1
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

  def getAssociationById(id: Long) = Action.async {
    repo.getAssociationById(id).map { res =>
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
                      res.acopio, res.promedio, res.excedent, res.pleno,
                      request.session.get("userId").get.toLong,
                      request.session.get("userName").get.toString
                    ).map { _ =>
          Redirect(routes.ProductorController.show(res.id))
        }
      }
    )
  }
}

case class CreateProductorForm(
                                name: String, carnet: Int, telefono: Int,
                                direccion: String, account: String, module: Long,
                                acopio: Int, promedio: Int, excedent: Int, pleno: Int
                              )

case class UpdateProductorForm(
                                id: Long, name: String, carnet: Int, telefono: Int,
                                direccion: String, account: String, module: Long,
                                totalDebt: Double, numberPayment: Int,
                                acopio: Int, promedio: Int, excedent: Int, pleno: Int
                              )

case class SearchProductorForm (search: String)
