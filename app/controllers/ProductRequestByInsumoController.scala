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

class ProductRequestByInsumoController @Inject() (
                                                    repo: ProductRequestByInsumoRepository,
                                                    repoVete: UserRepository,
                                                    repoModule: ModuleRepository,
                                                    repoInsUser: UserRepository,
                                                    val messagesApi: MessagesApi
                                                  )
                                 (implicit ec: ExecutionContext) extends Controller with I18nSupport{

  val newForm: Form[CreateProductRequestByInsumoForm] = Form {
    mapping(
      "date" -> text,
      "user" -> longNumber,
      "module" -> longNumber,
      "status" -> text,
      "detail" -> text
    )(CreateProductRequestByInsumoForm.apply)(CreateProductRequestByInsumoForm.unapply)
  }
  var modules = getmodulesNamesMap()

  def index = Action { implicit request =>
    val usersNames = getUserNamesMap(request.session.get("userId").getOrElse("0").toLong)
    modules = getmodulesNamesMap()
    Ok(views.html.productRequestByInsumo_index(usersNames, modules))
  }

  def add = Action.async { implicit request =>
    newForm.bindFromRequest.fold(
      errorForm => {
        Future.successful(Ok(views.html.productRequestByInsumo_index(Map[String, String](), Map[String, String]())))
      },
      res => {
        repo.create(res.date, res.user, res.module, modules(res.module.toString), res.status, res.detail, "veterinaria").map { _ =>
          Redirect(routes.UserController.profileById(res.user))
        }
      }
    )
  }

  def addGet = Action { implicit request =>
    val veterinariosNames = getUserNamesMap(request.session.get("userId").getOrElse("0").toLong)
    modules = getmodulesNamesMap()
    Ok(views.html.productRequestByInsumo_add(newForm, veterinariosNames, modules))
  }

  def getProductRequestByInsumosByUser(id: Long) = Action.async {
    repo.listByUser(id).map { res =>
      Ok(Json.toJson(res))
    }
  }

  def getProductRequestByInsumosBymodule(id: Long) = Action.async {
    repo.listByModule(id).map { res =>
      Ok(Json.toJson(res))
    }
  }

  def getProductRequestByInsumos = Action.async {
    repo.list().map { res =>
      Ok(Json.toJson(res))
    }
  }

  // update required
  val updateForm: Form[UpdateProductRequestByInsumoForm] = Form {
    mapping(
      "id" -> longNumber,
      "date" -> text,
      "user" -> longNumber,
      "module" -> longNumber,
      "status" -> text,
      "detail" -> text
    )(UpdateProductRequestByInsumoForm.apply)(UpdateProductRequestByInsumoForm.unapply)
  }

  // to copy
  def show(id: Long) = Action {
    Ok(views.html.productRequestByInsumo_show())
  }

  // update required
  def getUpdate(id: Long) = Action.async { implicit request =>
    repo.getById(id).map {case (res) =>
      val anyData = Map("id" -> id.toString().toString(), "date" -> res.toList(0).date.toString(), "user" -> res.toList(0).user.toString(), "module" -> res.toList(0).module.toString(), "status" -> res.toList(0).status.toString(), "detail" -> res.toList(0).detail.toString())
      val insumosMap = getUserNamesMap(request.session.get("userId").getOrElse("0").toLong)
      val storeMap = getmodulesNamesMap()
      Ok(views.html.productRequestByInsumo_update(updateForm.bind(anyData), insumosMap, storeMap))
    }
  }

// update required
  def getSend(id: Long) = Action.async { implicit request =>
    repo.sendById(id).map {case (res) =>
      Redirect(routes.UserController.profileById(request.session.get("userId").getOrElse("0").toLong))
    }
  }

// update required
  def getAccept(id: Long) = Action.async { implicit request =>
    repo.acceptById(id).map {case (res) =>
      Redirect(routes.UserController.profileById(request.session.get("userId").getOrElse("0").toLong))
    }
  }

// update required
  def getFinish(id: Long) = Action.async { implicit request =>
    repo.finishById(id).map {case (res) =>
      Redirect(routes.UserController.profileById(request.session.get("userId").getOrElse("0").toLong))
    }
  }

// update required
  def getCancel(id: Long) = Action.async { implicit request =>
    repo.cancelById(id).map {case (res) =>
      Redirect(routes.UserController.profileById(request.session.get("userId").getOrElse("0").toLong))
    }
  }


  def getUserNamesMap(id: Long): Map[String, String] = {
    Await.result(repoVete.getById(id).map{ case (res1) => 
      val cache = collection.mutable.Map[String, String]()
      res1.foreach { user => 
        cache put (user.id.toString, user.nombre)
      }
      
      cache.toMap
    }, 3000.millis)
  }

  def getInsumoUserNamesMap(): Map[String, String] = {
    Await.result(repoInsUser.listInsumoUsers().map{ case (res1) => 
      val cache = collection.mutable.Map[String, String]()
      res1.foreach { user => 
        cache put (user.id.toString, user.nombre)
      }
      
      cache.toMap
    }, 3000.millis)
  }

  def getmodulesNamesMap(): Map[String, String] = {
    Await.result(repoModule.list().map{ case (res1) => 
      val cache = collection.mutable.Map[String, String]()
      res1.foreach { user => 
        cache put (user.id.toString, user.name)
      }
      
      cache.toMap
    }, 3000.millis)
  }

  // delete required
  def delete(id: Long) = Action.async {
    repo.delete(id).map { res =>
      Ok(views.html.productRequestByInsumo_index(Map[String, String](), Map[String, String]()))
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
        Future.successful(Ok(views.html.productRequestByInsumo_update(errorForm, Map[String, String](), Map[String, String]())))
      },
      res => {
        repo.update(res.id, res.date, res.user, res.module, modules(res.module.toString), res.status, res.detail, "insumo").map { _ =>
          Redirect(routes.UserController.profile())
        }
      }
    )
  }
}

case class CreateProductRequestByInsumoForm(date: String, user: Long, module: Long, status: String, detail: String)

case class UpdateProductRequestByInsumoForm(id: Long, date: String, user: Long, module: Long, status: String, detail: String)
