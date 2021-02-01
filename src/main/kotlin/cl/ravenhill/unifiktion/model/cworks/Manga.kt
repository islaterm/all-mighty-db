/**
 * "unifiktion" (c) by Ignacio Slater M.
 * "unifiktion" is licensed under a
 * Creative Commons Attribution 4.0 International License.
 * You should have received a copy of the license along with this
 * work. If not, see <http://creativecommons.org/licenses/by/4.0/>.
 */
package cl.ravenhill.unifiktion.model.cworks

import cl.ravenhill.unifiktion.model.Language


/**
 * @author [Ignacio Slater Muñoz](mailto:islaterm@gmail.com)
 */
class Manga(names: Map<Language, String>, release: String) :
  AbstractWork(names, release, CWorkType.MANGA) {

  override fun equals(other: Any?) = other is Manga && other.uri == this.uri
  override fun hashCode() = uri.hashCode()
  override fun toString() = "Manga (\n" +
      "\t${names[Language.ENGLISH]},\n" +
      "\t$release,\n" +
      ")"
}
