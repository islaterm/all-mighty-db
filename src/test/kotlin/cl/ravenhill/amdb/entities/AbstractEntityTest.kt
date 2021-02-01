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
import kotlin.random.Random

/**
 * @author [Ignacio Slater Muñoz](mailto:islaterm@gmail.com)
 */
abstract class AbstractEntityTest {
  private val basePath = "${System.getProperty("user.dir")}/src/test/data"
  protected var seed = 0L
  protected lateinit var rng: Random

  open fun setUp() {
    File(basePath).mkdirs()
    seed = Random.nextLong()
    rng = Random(seed)
  }

  @AfterEach
  fun tearDown() {
    File(basePath).deleteRecursively()
  }
}