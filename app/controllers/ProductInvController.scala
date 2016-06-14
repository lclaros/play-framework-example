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

class ProductInvController @Inject() (repo: ProductInvRepository, repoInsum: ProductRepository, repoProvee: ProveedorRepository, val messagesApi: MessagesApi)
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

  def index = Action {
    val insumosNames = getInsumoNamesMap()
    val proveeNames = getProveeNamesMap()
    Ok(views.html.productInv_index(newForm, insumosNames, proveeNames))
  }

  def addByProduct(productId: Long) = Action {
    val insumosNames = getInsumoNamesMapByProduct(productId)
    val proveeNames = getProveeNamesMap()
    Ok(views.html.productInv_index(newForm, insumosNames, proveeNames))
  }

  def request = Action {
    val insumosNames = getInsumoNamesMap()
    val proveeNames = getProveeNamesMap()
    Ok(views.html.productInv_request(newForm, insumosNames, proveeNames))
  }

  def request_row = Action {
    val insumosNames = getInsumoNamesMap()
    val proveeNames = getProveeNamesMap()
    Ok(views.html.productInv_request_row(newForm, insumosNames, proveeNames))
  }

  def add = Action.async { implicit request =>
    newForm.bindFromRequest.fold(
      errorForm => {
        Future.successful(Ok(views.html.productInv_index(errorForm, Map[String, String](), Map[String, String]())))
      },
      res => {
        repo.create(res.productId, res.proveedorId, res.amount, res.amountLeft).map { _ =>
          repoInsum.updateAmount(res.productId, res.amount)
          Redirect(routes.ProductController.show(res.productId))
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
  def show(id: Long) = Action {
    Ok(views.html.productInv_show())
  }

  // update required
  def getUpdate(id: Long) = Action.async {
    repo.getById(id).map {case (res) =>
      val anyData = Map("id" -> id.toString().toString(), "productId" -> res.toList(0).productId.toString(), "proveedorId" -> res.toList(0).proveedorId.toString(), "amount" -> res.toList(0).amount.toString(), "amountLeft" -> res.toList(0).amountLeft.toString())
      val insumosMap = getInsumoNamesMap()
      val proveeMap = getProveeNamesMap()
      Ok(views.html.productInv_update(updateForm.bind(anyData), insumosMap, proveeMap))
    }
  }

  def getInsumoNamesMap(): Map[String, String] = {
    Await.result(repoInsum.getListNames().map{ case (res1) => 
      val cache = collection.mutable.Map[String, String]()
      res1.foreach{ case (key: Long, value: String) => 
        cache put (key.toString(), value)
      }
      println(cache)
      cache.toMap
    }, 3000.millis)
  }

  def getInsumoNamesMapByProduct(product: Long): Map[String, String] = {
    Await.result(repoInsum.getListNamesById(product).map{ case (res1) => 
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


  // delete required
  def delete(id: Long) = Action.async {
    repo.delete(id).map { res =>
      println(res);
      //repoInsum.updateAmount(res.productId, );
      Ok(views.html.productInv_index(newForm, Map[String, String](), Map[String, String]()))
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
        Future.successful(Ok(views.html.productInv_update(errorForm, Map[String, String](), Map[String, String]())))
      },
      res => {
        repo.update(res.id, res.productId, res.proveedorId, res.amount, res.amountLeft).map { _ =>
          Redirect(routes.ProductInvController.index)
        }
      }
    )
  }

}

case class CreateProductInvForm(productId: Long, proveedorId: Long, amount: Int, amountLeft: Int)

case class UpdateProductInvForm(id: Long, productId: Long, proveedorId: Long, amount: Int, amountLeft: Int)