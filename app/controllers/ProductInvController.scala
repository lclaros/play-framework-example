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
import be.objectify.deadbolt.scala.DeadboltActions
import security.MyDeadboltHandler


class ProductInvController @Inject() (repo: ProductInvRepository, repoProduct: ProductRepository, repoProvee: ProveedorRepository, val messagesApi: MessagesApi)
                                 (implicit ec: ExecutionContext) extends Controller with I18nSupport{

  val newForm: Form[CreateProductInvForm] = Form {
    mapping(
      "productId" -> longNumber,
      "proveedorId" -> longNumber,
      "amount" -> number,
      "amountLeft" -> number
    )(CreateProductInvForm.apply)(CreateProductInvForm.unapply)
  }

  val unidades = scala.collection.immutable.Map[String, String]("1" -> "Unidad 1", "2" -> "Unidad 2")

  def index = Action.async { implicit request =>
    repo.list().map { res =>
      Ok(views.html.productInv_index(new MyDeadboltHandler, res))
    }
  }
  var insumosNames = getInsumoNamesMapByProduct(0)
  var proveeNames = getProveeNamesMap()
  var productId: Long = 0
  def addGet(productId: Long) = Action { implicit request =>
    this.productId = productId
    insumosNames = getInsumoNamesMapByProduct(productId)
    proveeNames = getProveeNamesMap()
    Ok(views.html.productInv_add(new MyDeadboltHandler, productId, newForm, insumosNames, proveeNames))
  }

  def request = Action {
    insumosNames = getInsumoNamesMap()
    proveeNames = getProveeNamesMap()
    Ok(views.html.productInv_request(newForm, insumosNames, proveeNames))
  }

  def request_row = Action {
    insumosNames = getInsumoNamesMap()
    proveeNames = getProveeNamesMap()
    Ok(views.html.productInv_request_row(newForm, insumosNames, proveeNames))
  }

  def add = Action.async { implicit request =>
    newForm.bindFromRequest.fold(
      errorForm => {
        Future.successful(Ok(views.html.productInv_add(new MyDeadboltHandler, productId, newForm, insumosNames, proveeNames)))
      },
      res => {
        repo.create(res.productId, res.proveedorId, res.amount, res.amountLeft).map { resNew =>
          repoProduct.updateAmount(res.productId, res.amount)
          Redirect(routes.ProductInvController.show(resNew.id))
        }
      }
    )
  }

  def add_request_row = Action.async { implicit request =>
    newForm.bindFromRequest.fold(
      errorForm => {
        Future.successful(Ok(views.html.productInv_request(errorForm, Map[String, String](), Map[String, String]())))
      },
      res => {
        repo.create(res.productId, res.proveedorId, res.amount, res.amountLeft).map { _ =>
          Redirect(routes.ProductInvController.request)
        }
      }
    )
  }

  def getProductInvs = Action.async {
    repo.list().map { res =>
      Ok(Json.toJson(res))
    }
  }

  def getProductInvsByInsumo(id: Long) = Action.async {
    repo.listByInsumo(id).map { res =>
      Ok(Json.toJson(res))
    }
  }


  // update required
  val updateForm: Form[UpdateProductInvForm] = Form {
    mapping(
      "id" -> longNumber,
      "productId" -> longNumber,
      "proveedorId" -> longNumber,
      "amount" -> number,
      "amountLeft" -> number
    )(UpdateProductInvForm.apply)(UpdateProductInvForm.unapply)
  }

  // to copy
  def show(id: Long) = Action.async { implicit request =>
    repo.getById(id). map { res =>
      Ok(views.html.productInv_show(new MyDeadboltHandler, res(0)))
    }
  }

  var updatedRow: ProductInv = _

  // update required
  def getUpdate(id: Long) = Action.async { implicit request =>
    repo.getById(id).map {case (res) =>
      updatedRow = res(0)
      val anyData = Map("id" -> id.toString().toString(), "productId" -> updatedRow.productId.toString(), "proveedorId" -> updatedRow.proveedorId.toString(), "amount" -> updatedRow.amount.toString(), "amountLeft" -> updatedRow.amountLeft.toString())
      insumosNames = getInsumoNamesMap()
      proveeNames = getProveeNamesMap()
      Ok(views.html.productInv_update(new MyDeadboltHandler, updatedRow, updateForm.bind(anyData), insumosNames, proveeNames))
    }
  }

  def getInsumoNamesMap(): Map[String, String] = {
    Await.result(repoProduct.getListNames().map{ case (res1) => 
      val cache = collection.mutable.Map[String, String]()
      res1.foreach{ case (key: Long, value: String) => 
        cache put (key.toString(), value)
      }
      println(cache)
      cache.toMap
    }, 3000.millis)
  }

  def getInsumoNamesMapByProduct(product: Long): Map[String, String] = {
    Await.result(repoProduct.getListNamesById(product).map{ case (res1) => 
      val cache = collection.mutable.Map[String, String]()
      res1.foreach{ case (key: Long, value: String) => 
        cache put (key.toString(), value)
      }
      println(cache)
      cache.toMap
    }, 3000.millis)
  }

  def getProveeNamesMap(): Map[String, String] = {
    Await.result(repoProvee.getListNames().map{ case (res1) => 
      val cache = collection.mutable.Map[String, String]()
      res1.foreach{ case (key: Long, value: String) => 
        cache put (key.toString(), value)
      }
      println(cache)
      cache.toMap
    }, 3000.millis)
  }

  def getParentId(id: Long): Long = {
    Await.result(repo.getById(id).map { res =>
      res(0).productId
      }, 200.millis)
  }

  def getAmountLeft(id: Long): Int = {
    Await.result(repo.getById(id).map { res =>
      res(0).amountLeft
      }, 1000.millis)
  }

  // delete required
  def delete(id: Long) = Action.async {
    val parentId = getParentId(id)
    val amountLeft = getAmountLeft(id)
    repo.delete(id).map { res =>
      repoProduct.updateInventary(parentId, -amountLeft);
      Redirect(routes.ProductController.show(parentId))
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
        Future.successful(Ok(views.html.productInv_update(new MyDeadboltHandler, updatedRow, updateForm, insumosNames, proveeNames)))
      },
      res => {
        val oldAmountLeft = getAmountLeft(res.id)
        repo.update(res.id, res.productId, res.proveedorId, res.amount, res.amountLeft).map { _ =>
          repoProduct.updateInventary(res.productId, res.amountLeft - oldAmountLeft);
          Redirect(routes.ProductInvController.show(res.id))
        }
      }
    )
  }

}

case class CreateProductInvForm(productId: Long, proveedorId: Long, amount: Int, amountLeft: Int)

case class UpdateProductInvForm(id: Long, productId: Long, proveedorId: Long, amount: Int, amountLeft: Int)