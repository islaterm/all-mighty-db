/**
 * "unifiktion" (c) by Ignacio Slater M.
 * "unifiktion" is licensed under a
 * Creative Commons Attribution 4.0 International License.
 * You should have received a copy of the license along with this
 * work. If not, see <http://creativecommons.org/licenses/by/4.0/>.
 */
package cl.ravenhill.unifiktion.model.cworks.hentai

import cl.ravenhill.unifiktion.model.Language
import cl.ravenhill.unifiktion.model.cworks.AbstractWork
import cl.ravenhill.unifiktion.model.cworks.CWorkType

/**
 * @author [Ignacio Slater Muñoz](mailto:islaterm@gmail.com)
 */
class ArtistCG(names: Map<Language, String>, release: String) : AbstractWork(
  names, release,
  CWorkType.ARTIST_CG
) {
  override fun equals(other: Any?) = other is ArtistCG && other.uri == this.uri
  override fun hashCode() = uri.hashCode()
  override fun toString() = "ArtistCG (\n" +
      "\t${names[Language.ENGLISH]},\n" +
      "\t$release,\n" +
      ")"
}