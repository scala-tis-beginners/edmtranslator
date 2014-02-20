package com.example.edmtranslator
// AUTO-GENERATED Slick data model
/** Stand-alone Slick data model for immediate use */
object Tables extends {
  val profile = scala.slick.driver.H2Driver
} with Tables

/** Slick data model trait for extension, choice of backend or usage in the cake pattern. (Make sure to initialize this late.) */
trait Tables {
  val profile: scala.slick.driver.JdbcProfile
  import profile.simple._
  import scala.slick.model.ForeignKeyAction
  import scala.slick.jdbc.{GetResult => GR}
  // NOTE: GetResult mappers for plain SQL are only generated for tables where Slick knows how to map the types of all columns.
  
  /** Entity class storing rows of table Dictionary
   *  @param columnJa Database column COLUMN_JA PrimaryKey
   *  @param columnEn Database column COLUMN_EN  */
  case class DictionaryRow(columnJa: String, columnEn: Option[String])
  /** GetResult implicit for fetching DictionaryRow objects using plain SQL queries */
  implicit def GetResultDictionaryRow(implicit e0: GR[String]): GR[DictionaryRow] = GR{
    prs => import prs._
    DictionaryRow.tupled((<<[String], <<?[String]))
  }
  /** Table description of table DICTIONARY. Objects of this class serve as prototypes for rows in queries. */
  class Dictionary(tag: Tag) extends Table[DictionaryRow](tag, Some("LABO"), "DICTIONARY") {
    def * = (columnJa, columnEn) <> (DictionaryRow.tupled, DictionaryRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (columnJa.?, columnEn).shaped.<>({r=>import r._; _1.map(_=> DictionaryRow.tupled((_1.get, _2)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))
    
    /** Database column COLUMN_JA PrimaryKey */
    val columnJa: Column[String] = column[String]("COLUMN_JA", O.PrimaryKey)
    /** Database column COLUMN_EN  */
    val columnEn: Column[Option[String]] = column[Option[String]]("COLUMN_EN")
  }
  /** Collection-like TableQuery object for table Dictionary */
  lazy val Dictionary = new TableQuery(tag => new Dictionary(tag))
}