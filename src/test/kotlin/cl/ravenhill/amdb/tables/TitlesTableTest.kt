package cl.ravenhill.amdb.tables

import org.jetbrains.exposed.sql.exists
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.io.File

internal class TitlesTableTest {
  private lateinit var table: TitlesTable
  private val basePath = "${System.getProperty("user.dir")}/src/test/data"
  private val dbPath = "$basePath/amdb.db"

  @BeforeEach
  fun setUp() {
    File(basePath).mkdirs()
    table = TitlesTable(test = true)
  }

  @Test
  fun initTest() {
    val expectedColumnNames = listOf("uri", "name")
    transaction {
      assertTrue(table.table.exists())
      val columnNames = table.table.columns.map { it.name }

    }
  }

  @AfterEach
  fun tearDown() {
    File(dbPath).deleteRecursively()
  }
}