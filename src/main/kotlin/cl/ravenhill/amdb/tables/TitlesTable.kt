package cl.ravenhill.amdb.tables

import org.jetbrains.exposed.dao.id.IntIdTable

class TitlesTable(test: Boolean = false) : AbstractTable(Titles, test) {
  private object Titles : IntIdTable() {
    val uri = varchar("uri", 50)
  }
}

fun main() {
  TitlesTable()
}