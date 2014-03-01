edmtranslator
=============
edmファイル内の物理名を日本語から英語に翻訳するツールです。
辞書はデータベースに格納した日英対応表を使います。(現在サポートしているデータベースは H2 のみです）

ビルド方法
----------
sbt で assembly すると target ディレクトリに edmtr.jar がビルドされます。

  sbt assembly
  
実行方法
--------
DBへの接続文字列と出力ファイル名、入力ファイル名を指定して実行します。

  java -jar edmtr.jar -d jdbc:h2:sample -o output.edm input.edm
  
標準入力、標準出力も使えます。

  cat input.edm | java -jar edmtr.jar -d jdbc:h2:sample > output.edm
  
/src/main/resources/sample にサンプルのデータベースと入力ファイルがあります。
