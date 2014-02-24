package org.adelio

import org.json4s.JsonAST.{JNothing, JValue}

/**
 * @author adelio
 * @since 18/02/14
 */
package object json4spath {

  trait JValueSimpleExtractor[Out] {
    def translateValue: (JValue#Values) => Out = _.asInstanceOf[Out]
    def simpleExtract(jv: JValue): Option[Out] =
      Option(jv.values).flatMap(v => if (v == None) None else Some(v)).map(translateValue)
  }

  implicit def stringExtractor  = new JValueSimpleExtractor[String] {}
  implicit def booleanExtractor = new JValueSimpleExtractor[Boolean] {}
  implicit def doubleExtractor  = new JValueSimpleExtractor[Double] {}
  implicit def bigdecExtractor  = new JValueSimpleExtractor[BigDecimal] {}
  implicit def intExtractor     = new JValueSimpleExtractor[Int] {
    override def translateValue = _.asInstanceOf[BigInt].toInt
  }
  implicit def jvalueExtractor  = new JValueSimpleExtractor[JValue] {
    override def translateValue: (JValue#Values) => JValue = ???
    override def simpleExtract(jv: JValue): Option[JValue] = if (jv == JNothing) None else Some(jv)
  }

}