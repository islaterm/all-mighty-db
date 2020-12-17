/**
 * "amdb" (c) by Ignacio Slater M.
 * "amdb" is licensed under a
 * Creative Commons Attribution 4.0 International License.
 * You should have received a copy of the license along with this
 * work. If not, see <http://creativecommons.org/licenses/by/4.0/>.
 */
package cl.ravenhill.unifiktion

import cl.ravenhill.unifiktion.model.CreativeWork
import cl.ravenhill.unifiktion.model.DBModel
import cl.ravenhill.unifiktion.model.Language

/**
 * @author <a href=mailto:ignacio.slater@ug.uchile.cl>Ignacio Slater Muñoz</a>
 */

fun main() {
  val undertale = CreativeWork(mapOf(Language.ENGLISH to "Undertale"), "2015-09-15")
  undertale.wikidata = "Q21039924"
  DBModel.addCreativeWork(undertale)

  val ddlc = CreativeWork(mapOf(Language.ENGLISH to "Doki Doki Literature Club!"), "2017-09-21")
  ddlc.wikidata = "Q42266827"
  DBModel.addCreativeWork(ddlc)

  DBModel.save()
}

