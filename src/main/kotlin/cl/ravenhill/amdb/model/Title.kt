package cl.ravenhill.amdb.model

import java.net.URI
import java.time.LocalDate
import java.util.*

/**
 * Base class representing a Title in the database.
 * @property id URI
 *    a unique identifier for the title
 * @property name String
 *    the human-friendly identifier of the title
 * @property parentSet MutableList<Title>
 *    a list with the titles that contain this one as a child
 * @property children MutableList<Title>
 *    a list with other titles that belong to this one
 */
class Title(
  id: String,
  var name: String,
  var startDate: LocalDate,
) {
  val id: URI = URI.create(id)
  var score = 0.0
  private val childSet = mutableSetOf<Title>()
  private val parentSet = mutableSetOf<Title>()
  val children get() = childSet.toList()
  val parents get() = parentSet.toList()

  override fun equals(other: Any?) =
    other is Title && other.id == this.id

  override fun hashCode() = Objects.hash(Title::class, id)

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