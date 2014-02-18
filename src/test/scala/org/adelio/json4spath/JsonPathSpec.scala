package org.adelio.json4spath

import java.math.MathContext
import org.specs2.mutable.Specification
import org.json4s.jackson.JsonMethods.parse

import JsonPath._

/**
 * @author adelio
 * @since 17/02/14
 */
class JsonPathSpec extends Specification {

  val json = """{
               |    "buenos_aires": {
               |        "null": null,
               |        "population": 35,
               |        "language": "es",
               |        "is_great_city": true,
               |        "score": 9.777777777777777777779777777777777777777777977777777777777779,
               |        "airports": [
               |            {
               |                "id": "EZE",
               |                "name": "Aeropuerto Internacional Ministro Pistarini"
               |            },
               |            {
               |                "id": "AEP",
               |                "name": "Aeroparque Jorge Newbery"
               |            }
               |        ]
               |    }
               |}""".stripMargin

  val ast = parse(json)
  val astBigDec = parse(json, useBigDecimalForDouble = true)

  val trimmedScore = 9.777777777777777777779
  val actualScore  = BigDecimal("9.777777777777777777779777777777777777777777977777777777777779", MathContext.UNLIMITED)


  // //////////////////////////////////////////////
  // TESTS
  // //////////////////////////////////////////////

  "JsonPath" should {

    "find simple paths" in {
      val pop       = ast i_\  "buenos_aires.population"
      val language  = ast s_\  "buenos_aires.language"
      val greatCity = ast b_\  "buenos_aires.is_great_city"
      val score     = ast d_\  "buenos_aires.score"
      val realScore = astBigDec bd_\ "buenos_aires.score"

      pop       === 35
      language  === "es"
      greatCity === true
      score     === 9.777777777777777777779
      realScore === actualScore
    }

    "search a given path, returning an Option[type]" in {
      (ast       i_\?  "buenos_aires.population")    === Some(35)
      (ast       s_\?  "buenos_aires.language")      === Some("es")
      (ast       b_\?  "buenos_aires.is_great_city") === Some(true)
      (ast       d_\?  "buenos_aires.score")         === Some(9.777777777777777777779)
      (astBigDec bd_\? "buenos_aires.score")         === Some(actualScore)
    }

    "support searching nulled fields" in {
      (ast       s_\?  "buenos_aires.null") === None
      (ast       i_\?  "buenos_aires.null") === None
      (ast       b_\?  "buenos_aires.null") === None
      (ast       d_\?  "buenos_aires.null") === None
      (astBigDec bd_\? "buenos_aires.null") === None
    }

    "support searching inexistent fields" in {
      (ast       s_\?  "buenos_aires.asdf") === None
      (ast       i_\?  "buenos_aires.asdf") === None
      (ast       b_\?  "buenos_aires.asdf") === None
      (ast       d_\?  "buenos_aires.asdf") === None
      (astBigDec bd_\? "buenos_aires.asdf") === None
    }

    "support browsing invalid paths" in {
      (ast       s_\?  "buenos_aires.invalid.path") === None
      (ast       i_\?  "buenos_aires.invalid.path") === None
      (ast       b_\?  "buenos_aires.invalid.path") === None
      (ast       d_\?  "buenos_aires.invalid.path") === None
      (astBigDec bd_\? "buenos_aires.invalid.path") === None

      (ast       s_\?  "buenos_aires.invalid.arrayed[8].path") === None
      (ast       i_\?  "buenos_aires.invalid.arrayed[8].path") === None
      (ast       b_\?  "buenos_aires.invalid.arrayed[8].path") === None
      (ast       d_\?  "buenos_aires.invalid.arrayed[8].path") === None
      (astBigDec bd_\? "buenos_aires.invalid.arrayed[8].path") === None

      (ast       s_\?  "completely.invalid.path") === None
      (ast       i_\?  "completely.invalid.path") === None
      (ast       b_\?  "completely.invalid.path") === None
      (ast       d_\?  "completely.invalid.path") === None
      (astBigDec bd_\? "completely.invalid.path") === None

      (ast       s_\?  "completely.invalid.arrayed[8].path") === None
      (ast       i_\?  "completely.invalid.arrayed[8].path") === None
      (ast       b_\?  "completely.invalid.arrayed[8].path") === None
      (ast       d_\?  "completely.invalid.arrayed[8].path") === None
      (astBigDec bd_\? "completely.invalid.arrayed[8].path") === None
    }

    "be able to go inside arrays" in {
      val eze = ast s_\ "buenos_aires.airports[0].id"
      val aep = ast s_\ "buenos_aires.airports[1].id"

      eze === "EZE"
      aep === "AEP"
    }
  }

}
