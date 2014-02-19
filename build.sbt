organization := "com.github.migralito"

name := "json4s-path"

publishMavenStyle := true

publishTo := {
  if (isSnapshot.value)
    Some("Sonatype Snapshots Nexus" at "https://oss.sonatype.org/content/repositories/snapshots")
  else
    Some("Sonatype Snapshots Nexus" at "https://oss.sonatype.org/service/local/staging/deploy/maven2")
}

libraryDependencies ++= Seq(
  "org.json4s"     %%     "json4s-jackson"     %     "3.2.7",
  "org.specs2"     %%     "specs2"             %     "1.13"  % "test"
)

releaseSettings

licenses := Seq("Apache License, Version 2.0" -> url("http://www.apache.org/licenses/LICENSE-2.0"))

homepage := Some(url("https://github.com/migralito/json4s-path"))

pomExtra := (
    <scm>
      <url>https://github.com/migralito/json4s-path.git</url>
      <connection>scm:git:https://github.com/migralito/json4s-path.git</connection>
    </scm>
    <developers>
      <developer>
        <id>migralito</id>
        <name>Alejandro De Lio</name>
        <url>https://github.com/migralito</url>
      </developer>
    </developers>)