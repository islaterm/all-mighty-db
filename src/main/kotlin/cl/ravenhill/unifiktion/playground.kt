/**
 * "amdb" (c) by Ignacio Slater M.
 * "amdb" is licensed under a
 * Creative Commons Attribution 4.0 International License.
 * You should have received a copy of the license along with this
 * work. If not, see <http://creativecommons.org/licenses/by/4.0/>.
 */
package cl.ravenhill.unifiktion

import cl.ravenhill.unifiktion.model.DBModel
import cl.ravenhill.unifiktion.model.Language
import cl.ravenhill.unifiktion.model.cworks.Manga
import cl.ravenhill.unifiktion.model.cworks.hentai.ArtistCG

/**
 * @author <a href=mailto:ignacio.slater@ug.uchile.cl>Ignacio Slater Muñoz</a>
 */

fun main() {
  val names =
    mapOf(
      Language.ENGLISH to "[Galaxy Monooki (Hanaduka Ryouji)] Extra CG Collection Vol. 03 " +
          "Nami-san Only (One Piece)",
      Language.JAPANESE to "[ギャラクシー物置 (華塚良治)] Extra CG Collection Vol. 03 ナミさんおんり～ " +
          "(ワンピース)"
    )
  val example = ArtistCG(names, "2009-02-04")
  example.addEquivalentEH("100002", "5625deb2bf")
  DBModel.addArtistCG(example)

  DBModel.save()
}

