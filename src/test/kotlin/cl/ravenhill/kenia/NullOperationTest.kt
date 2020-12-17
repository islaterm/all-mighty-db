package cl.ravenhill.kenia

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

/**
 * @author [Ignacio Slater Muñoz](mailto:ignacio.slater@ug.uchile.cl)
 */
internal class NullOperationTest {
  @Test
  fun `null operation cannot be used in a query`() {
    assertThrows(InvalidQueryException::class.java) {
      NullOperation.toString()
    }
  }
}