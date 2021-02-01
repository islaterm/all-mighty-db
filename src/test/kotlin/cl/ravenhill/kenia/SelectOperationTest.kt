package cl.ravenhill.kenia

import org.apache.commons.lang3.RandomStringUtils
import org.apache.jena.rdf.model.ModelFactory
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.RepeatedTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import kotlin.random.Random
import kotlin.test.assertEquals

/**
 * @author [Ignacio Slater Muñoz](mailto:ignacio.slater@ug.uchile.cl)
 */
internal class SelectOperationTest {
  private var seed = 0L
  private lateinit var queryObj: SparqlQuery

  @BeforeEach
  fun setUp() {
    seed = Random.nextLong()
    queryObj = SparqlQuery(ModelFactory.createDefaultModel())
  }

  @RepeatedTest(32)
  fun `fields must be alphanumeric`() {
    val rng = Random(seed)
    var isValid = true
    val fields = Array<String>(rng.nextInt(10) + 1) {
      val aux = RandomStringUtils.random(rng.nextInt(20))
      if (!"^[a-zA-Z0-9_]*\$".toRegex().matches(aux)) {
        isValid = false
      }
      aux
    }
    if (!isValid) {
      assertThrows(InvalidQueryException::class.java, {
        SelectOperation(queryObj, *fields)
      }, "Test failed with seed: $seed")
    } else {
      assertDoesNotThrow { SelectOperation(queryObj, *fields) }
    }
  }

  @Test
  fun `fields can't be empty`() {
    assertThrows(InvalidQueryException::class.java) {
      SelectOperation(queryObj)
    }
  }

  @Test
  fun `a where operation is created after the select`() {
    val selectOp = SelectOperation(queryObj, "?x", "?y")
    val whereOp = selectOp where arrayOf("?x" to mapOf("pred" to "obj"))
    assertEquals(whereOp, WhereOperation(selectOp, "?x" to mapOf("pred" to "obj")))
  }

  @Test
  fun `correct select query generation`() {
    val selectOp = SelectOperation(queryObj, "?x", "?y")
    selectOp.where(arrayOf("?x" to mapOf("pred" to "obj")))
    assertEquals("SELECT ?x ?y", "$selectOp")
  }
}