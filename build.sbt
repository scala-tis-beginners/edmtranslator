name := "edmtanslator"

version := "1.0"

scalaVersion := "2.10.3"

libraryDependencies ++= Seq(
  "com.h2database" % "h2" % "1.3.175"
  ,"com.typesafe.slick" %% "slick" % "2.0.0"
  ,"org.scalatest" %% "scalatest" % "2.0" % "test"
)

tableGenSettings := GenTableSettings(
  slickDriver = "scala.slick.driver.H2Driver"
  ,jdbcDriver = "org.h2.Driver"
  ,url = "jdbc:h2:mem:test;INIT=runscript from 'src/main/sql/ddl.sql'"
  ,catalog = ""
  ,schema = "LABO"
  ,pkg = "com.example.edmtranslator"
)