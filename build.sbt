organization := "org.adelio"

name := "json4s-path"

version := "1.0"

libraryDependencies ++= Seq(
  "org.json4s"     %%     "json4s-jackson"     %     "3.2.7",
  "org.specs2"     %%     "specs2"             %     "1.13"  % "test"
)