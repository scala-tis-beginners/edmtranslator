package com.example.edmtranslator

import com.example.edmtranslator.dictionary.{NotCompletelyTranslationException, Dictionary}
import scala.xml._
import scala.xml.transform._

/**
 * EDMファイルで定義されたエンティティの物理名の翻訳変換を行います。
 */
class EdmTranslator(env: {

   /**
   *  翻訳に使用する [[Dictionary]]
   */
  val dictionary: Dictionary

}) {

  /**
   * エンティティの物理名を変換する[[RewriteRule]]です。
   */
    object EntityPhysicalNameRewriteRule extends RewriteRule {

      val wordDelimiter = "_"

      /**
       * '''P-NAME'''属性を翻訳します。
       * 翻訳できない場合は素の文言のままにします。
       */
      def translateAttribute(n: Node): MetaData = {
        n.attribute("P-NAME") match {
          case Some(attr) => {

            val words = attr.text split wordDelimiter

            val transedWords = for (word <- words) yield word match {
              case word: String if word.matches("[a-zA-Z]+") => word
              case word: String =>
                env.dictionary.translate(word) match {

                  case Right(words) => words.mkString(wordDelimiter)

                  case Left(NotCompletelyTranslationException(words)) => {

                    System.err.println(s"[Failure] $word => ${words mkString(wordDelimiter)}")

                    words.mkString(wordDelimiter)
                  }
                }
            }
            Attribute("P-NAME", Text(transedWords.mkString(wordDelimiter)), Null)
          }
          case None => Null
        }
      }

      override def transform(xml: Node): Seq[Node] = xml match {
        case elem @ Elem(_, "ENTITY", attributes, _, _*) =>
          elem.asInstanceOf[Elem] % attributes.append(translateAttribute(elem))
        case elem @ Elem(_, "INDEX", attributes, _, _*) =>
          elem.asInstanceOf[Elem] % attributes.append(translateAttribute(elem))
        case elem @ Elem(_, "RELATION", attributes, _, _*) =>
          elem.asInstanceOf[Elem] % attributes.append(translateAttribute(elem))
        case elem @ Elem(_, "ATTR", attributes, _, _*) =>
          elem.asInstanceOf[Elem] % attributes.append(translateAttribute(elem))
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
