/**
 * "amdb" (c) by Ignacio Slater M.
 * "amdb" is licensed under a
 * Creative Commons Attribution 4.0 International License.
 * You should have received a copy of the license along with this
 * work. If not, see <http://creativecommons.org/licenses/by/4.0/>.
 */
package cl.ravenhill.amdb.entities

import org.junit.jupiter.api.AfterEach
import java.io.File

/**
 * @author [Ignacio Slater Muñoz](mailto:islaterm@gmail.com)
 */
abstract class AbstractEntityTest {
  private val basePath = "${System.getProperty("user.dir")}/src/test/data"

  open fun setUp() {
    File(basePath).mkdirs()
  }

  @AfterEach
  fun tearDown() {
    File(basePath).deleteRecursively()
  }
}