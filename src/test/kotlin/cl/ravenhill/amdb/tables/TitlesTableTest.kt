package cl.ravenhill.amdb.tables

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class TitlesTableTest : AbstractTableTest() {
  private lateinit var titles: TitlesTable

  @BeforeEach
  override fun setUp() {
    super.setUp()
    titles = TitlesTable(test = true)
    expectedColumnNames = listOf(URI_K, NAME_K, SCORE_K, RELEASE_K)
  }

  @Test
  fun initTest() {
    checkInitialization(titles.table)
  }
}