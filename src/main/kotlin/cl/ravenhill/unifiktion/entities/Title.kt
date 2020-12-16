/**
 * "All-Mighty DB" (c) by Ignacio Slater M.
 * "All-Mighty DB" is licensed under a
 * Creative Commons Attribution 4.0 International License.
 * You should have received a copy of the license along with this
 * work. If not, see <http://creativecommons.org/licenses/by/4.0/>.
 */
package cl.ravenhill.unifiktion.entities

import cl.ravenhill.unifiktion.tables.TitlesTable
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import java.time.LocalDate
import java.util.*

/**
 * Base class representing a _title_ in the database.
 * @author [Ignacio Slater Muñoz](mailto:islaterm@gmail.com)
 */
class Title(id: EntityID<Int>) : IntEntity(id) {

  companion object : IntEntityClass<Title>(TitlesTable.Titles)

  var uri by TitlesTable.Titles.uri
  var name by TitlesTable.Titles.name
  var release by TitlesTable.Titles.release
  var score by TitlesTable.Titles.score

  var releaseDate: LocalDate
    get() = LocalDate.ofEpochDay(release)
    set(value) {
      release = value.toEpochDay()
    }

  private val childSet = mutableSetOf<Title>()
  private val parentSet = mutableSetOf<Title>()
  val children get() = childSet.toList()
  val parents get() = parentSet.toList()

  override fun equals(other: Any?): Boolean {
    if (other === this) {
      return true
    }
    if (other !is Title) {
      return false
    }
    return other.uri == this.uri
  }

  override fun hashCode() = Objects.hash(Title::class, uri)

  fun addChild(child: Title) {
    childSet.add(child)
    child.parentSet.add(this)
  }

  fun addParent(parent: Title) {
    parentSet.add(parent)
    parent.childSet.add(this)
  }

  fun removeChild(child: Title) {
    childSet.remove(child)
    child.parentSet.remove(this)
  }

  fun removeParent(parent: Title) {
    parentSet.remove(parent)
    parent.childSet.remove(this)
  }
}