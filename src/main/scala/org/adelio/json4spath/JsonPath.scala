package org.adelio.json4spath

import scala.{BigDecimal => BigDec}
import scala.annotation.tailrec
import org.json4s.JsonAST.JValue

/**
 * @author adelio
 * @since 18/02/14
 */
class JsonPath(jValue: JValue) {

  val NonIndexed = """(\w+)""".r          // some_field
  val Indexed    = """(\w+)\[(\d+)]""".r  // some_field[index]

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
}

object JsonPath {
  implicit def enrich(ast: JValue) = new JsonPath(ast)
}
