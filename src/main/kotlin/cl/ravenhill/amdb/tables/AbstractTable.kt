package cl.ravenhill.amdb.tables

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

abstract class AbstractTable(val table: Table, test: Boolean = false) {
  private val dbPath = "${System.getProperty("user.dir")}${
    if (test) {
      "/src/test"
    } else {
      ""
    }
  }/data/amdb.db"
  private val database: Database

  init {
    database = Database.connect("jdbc:sqlite:$dbPath", "org.sqlite.JDBC")
    transaction(database) {
      if (!test) {
        addLogger(StdOutSqlLogger)
      }
      SchemaUtils.create(table)
    }
  }
}
