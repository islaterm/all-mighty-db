/**
 * "All-Mighty DB" (c) by Ignacio Slater M.
 * "All-Mighty DB" is licensed under a
 * Creative Commons Attribution 4.0 International License.
 * You should have received a copy of the license along with this
 * work. If not, see <http://creativecommons.org/licenses/by/4.0/>.
 */
package cl.ravenhill.amdb.tables

import org.jetbrains.exposed.dao.id.IntIdTable

/**
 * Wrapper for the table containing all the titles in the database.
 *
 * @author [Ignacio Slater Muñoz](mailto:islaterm@gmail.com)
 */
class TitlesTable(test: Boolean = false) : AbstractTable(Titles, test) {
  /**
   * The concrete titles table.
   *
   * A title is composed of:
   *  - __id__: the index of the element in the table
   *  - __uri__: a unique identifier
   *  - __name__: a human-friendly identifier
   *  - __score__: the average score of the title
   *  - __release_date__: the release date in UNIX time format
   */
  object Titles : IntIdTable() {
    val uri = varchar(URI_K, 48)
    val name = text(NAME_K)
    val score = float(SCORE_K)
    val release = long(RELEASE_K)
  }
}
