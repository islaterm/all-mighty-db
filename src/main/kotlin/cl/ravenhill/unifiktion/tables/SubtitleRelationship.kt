package cl.ravenhill.unifiktion.tables

import org.jetbrains.exposed.dao.id.IntIdTable

/**
 * Table containing parent-child relationship between titles.
 * For example: A season is a child of a TV show.
 *
 * @author [Ignacio Slater Muñoz](mailto:islaterm@gmail.com)
 */
class SubtitleRelationship(test: Boolean = false) : AbstractTable(Subtitles, test) {
  private object Subtitles : IntIdTable() {
    val parent = reference("parent", TitlesTable.Titles)
    val child = reference("child", TitlesTable.Titles)
  }
}
