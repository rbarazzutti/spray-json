package spray.json

import scala.collection.mutable

object JsObjectSafe {
  private def safe(jsValue: JsValue) = jsValue match {
    case jsos: JsObjectSafe ⇒ jsos
    case JsObject(fields) ⇒ JsObjectSafe(fields)
    case a:Any ⇒ a
  }
}

import spray.json.JsObjectSafe._

case class JsObjectSafe(override val fields: Map[String, JsValue]) extends JsObject(fields) {
  private val accessedFields = mutable.HashSet.empty[String]

  override def getFields(fieldNames: String*) = {
    accessedFields ++= fieldNames
    super.getFields(fieldNames: _*).map(safe)
  }

  def unaccessedFields = fields -- accessedFields
}
