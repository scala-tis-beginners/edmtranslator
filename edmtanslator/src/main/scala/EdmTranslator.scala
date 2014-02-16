import dictionary.Dictionary
import scala.xml._
import scala.xml.transform._

/**
 * EDMファイルで定義されたエンティティの物理名の翻訳変換を行います。
 * @param dictionary  翻訳に使用する [[Dictionary]]
 */
class EdmTranslator(dictionary: Dictionary) {

  /**
   * エンティティの物理名を変換する[[RewriteRule]]です。
   */
    object EntityPhysicalNameRewriteRule extends RewriteRule {

      /**
       * '''P-NAME'''属性を翻訳します。
       * 翻訳できない場合は素の文言のままにします。
       */
      def translateAttribute(n: Node): MetaData = {
        n.attribute("P-NAME") match {
          case Some(attr)
            => Attribute("P-NAME", Text(dictionary.find(attr.text).getOrElse(attr.text)), Null)
          case None
            => Null
        }
      }

      override def transform(xml: Node): Seq[Node] = xml match {
        case elem @ Elem(_, "ENTITY", attributes, _, _*)
          => elem.asInstanceOf[Elem] % attributes.append(translateAttribute(elem))
        case elem @ Elem(_, "ATTR", attributes, _, _*)
          => elem.asInstanceOf[Elem] % attributes.append(translateAttribute(elem))
        case other => other
      }
    }

  /**
   * EDMファイルの[[Node]]を翻訳変換します。
   * @param xml EDMファイルの[[Node]]
   * @return エンティティの物理名が翻訳された[[Node]]
   */
  def translate(xml :Node): Node = {
    new RuleTransformer(EntityPhysicalNameRewriteRule).transform(xml).head
  }
}
