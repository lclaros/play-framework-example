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
import play.api.data.format.Formats._ 
import be.objectify.deadbolt.scala.DeadboltActions
import security.MyDeadboltHandler

class ProductController @Inject() (repo: ProductRepository, repoProdInv: ProductInvRepository, 
                                   repoUnit: MeasureRepository, val messagesApi: MessagesApi)
                                 (implicit ec: ExecutionContext) extends Controller with I18nSupport{

  val newForm: Form[CreateProductForm] = Form {
    mapping(
      "name" -> nonEmptyText,
      "cost" -> of[Double],
      "percent" -> of[Double],
      "description" -> text,
      "measureId" -> longNumber,
      "currentAmount" -> number
    )(CreateProductForm.apply)(CreateProductForm.unapply)
  }

  var unidades = getMeasureMap()
  def getMeasureMap(): Map[String, String] = {
    Await.result(repoUnit.getListNames().map{ case (res1) => 
      val cache = collection.mutable.Map[String, String]()
      res1.foreach{ case (key: Long, value: String) => 
        cache put (key.toString(), value)
      }
      
      cache.toMap
    }, 3000.millis)
  }

  def addGet = Action { implicit request =>
    unidades = getMeasureMap()
    Ok(views.html.product_add(new MyDeadboltHandler, newForm, unidades))
  }

  def index = Action.async { implicit request =>
    repo.list().map { res =>
      Ok(views.html.product_index(new MyDeadboltHandler, res))
    }
  }

  def list = Action {
    Ok(views.html.product_list())
  }

  def addProduct = Action.async { implicit request =>
    newForm.bindFromRequest.fold(
      errorForm => {
        Future.successful(Ok(views.html.product_add(new MyDeadboltHandler, errorForm, unidades)))
      },
      res => {
        repo.create(
                      res.name, res.cost, res.percent,res.cost + res.cost * res.percent, res.description,
                      res.measureId, unidades(res.measureId.toString),
                      res.currentAmount
                    ).map { resNew =>
          Redirect(routes.ProductController.show(resNew.id))
        }
      }
    )
  }

  def getProducts = Action.async {
  	repo.list().map { insumos =>
      Ok(Json.toJson(insumos))
    }
  }

  // update required
  val updateForm: Form[UpdateProductForm] = Form {
    mapping(
      "id" -> longNumber,
      "name" -> nonEmptyText,
      "cost" -> of[Double],
      "percent" -> of[Double],
      "price" -> of[Double],
      "description" -> text,
      "measureId" -> longNumber,
      "currentAmount" -> number
    )(UpdateProductForm.apply)(UpdateProductForm.unapply)
  }

  def getChildren(id: Long): Seq[ProductInv] = {
    Await.result(repoProdInv.listByProductId(id).map { res => 
      res
      } , 3000.millis)
  }

  // to copy
  def show(id: Long) = Action.async { implicit request =>
    val children = getChildren(id)
    repo.getById(id).map { res =>
      Ok(views.html.product_show(new MyDeadboltHandler, res(0), children))
    }
  }


  var updatedRow: Product = _

  // update required
  def getUpdate(id: Long) = Action.async { implicit request =>
    repo.getById(id).map { res =>
      updatedRow = res(0)
      val anyData = Map(
                        "id" -> id.toString().toString(),
                        "name" -> updatedRow.name,
                        "cost" -> updatedRow.cost.toString(),
                        "percent" -> updatedRow.percent.toString(),
                        "price" -> updatedRow.price.toString(),
                        "description" -> updatedRow.description,
                        "measureId" -> updatedRow.measureId.toString(),
                        "measureName" -> updatedRow.measureName.toString(),
                        "currentAmount" -> updatedRow.currentAmount.toString()
                        )
      Ok(views.html.product_update(new MyDeadboltHandler, updatedRow, updateForm.bind(anyData), unidades))
    }
  }

  // delete required
  def delete(id: Long) = Action.async {
    repo.delete(id).map { res =>
      Redirect(routes.ProductController.index)
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
        Future.successful(Ok(views.html.product_update(new MyDeadboltHandler, updatedRow, errorForm, unidades)))
      },
      res => {
        repo.update(
                      res.id, res.name, res.cost, res.percent, res.price,
                      res.description, res.measureId, unidades(res.measureId.toString),
                      res.currentAmount
                    ).map { _ =>
          Redirect(routes.ProductController.show(res.id))
        }
      }
    )
  }

  def upload(id: Long) = Action(parse.multipartFormData) { request =>
    request.body.file("picture").map { picture =>
      import java.io.File
      val filename = picture.filename;
      val type1 = filename.substring(filename.length - 4);
      val contentType = picture.contentType
      val fileNewName = id.toString() + "_product" + type1
      val path_1 = "/home/llll/Desktop/projects/isystem/public/images/"
      try { 
        new File(s"$path_1$fileNewName").delete()
      } catch {
        case e: Exception => println(e)
      }
      picture.ref.moveTo(new File(s"$path_1$fileNewName"))
      Redirect(routes.ProductController.show(id))
    }.getOrElse {
      Redirect(routes.ProductController.show(id)).flashing(
        "error" -> "Missing file")
    }
  }

}

case class CreateProductForm(
                              name: String, cost: Double, percent: Double,
                              description: String, measureId: Long, currentAmount: Int
                            )

case class UpdateProductForm(
                              id: Long, name: String, cost: Double,
                              percent: Double, price: Double, description: String,
                              measureId: Long, currentAmount: Int
                            )