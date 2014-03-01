package com.example.edmtranslator.dictionary

import com.example.edmtranslator.Tables._
import scala.slick.driver.H2Driver.simple._

/**
 * Created by kazuki on 2014/02/16.
 */
class DatabaseDictionary(env: {

  /**
   * DB接続
   */
  val database: Database

}) extends Dictionary {

  override def find(from: String): Option[String] = {
    env.database withSession { implicit session =>
      val words = Dictionary.filter(_.columnJa === from).list

      if (words.isEmpty) None else words.head.columnEn
    }
  }

  override def translate(text: String): Either[NotCompletelyTranslationException, List[String]] = {

    val words = env.database withSession { implicit session =>
      Dictionary.list().sortWith { (l,r) => l.columnJa.length > r.columnJa.length }
    }

    def trans(str: String): Either[NotCompletelyTranslationException, List[String]] = str match {

      case "" => Right(Nil)

      case s: String =>
        for (word <- words if s.indexOf(word.columnJa) == 0) {

          val transTo = word.columnEn.getOrElse("")

          return trans(s.substring(word.columnJa.length, s.length)) match {
            case Right(transWords) =>
              Right(transTo :: transWords)
            case Left(NotCompletelyTranslationException(transWords)) =>
              Left(NotCompletelyTranslationException(transTo :: transWords))
          }
        }
        Left(NotCompletelyTranslationException(str :: Nil))
    }
    trans(text)
  }
}
