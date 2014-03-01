package com.example.edmtranslator

import com.example.edmtranslator.dictionary.DatabaseDictionary
import scala.slick.driver.H2Driver.simple._


trait Config {

  val database: Database

  lazy val dictionary = new DatabaseDictionary(this)

  lazy val translator = new EdmTranslator(this)
}
