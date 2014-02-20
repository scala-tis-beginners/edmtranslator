import com.example.edmtranslator.dictionary.Dictionary
import org.scalatest._
import scala.xml.XML
import scala.xml.Utility.trim;

/**
 * Created by kazuki on 2014/02/16.
 */
class EdmTranslator$Test extends FunSuite with Matchers {


  test("エンティティとその属性の物理名が辞書で定義された文言に翻訳される") {

    object dictionaryMock extends Dictionary {
      override def find(word: String): Option[String] = word match {
        case "テスト用エンティティ" => Some("entity-for-test")
        case "テスト用属性" => Some("attr-for-test")
      }
    }

    val translator = new EdmTranslator(dictionaryMock)

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
