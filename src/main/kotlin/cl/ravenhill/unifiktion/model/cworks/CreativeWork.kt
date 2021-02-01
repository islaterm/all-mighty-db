/**
 * "unifiktion" (c) by Ignacio Slater M.
 * "unifiktion" is licensed under a
 * Creative Commons Attribution 4.0 International License.
 * You should have received a copy of the license along with this
 * work. If not, see <http://creativecommons.org/licenses/by/4.0/>.
 */
package cl.ravenhill.unifiktion.model.cworks

import cl.ravenhill.unifiktion.model.Language
import cl.ravenhill.unifiktion.model.Source
import java.time.format.DateTimeFormatter

/**
 * Interface to represent a creative work entity on the database.
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
interface ICreativeWork {
  val uri: String
  val names: Map<Language, String>
  var release: String
  var score: Double
  var wikidata: String
  val sameAs: Map<Source, String>

  /**
   * Adds (or overrides) a new name to this work.
   * @param name
   *    a pair with the language of the name and the actual name; e.g.
   *    ``Language.ENGLISH to "The Lord of the Rings"``
   */
  fun addName(name: Pair<Language, String>)
  fun addEquivalentEH(id: String, token: String)
}

/**
 * Base class for all the creative works (be it books, music, etc.) in the database.
 * @author [Ignacio Slater Muñoz](mailto:islaterm@gmail.com)
 */
abstract class AbstractWork(
  names: Map<Language, String>,
  release: String,
  type: CWorkType
) : ICreativeWork {
  override val uri = "$type${names[Language.ENGLISH]!!.replace("[^a-zA-Z\\d]".toRegex(), "")}"

  private val _names = names.toMutableMap()
  override val names: Map<Language, String>
    get() = _names.toMap()

  override var release = release
    set(value) {
      DateTimeFormatter.ISO_DATE.parse(value)
      field = value
    }

  init {
    // Checks the release date format upon creation
    DateTimeFormatter.ISO_DATE.parse(release)
  }

  override var score = Double.NaN
  override var wikidata: String = ""
    set(value) {
      _sameAs[Source.WIKIDATA] = value
      field = value
    }
  private val _sameAs = mutableMapOf<Source, String>()
  override val sameAs: Map<Source, String>
    get() = _sameAs.toMap()

  override fun addName(name: Pair<Language, String>) {
    _names[name.first] = name.second
  }

  override fun addEquivalentEH(id: String, token: String) {
    _sameAs[Source.EHENTAI] = "$id/$token"
  }
}

/**
 *
 * @author [Ignacio Slater Muñoz](mailto:islaterm@gmail.com)
 */
enum class CWorkType(private val id: String) {
  MANGA("MANGA"),
  ARTIST_CG("ARTIST_CG");

  override fun toString() = id
}
