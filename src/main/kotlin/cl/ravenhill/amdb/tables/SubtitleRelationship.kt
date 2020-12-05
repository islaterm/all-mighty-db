package cl.ravenhill.amdb.tables

import cl.ravenhill.amdb.entities.Title
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.IntIdTable

/**
 * Table containing parent-child relationship between titles.
 * For example: A season is a child of a TV show.
 *
 * @author [Ignacio Slater Muñoz](mailto:islaterm@gmail.com)
 */
class SubtitleRelationship(test: Boolean = false) : AbstractTable(Subtitles, test) {
  companion object : IntEntityClass<Title>(TitlesTable.Titles)

  internal object Subtitles : IntIdTable() {
    val parent = reference("parent", TitlesTable.Titles)
    val child = reference("child", TitlesTable.Titles)
    override val primaryKey = PrimaryKey(parent, child)
  }
}
