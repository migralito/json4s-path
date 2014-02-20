organization := "com.github.migralito"

name := "json4s-path"

profileName := "com.github.migralito" // required by plugin sbt-sonatype

// publishMavenStyle := true // TODO: remove. This is supposed to be needed for release promotion in sonatype. sbt-sonatype would take care of this

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

licenses := Seq("Apache 2" -> url("http://www.apache.org/licenses/LICENSE-2.0")) // required for release promotion

homepage := Some(url("https://github.com/migralito/json4s-path"))  // required for release promotion

pomExtra := ( // required for release promotion
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

publish := { // bind (sbt-pgp) signing and (sbt-sonatype) promotion processes to publish task, so sbt-release can use it
  if (isSnapshot.value) {
    PgpKeys.publishSigned.value
  } else {
    PgpKeys.publishSigned.value
    sonatypeRelease.value
  }
}