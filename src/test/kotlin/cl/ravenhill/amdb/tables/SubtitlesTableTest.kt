package cl.ravenhill.amdb.tables

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class SubtitlesTableTest : AbstractTableTest() {
  private lateinit var subtitles: SubtitleRelationship

  @BeforeEach
  override fun setUp() {
    super.setUp()
    subtitles = SubtitleRelationship(test = true)
    expectedColumnNames = listOf("parent", "child")
  }

  @Test
  fun initTest() {
    checkInitialization(subtitles.table)
  }
}