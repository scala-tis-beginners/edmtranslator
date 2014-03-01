package com.example.edmtranslator.ui

import java.io._
import scopt.OptionParser
import com.example.edmtranslator
import scala.slick.driver.JdbcDriver.simple._
import scala.xml.XML
import scala.xml.dtd.{PublicID, DocType}

/**
 * Created by kazuki on 2014/02/21.
 */
object CUI {

  case class Config(database: String = "", input: Option[File] = None, output: Option[File] = None)

  object parser extends OptionParser[Config]("edmtr") {

    head("edmtr 0.1")

    opt[String]('d', "database").required()
      .valueName("<jdbc connection>")
      .text("jdbc connection string (support H2 only)")
      .action { (x, cfg) => cfg.copy(database = x) }


    opt[File]('o', "output").optional()
      .valueName("<file>")
      .text("output file name")
      .action { (x, cfg) => cfg.copy(output = Some(x)) }

    arg[File]("<file>").optional()
      .text("input file name")
      .action { (x, cfg) => cfg.copy(input = Some(x)) }
  }

  def main(args: Array[String]) = {
    parser.parse(args, Config()) map { config =>

      object appConfig extends edmtranslator.Config {
        override val database = Database.forURL(config.database)
      }

      val input: InputStream = (config.input) match {
        case Some(input) => new FileInputStream(input)
        case _ => System.in
      }
      val output: OutputStream = (config.output) match {
        case Some(output) => new FileOutputStream(output)
        case _ => System.out
      }

      val translatedXml = appConfig.translator.translate(XML.load(input))

      val doctype = DocType("xml", PublicID("ERD", null), Nil)
      XML.write(new OutputStreamWriter(output, "UTF-8"), translatedXml, "UTF-8", false, doctype)

    } getOrElse {
    }
  }

}
