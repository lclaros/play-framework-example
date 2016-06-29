package security

import be.objectify.deadbolt.scala.{DynamicResourceHandler, DeadboltHandler}
import play.api.mvc.{Request, Result, Results}
import be.objectify.deadbolt.core.models.Subject
import scala.concurrent.{ Future, Await }
import models.UserSecurity
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent._
import dal._
import javax.inject._

/**
 *
 * @author Steve Chaloner (steve@objectify.be)
 */
class MyDeadboltHandler (dynamicResourceHandler: Option[DynamicResourceHandler] = None)  extends DeadboltHandler {

  def beforeAuthCheck[A](request: Request[A]) = Future(None)

  override def getDynamicResourceHandler[A](request: Request[A]): Future[Option[DynamicResourceHandler]] = {
    Future(dynamicResourceHandler.orElse(Some(new MyDynamicResourceHandler())))
  }

//<<<<<<< HEAD
  override def getSubject[A](request: Request[A]): Future[Option[Subject]] = {
    // e.g. request.session.get("user")
    println("This is called after ")
    println(request)
    var user1 =  new UserSecurity("steve")
    user1.rol = request.session.get("role").get
    println(user1)
    
    Future(Some(user1))
/*=======
  override def getSubject[A](request: AuthenticatedRequest[A]): Future[Option[Subject]] = {
    // e.g. request.session.get("userSecurity")
    var res1 = "someone"
    request.session.get("userSecurity").map { res => 
      res1 = res
    }

    Future(Some(new UserSecurity(res1)))
>>>>>>> master*/
  }

  def onAuthFailure[A](request: Request[A]): Future[Result] = {
    Future {Results.Forbidden(views.html.accessFailed())}
  }
}