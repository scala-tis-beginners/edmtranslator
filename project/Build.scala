import sbt._
import Keys._


object EdmtranslatorBuild extends Build {

  lazy val root = Project(
    id = "main"
    ,base = file(".")
    ,settings = Project.defaultSettings ++ Seq(
      slick <<= slickCodeGenTask
      ,sourceGenerators in Compile <+= slickCodeGenTask
    )
  ) aggregate(codeGen) dependsOn(codeGen)

  lazy val codeGen = Project(
    id = "codeGen"
    ,base = file("edmtranslator-gen")
  )

  // code generation task
  lazy val slick = TaskKey[Seq[File]]("gen-tables")

  case class GenTableSettings(slickDriver: String, jdbcDriver: String, url: String, catalog: String, schema: String, pkg: String)
  lazy val tableGenSettings = SettingKey[GenTableSettings]("table-gen-settings")

  lazy val slickCodeGenTask =
    (scalaSource in Compile, dependencyClasspath in Compile, runner in Compile, streams, tableGenSettings) map {
      (dir, classPath, runner, streams, settings) =>

    // place generated files in sbt's managed sources folder
    val outputDir = dir.getPath

    toError(
      runner.run("com.example.edmtranslator.codegen.TableGen"
        ,classPath.files
        ,Array(settings.slickDriver, settings.jdbcDriver, settings.url, settings.catalog, settings.schema, outputDir, settings.pkg)
        ,streams.log)
    )

    Seq(file(outputDir + "/Tables.scala"))
  }
}