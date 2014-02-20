package com.example.edmtranslator.dictionary

import com.example.edmtranslator.Tables._
import scala.slick.driver.JdbcDriver.simple._

/**
 * Created by kazuki on 2014/02/16.
 */
class DatabaseDictionary extends Dictionary {

  override def find(from: String): Option[String] = {
    Database.forURL("jdbc:h2:mem;INIT=runscript from '/Users/kazuki/IdeaProjects/edmtranslator/edmtanslator/src/test/resources/data/ddl.sql'") withSession { implicit session =>
      val words = Dictionary.filter(_.columnJa === from).list

      if (words.isEmpty) None else words.head.columnEn
    }
  }

}
