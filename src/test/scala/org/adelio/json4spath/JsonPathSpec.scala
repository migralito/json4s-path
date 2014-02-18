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
                |        "some": "some",
                |        "population": 35,
                |        "language": "es",
                |        "is_great_city": true,
                |        "score": 9.7777777777777777777797777777777777777777779777777777777777779,
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
   val big = parse(json, true)


   // //////////////////////////////////////////////
   // TESTS
   // //////////////////////////////////////////////

   "JsonPath" should {

     "find simple paths" in {
       val `null`    = ast s_\? "buenos_aires.null"
       val some      = ast s_\? "buenos_aires.some"
       val pop       = ast i_\ "buenos_aires.population"
       val language  = ast s_\ "buenos_aires.language"
       val greatCity = ast b_\ "buenos_aires.is_great_city"
       val score     = ast d_\ "buenos_aires.score"
       val realScore = big bd_\ "buenos_aires.score"

       (ast s_\? "buenos_aires.asdf") === None
       (ast s_\? "buenos_aires.null") === None
       (ast s_\? "buenos_aires.some") === Some("some")

       pop       === 35
       language  === "es"
       greatCity === true
       score     === 9.777777777777777777779
       realScore === BigDecimal("9.7777777777777777777797777777777777777777779777777777777777779", MathContext.UNLIMITED)
     }

     "be able to go inside arrays" in {
       val eze = ast s_\ "buenos_aires.airports[0].id"
       val aep = ast s_\ "buenos_aires.airports[1].id"

       eze === "EZE"
       aep === "AEP"
     }
   }

 }
