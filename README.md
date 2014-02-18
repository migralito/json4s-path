json4s-path
===========

JsonPath for Json4s



How to use
==========

import JsonPath._

val ast = parse(strJson)

val name = ast s_\ "$.object.array[4].name"      // String value
val age  = ast i_\ "$.object.array[4].age"       // Integer value
val tall = ast b_\ "$.object.array[4].am_i_tall" // Boolean value

val maybeChildren = ast i_\? "$.object.array[4].childrenCount" // Option[Integer] value
