package code.snippet


import net.liftweb.util.BindPlus._
import net.liftweb.util._
import code.model.User
import Helpers._
import net.liftweb.common.{Full, Empty, Box}
import code.model.CodeSnippet
import net.liftweb.common.Failure

class UserSnippet {
  
	def UserName = User.currentUser match {
	  case Full(currUser) => currUser.username.get
	  case Empty =>  ""
	  case Failure(msg,_,_) => "msg"
	  case _=>	""
	}
	
  	def UserIcon = User.currentUser match {
	  case Full(currUser) => currUser.iconURL.get
	  case Empty =>  ""
	  case Failure(msg,_,_) => "msg"
	  case _=>	""
	}
	def CurrentUserName = "#userName" #> UserName
	
	def CurrentUserIcon = "#userIcon [src]" #> UserIcon
	
}