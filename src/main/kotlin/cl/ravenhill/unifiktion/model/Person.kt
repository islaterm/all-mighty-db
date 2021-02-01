/**
 * "unifiktion" (c) by Ignacio Slater M.
 * "unifiktion" is licensed under a
 * Creative Commons Attribution 4.0 International License.
 * You should have received a copy of the license along with this
 * work. If not, see <http://creativecommons.org/licenses/by/4.0/>.
 */
package cl.ravenhill.unifiktion.model

/**
 * @author [Ignacio Slater Muñoz](mailto:islaterm@gmail.com)
 */
class Person(val name: String, val uri: String) {
  companion object {
    fun from(url: String): List<Person> {
      return emptyList()
    }
  }
}