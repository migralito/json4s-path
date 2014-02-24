package org.adelio.json4spath

import scala.{BigDecimal => BigDec}
import scala.annotation.tailrec
import org.json4s.JsonAST.{JField, JObject, JArray, JValue}
import scala.util.matching.Regex

/**
 * @author adelio
 * @since 18/02/14
 */
class JsonPath(jValue: JValue) {

  val NonIndexed = """(\w+)""".r          // some_field
  val Indexed    = """(\w+)\[(\d+)]""".r  // some_field[index]
  val JustIndex  = """(\d+)""".r          // just_index

  def  s_\(path: String) = \?[String] (path).get
  def  b_\(path: String) = \?[Boolean](path).get
  def  i_\(path: String) = \?[Int]    (path).get
  def  d_\(path: String) = \?[Double] (path).get
  def bd_\(path: String) = \?[BigDec] (path).get

  def  s_\?(path: String) = \?[String] (path)
  def  b_\?(path: String) = \?[Boolean](path)
  def  i_\?(path: String) = \?[Int]    (path)
  def  d_\?(path: String) = \?[Double] (path)
  def bd_\?(path: String) = \?[BigDec] (path)

  def \?[Out](path: String)(implicit ext: JValueSimpleExtractor[Out]): Option[Out] = {
    @tailrec def diveInto(jv: JValue, remainingPath: List[String]): Option[Out] = remainingPath match {
      case NonIndexed(name) :: Nil => ext.simpleExtract(jv \ name)
      case NonIndexed(name) :: xs  => diveInto(jv \ name, xs)
      case Indexed(name, i) :: xs  => diveInto((jv \ name)(i.toInt), xs)
      case x                       => throw new RuntimeException(s"Invalid path '$path' at '$x'")
    }
    diveInto(jValue, (if (path.startsWith("$.")) path.drop(2) else path) .split('.').toList)
  }


  // //////////////////////////////////////////////
  // MODIFICATION INTERFACE
  // //////////////////////////////////////////////

  def < (newValue: JValue) = JsonPathWithModification(jValue, newValue)

  case class JsonPathWithModification(ast: JValue, replacement: JValue) {
    def into(path: String) = replace(ast, path.split('.').toList, replacement)
  }


  // //////////////////////////////////////////////
  // INTERNALS
  // //////////////////////////////////////////////

  def replace(part: JValue, qualifiedName: List[String], replacement: JValue): JValue =
    qualifiedName match {
      // the beginning of the json path
      case "$" :: xs =>
        replace(part, xs, replacement)

      // the field to change!
      case Nil => replacement

      // current token: "passengers[9]"
      case Indexed(fn, idx) :: xs => replace(part, fn :: idx :: xs, replacement)

      // current token: "9"
      case JustIndex(idx) :: xs =>
        val ja      = part.asInstanceOf[JArray]
        val vec     = ja.arr.toVector
        val i       = idx.toInt
        val updated = vec.updated(i, replace(vec(i), xs, replacement))
        ja.copy(arr = updated.toList)

      // current token: "passengers" as a field that holds ANY JValue (an object, an array or a simple value)
      case NonIndexed(fn) :: xs =>
        val jo = part.asInstanceOf[JObject]
        jo.copy(obj = jo.obj.map {
          case JField(`fn`, jv) => JField(`fn`, replace(jv, xs, replacement))
          case field            => field
        })
    }

}

object JsonPath {
  implicit def enrich(ast: JValue) = new JsonPath(ast)
}
