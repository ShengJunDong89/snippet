package code.snippet

import scala.xml.NodeSeq
import net.liftweb.util.Helpers._
import net.liftweb.http.SHtml
import net.liftweb.http.js.JsCmds.Noop
import code.model.User
import net.liftweb.http.S
import code.share.SiteConsts
import code.model.CodeSnippet
import scala.io.Source
import net.liftweb.http.LiftRules
import java.io.File
import net.liftweb.http.js.JsCmds.JsCrVar
import net.liftweb.http.js.JsCmds.Run
import net.liftweb.json.Serialization.{read, write}
import code.model.ToCModel._
import net.liftweb.http.js.JE.JsRaw
import scala.xml.Text

class db {

  val user = User.loggedIn_? match {
    case true => User.currentUser.get
    case false => S.redirectTo(SiteConsts.LOGIN_URL)
  }

  val lines = {
    val url = LiftRules.getResource("/books.txt").get
    Source.fromFile(url.getPath()).getLines().toList
  }

  def posts = {
    "li *" #> lines
  }

  def render(xhtml: NodeSeq): NodeSeq = {
    bind("database", xhtml,
      "addposts" -> SHtml.ajaxButton("Add Posts",
        () => {
          for (line <- lines) {
            val post = CodeSnippet.create
            post.Author.set(user.id)
            post.title.set(line.trim)
            println("before save")
            post.save
            println("after save")
          }
          jsUpdatePostCount
        }),
      "deleteposts" -> SHtml.ajaxButton("Delete All Posts",
        () => {
          for (p <- CodeSnippet.findAll())
            p.delete_!
          jsUpdatePostCount
        }),
      "numposts" -> {
        <span id="post_count">Number of Posts in db: {
          CodeSnippet.findAll().length
        }</span>
      },
      "addpost" -> SHtml.ajaxButton("Add Single Post",
//          <input type="button" value="Delete Post"></input>,
//          () => Run("$('#post_id').val()"),
//          (postId:String) => {
          () => {
            val post = CodeSnippet.create
            post.Author.set(user.id)
            post.title("post")
            post.save
            Noop
          }))
  }

  def jsUpdatePostCount = {
    JsCrVar("postCount", CodeSnippet.findAll.length) &
      Run("""
                  $("#post_count").html("Number of Posts in db: " + postCount);
                  """)
  }
}