package com.example.edmtranslator.ui

import java.io._
import scopt.OptionParser
import com.example.edmtranslator
import scala.slick.driver.JdbcDriver.simple._
import scala.xml.XML
import scala.xml.dtd.{PublicID, DocType}
import org.xml.sax.ext.DefaultHandler2
import javax.xml.parsers.SAXParserFactory
import java.nio.charset.Charset

/**
 * Created by kazuki on 2014/02/21.
 */
object CUI {

  case class CommandOption(
                     database: String = null,
                     charset: Charset = Charset.forName("UTF-8"),
                     input: Option[File] = None,
                     output: Option[File] = None)

  object optParser extends OptionParser[CommandOption]("edmtr") {

    head("edmtr 0.1")

    opt[String]('d', "database").required()
      .valueName("<jdbc connection>")
      .text("jdbc connection string (support H2 only)")
      .action { (x, cfg) => cfg.copy(database = x) }

    opt[File]('o', "output").optional()
      .valueName("<file>")
      .text("output file name")
      .action { (x, cfg) => cfg.copy(output = Some(x)) }

    opt[String]('c', "charset").optional()
      .valueName("<charset>")
      .text(
        s"""charset to read and write (default: UTF-8)
          |${Charset.availableCharsets().values()}
        """.stripMargin)
      .action { (x, cfg) => cfg.copy(charset = Charset.forName(x)) }
      .validate( x => if (Charset.isSupported(x)) success else failure(s"$x is not supported charset."))

    arg[File]("<file>").optional()
      .text("input file name")
      .action { (x, cfg) => cfg.copy(input = Some(x)) }
  }

  def main(args: Array[String]): Unit = {
    optParser.parse(args, CommandOption()) map { option =>

      object appConfig extends edmtranslator.Config {
        override val database = Database.forURL(option.database)
      }

      val bufferSize = 1024
      val input = new BufferedReader(new InputStreamReader((option.input) match {
        case Some(input) => new FileInputStream(input)
        case _ => System.in
      }, option.charset), bufferSize)

      val output = new BufferedWriter(new OutputStreamWriter((option.output) match {
        case Some(output) => new FileOutputStream(output)
        case _ => System.out
      }, option.charset), bufferSize)


      // DocType を取得できるようにハンドラを登録したパーサーを使う
      val parser = SAXParserFactory.newInstance().newSAXParser()
      object doctypeHandler extends DefaultHandler2 {
        var doctype: Option[DocType] = None
        override def startDTD(name: String, publicId: String, systemId: String) = {
          doctype = Some(DocType(name, PublicID(publicId, systemId), Nil))
        }
      }
      parser.setProperty("http://xml.org/sax/properties/lexical-handler", doctypeHandler)

      val translatedXml = using(input) { i =>
        appConfig.translator.translate(XML.withSAXParser(parser).load(i))
      }

      translatedXml.foreach { xml =>
        using(output) { o =>
          XML.write(output, xml, option.charset.name(), true, doctypeHandler.doctype.orNull)
        }
      }

    } getOrElse {
      // do nothing.
    }
  }

  /**
   * リソースを確実にクローズして結果を返す
   *
   * @param resource リソース
   * @param f リソースを使う処理
   * @return 処理結果
   */
  def using[A <: {def close()},B](resource:A)(f:A => B):Option[B] =
    try{
      Some( f(resource) )
    }catch{
      case e:Exception => e.printStackTrace
        None
    }finally{
      if(resource != null) resource.close()
    }

}
