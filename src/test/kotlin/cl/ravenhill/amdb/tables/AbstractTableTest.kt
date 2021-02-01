package cl.ravenhill.amdb.tables

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.exists
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertTrue
import java.io.File

internal abstract class AbstractTableTest {
  private val basePath = "${System.getProperty("user.dir")}/src/test/data"
  protected val dbPath = "$basePath/amdb.db"
  protected lateinit var expectedColumnNames: List<String>

  open fun setUp() {
    File(basePath).mkdirs()
  }

  @AfterEach
  fun tearDown() {
    File(basePath).deleteRecursively()
  }

  protected fun checkInitialization(table: Table) {
    transaction {
      assertTrue(table.exists())
      val columnNames = table.columns.map { it.name }
      for (name in expectedColumnNames) {
        assertTrue(name in columnNames, "$name is not part of $columnNames")
      }
    }
  }
}