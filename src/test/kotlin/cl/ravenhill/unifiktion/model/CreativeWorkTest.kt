package cl.ravenhill.unifiktion.model

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.RepeatedTest
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import kotlin.random.Random
import kotlin.random.nextUInt
import kotlin.reflect.KMutableProperty1

/**
 * @author [Ignacio Slater Muñoz](mailto:ignacio.slater@ug.uchile.cl)
 * @see [CreativeWork]
 * @see [Language]
 */
internal class CreativeWorkTest {
  private lateinit var startDate: String
  private val nameEn = Language.ENGLISH to "Example"
  private val nameEs = Language.SPANISH to "Ejemplo"
  private lateinit var creativeWork: CreativeWork
  private var seed = 0L
  private lateinit var rng: Random
  private lateinit var defaultNames: Map<Language, String>

  @BeforeEach
  fun setUp() {
    seed = Random.nextLong()
    rng = Random(seed)
    startDate =
      LocalDate.ofEpochDay(rng.nextLong(-36_500, 36_500)).format(DateTimeFormatter.ISO_DATE)
    defaultNames = mapOf(nameEn)
    creativeWork = CreativeWork(defaultNames, startDate)
  }

  @Test
  fun testConstructor() {
    val expectedWork = CreativeWork(defaultNames, startDate)
    assertEquals(expectedWork, creativeWork)
    assertEquals(expectedWork.hashCode(), creativeWork.hashCode())
    assertNotEquals(creativeWork, Any())
  }

  @Test
  fun `names can be added and changed`() {
    assertEquals(defaultNames, creativeWork.names)
    creativeWork.addName(nameEs)
    val newNames = (defaultNames + nameEs).toMutableMap()
    assertEquals(newNames, creativeWork.names)
    val dummy = "Dummy"
    creativeWork.addName(Language.ENGLISH to dummy)
    newNames[Language.ENGLISH] = "Dummy"
    assertEquals(newNames, creativeWork.names)
  }

  @ExperimentalUnsignedTypes
  @RepeatedTest(32)
  fun `wikidata equivalent can be set`() {
    assertTrue(creativeWork.wikidata.isBlank())
    val id = rng.nextUInt()
    creativeWork.wikidata = "Q$id"
    assertEquals("Q$id", creativeWork.wikidata, "Test failed with seed: $seed")
  }

  @RepeatedTest(32)
  fun scoreTest() {
    checkProperty(Double.NaN, Random(seed).nextDouble(-10.0, 10.0), CreativeWork::score)
  }

  @RepeatedTest(32)
  fun `release date should be modifiable`() {
    val newDate: String =
      LocalDate.ofEpochDay(Random.nextLong(-36_500, 36_500)).format(DateTimeFormatter.ISO_DATE)
    checkProperty(startDate, newDate, CreativeWork::release)
  }

  @Test
  fun `an invalid release date should throw an error`() {
    for (date in listOf("12-12-2020", "9999-99-99", "2012-13-1", "2020-11-31")) {
      assertThrows(DateTimeParseException::class.java) {
        CreativeWork(creativeWork.names, date)
      }
      assertThrows(DateTimeParseException::class.java) {
        creativeWork.release = date
      }
    }
  }

  private fun <T> checkProperty(
    initialValue: T,
    finalValue: T,
    prop: KMutableProperty1<CreativeWork, T>
  ) {
    assertEquals(initialValue, prop.get(creativeWork), "Test failed with seed: $seed")
    prop.set(creativeWork, finalValue)
    assertEquals(finalValue, prop.get(creativeWork), "Test failed with seed: $seed")
  }
}
//
//  @Test
//  fun manageChildrenTest() {
//    manageRelativesTest(CreativeWork::children, CreativeWork::addChild, CreativeWork::removeChild, CreativeWork::parents)
//  }
//
//  @Test
//  fun manageParentsTest() {
//    manageRelativesTest(CreativeWork::parents, CreativeWork::addParent, CreativeWork::removeParent, CreativeWork::children)
//  }
//
//  private fun manageRelativesTest(
//    relation: KProperty1<CreativeWork, List<CreativeWork>>,
//    adder: KFunction2<CreativeWork, CreativeWork, Unit>,
//    remover: KFunction2<CreativeWork, CreativeWork, Unit>,
//    inverseRelation: KProperty1<CreativeWork, List<CreativeWork>>
//  ) {
//    // The title starts with no relatives
//    assertTrue(relation.get(title).isEmpty())
//    // Checks that relatives can be added correctly
//    for (i in 1 until Random.nextInt(1, 10)) {
//      transaction {
//        val relative = CreativeWork.new {
//          uri = "$i"
//          name = "$i"
//          release = startDate
//          score = 0.0F
//        }
//        adder.invoke(title, relative)
//        assertEquals(i, relation.get(title).size)
//        checkRelationship(relation, relative, inverseRelation)
//      }
//    }
//    // Checks that relatives can be removed correctly
//    var expectedSize = relation.get(title).size
//    for (relative in relation.get(title)) {
//      remover.invoke(title, relative)
//      assertEquals(--expectedSize, relation.get(title).size)
//      checkRelationship(relation, relative, inverseRelation, related = false)
//    }
//    // The title should end with no relatives
//    assertTrue(relation.get(title).isEmpty())
//  }
//
//  private fun checkRelationship(
//    relation: KProperty1<CreativeWork, List<CreativeWork>>,
//    relative: CreativeWork,
//    inverseRelation: KProperty1<CreativeWork, List<CreativeWork>>,
//    related: Boolean = true
//  ) {
//    assertEquals(related, relative in relation.get(title))
//    assertEquals(related, title in inverseRelation.get(relative))
//  }
