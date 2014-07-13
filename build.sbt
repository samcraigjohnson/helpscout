name := """similar"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.11.1"

libraryDependencies ++= Seq(
  javaJdbc,
  javaEbean,
  cache,
  javaWs
)

libraryDependencies += "mysql" % "mysql-connector-java" % "5.1.18"

libraryDependencies ++= Seq(
	"org.webjars" % "foundation" % "5.3.0",
	"org.webjars" % "jquery" % "1.11.1"
)

libraryDependencies += "com.googlecode.json-simple" % "json-simple" % "1.1.1"