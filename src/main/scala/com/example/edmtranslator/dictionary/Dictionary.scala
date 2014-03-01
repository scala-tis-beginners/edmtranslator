package com.example.edmtranslator.dictionary

/**
 * 辞書
 *
 * ある文言を異なる表記の同じ意味を持つ文言に変換します。
 */
trait Dictionary {

  /**
   * 文言を検索します。
   *
   * @param word 検索する文言
   * @return 異なる表記で同じ意味を持つ文言。
   *         この辞書で定義されている文言の場合は [[Some]] を返します。
   *         この辞書で定義されていない文言の場合は [[None]] を返します。
   */
  def find(word: String): Option[String]

  def translate(text: String): Either[NotCompletelyTranslationException, List[String]]
}
