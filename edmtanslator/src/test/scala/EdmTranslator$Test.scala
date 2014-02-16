import org.scalatest._
import scala.xml.XML

/**
 * Created by kazuki on 2014/02/16.
 */
class EdmTranslator$Test extends FunSuite with Matchers {

  test("入力したEDMファイルが翻訳される") {
    val translator = new EdmTranslator()

    val input = XML.load(getClass.getResource("/data/input.edm"))
    val output = translator.translate(input)

    val expect = XML.load(getClass.getResource("/data/expected.edm"))

    assert(output == expect)
  }
}
