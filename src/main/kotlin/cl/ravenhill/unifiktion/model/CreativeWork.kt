/**
 * "unifiktion" (c) by Ignacio Slater M.
 * "unifiktion" is licensed under a
 * Creative Commons Attribution 4.0 International License.
 * You should have received a copy of the license along with this
 * work. If not, see <http://creativecommons.org/licenses/by/4.0/>.
 */
package cl.ravenhill.unifiktion.model

import java.time.format.DateTimeFormatter


/**
 * Base class for all the creative works (be it books, music, etc.) in the database.
 *
 * @property names
 *    a mapping of the name of the work for each language.
 * @property release
 *    a string representing the release date of the work following the ISO date format
 *    ``yyyy-mm-dd``.
 * @property uri
 *    the unique identifier of this element in the database.
 * @property wikidata
 *    the id of the equivalent entity on WikiData
 * @property sameAs
 *    a map of the equivalent entities on external sources (e.g. WikiData, IMDB).
 *
 * @author [Ignacio Slater Muñoz](mailto:islaterm@gmail.com)
 */
class CreativeWork(names: Map<Language, String>, release: String) {
  val uri = names[Language.ENGLISH]!!.replace("[^a-zA-Z\\d]".toRegex(), "")

  private val _names = names.toMutableMap()
  val names: Map<Language, String>
    get() = _names.toMap()

  var release = release
    set(value) {
      DateTimeFormatter.ISO_DATE.parse(value)
      field = value
    }

  var score = Double.NaN

  init {
    // Checks the release date format upon creation
    DateTimeFormatter.ISO_DATE.parse(release)
  }

  var wikidata: String = ""
    set(value) {
      _sameAs["wikidata"] = value
      field = value
    }
  private val _sameAs = mutableMapOf<String, String>()
  val sameAs = _sameAs.toMap()

  override fun equals(other: Any?) = other is CreativeWork && other.uri == this.uri

  override fun hashCode() = uri.hashCode()

  fun addName(name: Pair<Language, String>) {
    _names[name.first] = name.second
  }
}
