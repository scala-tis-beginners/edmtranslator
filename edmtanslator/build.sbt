name := "edmtanslator"

version := "1.0"

libraryDependencies ++= Seq(
  "com.h2database" % "h2" % "1.3.175"
  ,"com.typesafe.slick" %% "slick" % "2.0.0"
  ,"org.scalatest" %% "scalatest" % "2.0" % "test"
)
