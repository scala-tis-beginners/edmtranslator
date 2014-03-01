import EdmtranslatorBuild._
import sbtassembly.Plugin._
import sbtassembly.Plugin.AssemblyKeys._

name := "edmtanslator"

version := "1.0"

scalaVersion := "2.10.3"

libraryDependencies ++= Seq(
  "com.h2database" % "h2" % "1.3.175"
  ,"com.typesafe.slick" %% "slick" % "2.0.0"
  ,"org.slf4j" % "slf4j-simple" % "1.7.6"
  ,"com.github.scopt" %% "scopt" % "3.2.0"
  ,"org.scalatest" %% "scalatest" % "2.0" % "test"
)

resolvers += Resolver.sonatypeRepo("public")

tableGenSettings := GenTableSettings(
  slickDriver = "scala.slick.driver.H2Driver"
  ,jdbcDriver = "org.h2.Driver"
  ,url = "jdbc:h2:mem:test;INIT=runscript from 'src/main/sql/ddl.sql'"
  ,catalog = ""
  ,schema = "LABO"
  ,pkg = "com.example.edmtranslator"
)

assemblySettings

jarName in assembly := "edmtr.jar"

mainClass in assembly := Some("com.example.edmtranslator.ui.CUI")