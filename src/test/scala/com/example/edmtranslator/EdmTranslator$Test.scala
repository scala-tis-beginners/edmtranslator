package com.example.edmtranslator

import com.example.edmtranslator.Tables._
import org.scalatest._
import scala.xml.Utility.trim

import scala.slick.driver.H2Driver.simple._
import Database.dynamicSession
import com.example.edmtranslator.Tables.DictionaryRow
import scala.Some

/**
 * Created by kazuki on 2014/02/16.
 */
class EdmTranslator$Test extends FunSuite with BeforeAndAfter with Matchers with TestConfig {


  before {
    database withDynSession Dictionary.ddl.create
  }

  after {
    database withDynSession Dictionary.ddl.drop
  }


  test("エンティティとその属性の物理名が辞書で定義された文言に翻訳される") {

    database withDynSession {
      Dictionary ++= Seq(
        DictionaryRow("プロジェクト", Some("project")),
        DictionaryRow("ＩＤ", Some("id")),
        DictionaryRow("メンバー", Some("member"))
      )
    }

    val xml =
      <ERD>
        <ENTITY P-NAME="プロジェクト" >
          <ATTR P-NAME="プロジェクトＩＤ" />
          <INDEX P-NAME="プロジェクトＩＤ" >
          </INDEX>
        </ENTITY>
        <RELATION P-NAME="FK_プロジェクト_メンバー" >
        </RELATION>
      </ERD>

    val output = translator.translate(xml)

    val expect =
      <ERD>
        <ENTITY P-NAME="project" >
          <ATTR P-NAME="project_id" />
          <INDEX P-NAME="project_id" >
          </INDEX>
        </ENTITY>
        <RELATION P-NAME="FK_project_member" >
        </RELATION>
      </ERD>

    assert(trim(output) == trim(expect))
  }
}
