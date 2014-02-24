package com.example.edmtranslator

import com.example.edmtranslator.dictionary.DatabaseDictionary
import scala.slick.driver.JdbcDriver.simple._


trait Config {

  lazy val database = Database.forURL("jdbc:h2:mem")

  lazy val dictionary = new DatabaseDictionary(this)

  lazy val translator = new EdmTranslator(this)
}
