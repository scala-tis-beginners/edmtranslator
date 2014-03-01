package com.example.edmtranslator

import scala.slick.driver.H2Driver.simple._

/**
 * Created by kazuki on 2014/02/21.
 */
trait TestConfig extends Config {

  // sbt でテストするとエラーになるので。。。
  // java.sql.SQLException: No suitable driver found for...
  Class.forName("org.h2.Driver")

  override val database = Database.forURL("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1;INIT=CREATE SCHEMA IF NOT EXISTS LABO")

}
