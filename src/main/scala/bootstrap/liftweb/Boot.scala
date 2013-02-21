package bootstrap.liftweb

import net.liftweb._
import util._
import Helpers._

import common._
import http._
import js.JE
import sitemap._
import Loc._
import net.liftmodules.JQueryModule
import net.liftweb.http.js.jquery._


/**
 * A class that's instantiated early and run.  It allows the application
 * to modify lift's environment
 */
class Boot {
  def boot {
    // where to search snippet
    LiftRules.addToPackages("code")

    // Build SiteMap
    val entries = List(
      Menu.i("Home") / "index", // the simple way to declare a menu

      // more complex because this menu allows anything in the
      // /static path to be visible
      Menu(Loc("Static", Link(List("static"), true, "/static/index2"),
	       "Static Content")))

    // set the sitemap.  Note if you don't want access control for
    // each page, just comment this line out.
    LiftRules.setSiteMap(SiteMap(entries:_*))

    //Show the spinny image when an Ajax call starts
    LiftRules.ajaxStart =
      Full(() => LiftRules.jsArtifacts.show("ajax-loader").cmd)
    
    // Make the spinny image go away when it ends
    LiftRules.ajaxEnd =
      Full(() => LiftRules.jsArtifacts.hide("ajax-loader").cmd)

    // Force the request to be UTF-8
    LiftRules.early.append(_.setCharacterEncoding("UTF-8"))

    // Use HTML5 for rendering
    LiftRules.htmlProperties.default.set((r: Req) =>
      new Html5Properties(r.userAgent))

    //Init the jQuery module, see http://liftweb.net/jquery for more information.
    LiftRules.jsArtifacts = JQueryArtifacts
    JQueryModule.InitParam.JQuery=JQueryModule.JQuery172
    JQueryModule.init()


    /**** user experience settings  ****/
    // set the time that notices should be displayed and then fadeout
    LiftRules.noticesAutoFadeOut.default.set((notices: NoticeType.Value) => {
      notices match {
        case NoticeType.Notice => Full(10 seconds, 1 second)
        case NoticeType.Warning => Full(30 seconds, 1 second)
        case NoticeType.Error => Full(30 seconds, 1 second)
        case _ => Empty
      }
    })


    val jsNotice =
      """$('#lift__noticesContainer___notice')
        |.addClass("alert-box success")
        |.append('<a href="" class="close">&times;</a>')""".stripMargin

    val jsWarning =
      """$('#lift__noticesContainer___warning')
        |.addClass("alert-box")
        |.append('<a href="" class="close">&times;</a>')""".stripMargin

    val jsError =
      """$('#lift__noticesContainer___error')
        |.addClass("alert-box alert")
        |.append('<a href="" class="close">&times;</a>')""".stripMargin


    LiftRules.noticesEffects.default.set(
      (notice: Box[NoticeType.Value], id: String) => {
        val js = notice.map( _.title) match{
          case Full("Notice")   => Full(JE.JsRaw( jsNotice ).cmd)
          case Full("Warning")  => Full(JE.JsRaw( jsWarning ).cmd)
          case Full("Error")    => Full(JE.JsRaw( jsError ).cmd)
          case _                => Full(JE.JsRaw( jsNotice ).cmd)
        }
        js
      })



  }
}
