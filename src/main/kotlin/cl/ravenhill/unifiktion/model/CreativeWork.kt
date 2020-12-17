/**
 * "unifiktion" (c) by Ignacio Slater M.
 * "unifiktion" is licensed under a
 * Creative Commons Attribution 4.0 International License.
 * You should have received a copy of the license along with this
 * work. If not, see <http://creativecommons.org/licenses/by/4.0/>.
 */
package cl.ravenhill.unifiktion.model

import java.time.LocalDate
import java.time.format.DateTimeFormatter


/**
 * @author [Ignacio Slater Muñoz](mailto:islaterm@gmail.com)
 */
class CreativeWork(val names: Map<Language, String>, release: LocalDate) {
  val release: String = release.format(DateTimeFormatter.ISO_DATE)
  val uri = names[Language.ENGLISH]!!.replace("[^a-zA-Z\\d]".toRegex(), "")

  var wikidata: String = ""
    set(value) {
      sameAs["wikidata"] = value
      field = value
    }

  val sameAs = mutableMapOf<String, String>()

  override fun equals(other: Any?) = other is CreativeWork && other.uri == this.uri

  override fun hashCode() = uri.hashCode()
}
