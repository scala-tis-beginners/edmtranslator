package com.example.edmtranslator.codegen

import scala.Some
import scala.slick.driver.JdbcDriver
import scala.slick.model.codegen.SourceCodeGenerator
import scala.slick.jdbc.meta._

/**
 * Created by kazuki on 2014/02/16.
 */
object TableGen {

  def main (args: Array[String]) = {
    println(getClass.getResource("/").getPath)

    val Array(slickDriver, jdbcDriver, url, catalog, schema, outputDir, pkg, _*) = args

    val db = JdbcDriver.simple.Database.forURL(url,driver=jdbcDriver)
    val model = db.withSession{ implicit session =>
      val tables = MTable.getTables(Some(catalog), Some(schema), None, None).list
      createModel(tables, JdbcDriver)
    }
    val codegen = new SourceCodeGenerator(model);
    codegen.writeToFile(slickDriver, outputDir, pkg)
  }
}
