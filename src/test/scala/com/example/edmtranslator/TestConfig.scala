package com.example.edmtranslator

import scala.slick.driver.JdbcDriver.simple._
import com.example.edmtranslator.Tables._

/**
 * Created by kazuki on 2014/02/21.
 */
trait TestConfig extends Config {

  override lazy val database = Database.forURL("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1;INIT=CREATE SCHEMA IF NOT EXISTS LABO")

}
