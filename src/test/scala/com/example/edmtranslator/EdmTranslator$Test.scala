package com.example.edmtranslator

import com.example.edmtranslator.Tables._
import org.scalatest._
import scala.xml.Utility.trim

import scala.slick.driver.JdbcDriver.simple._
import Database.dynamicSession

/**
 * Created by kazuki on 2014/02/16.
 */
class EdmTranslator$Test extends FunSuite with BeforeAndAfter with Matchers with TestConfig {


  before {
    database withDynSession Tables.Dictionary.ddl.create
  }

  after {
    database withDynSession Tables.Dictionary.ddl.drop
  }


  test("エンティティとその属性の物理名が辞書で定義された文言に翻訳される") {

    database withDynSession {
      Dictionary ++= Seq(
        DictionaryRow("テスト用エンティティ", Some("entity-for-test")),
        DictionaryRow("テスト用属性", Some("attr-for-test"))
      )
    }

    val xml =
      <ERD>
        <ENTITY P-NAME="テスト用エンティティ" >
          <ATTR P-NAME="テスト用属性" />
        </ENTITY>
      </ERD>

    val output = translator.translate(xml)

    val expect =
      <ERD>
        <ENTITY P-NAME="entity-for-test" >
          <ATTR P-NAME="attr-for-test" />
        </ENTITY>
      </ERD>

    assert(trim(output) == trim(expect))
  }
}
