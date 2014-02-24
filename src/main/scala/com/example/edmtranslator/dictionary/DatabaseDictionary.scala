package com.example.edmtranslator.dictionary

import com.example.edmtranslator.Tables._
import scala.slick.driver.JdbcDriver.simple._
import scala.slick.driver.JdbcDriver.backend.DatabaseDef

/**
 * Created by kazuki on 2014/02/16.
 */
class DatabaseDictionary(env: {

  /**
   * DB接続
   */
  val database: DatabaseDef

}) extends Dictionary {

  override def find(from: String): Option[String] = {
    env.database withSession { implicit session =>
      val words = Dictionary.filter(_.columnJa === from).list

      if (words.isEmpty) None else words.head.columnEn
    }
  }

}
