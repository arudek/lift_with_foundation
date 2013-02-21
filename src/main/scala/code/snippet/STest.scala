package code.snippet

import net.liftweb.common.Box
import java.util.Date
import code.lib.DependencyFactory
import net.liftweb.util.Helpers._
import net.liftweb.http.{S, SHtml}
import net.liftweb.http.js.JsCmds.SetHtml

/**
 * Created with IntelliJ IDEA.
 * User: arne
 * Date: 21.02.13
 * Time: 16:06
 * To change this template use File | Settings | File Templates.
 */
class STest {

  def machWas = {
    S.error("Fehler! huhu...")
    S.error("Fehler! 2")
    S.notice("Notice")
    S.warning("warning")
    S.warning("warning2")
    SetHtml("result", <p>hihi! Ds ist neu...</p>)
  }

  def button = "*" #> SHtml.ajaxButton("huhu",() => machWas)
  def machwas = "*" #> <p>Das hier kommt vom Snippet!</p>
}
