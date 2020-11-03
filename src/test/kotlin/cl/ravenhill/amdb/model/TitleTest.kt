package cl.ravenhill.amdb.model

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.RepeatedTest
import org.junit.jupiter.api.Test
import java.time.LocalDate
import kotlin.random.Random
import kotlin.reflect.KFunction2
import kotlin.reflect.KMutableProperty1
import kotlin.reflect.KProperty1
import kotlin.test.assertTrue

internal class TitleTest {
  private lateinit var startDate: LocalDate
  private val uri = "http://example.org"
  private val name = "Example"
  private lateinit var title: Title
  private var seed = 0L

  @BeforeEach
  fun setUp() {
    seed = Random.nextLong()
    startDate = LocalDate.ofEpochDay(Random.nextLong(-36_500, 36_500))
    title = Title(uri, name, startDate)
  }

  @Test
  fun testConstructor() {
    val expectedTitle = Title(uri, name, startDate)
    assertEquals(expectedTitle, title)
    assertEquals(expectedTitle.hashCode(), title.hashCode())
    assertNotEquals(title, Title("test:different_uri", name, startDate))
    assertNotEquals(title, Any())
  }

  @Test
  fun testName() {
    checkProperty(name, "New name", Title::name)
  }

  @RepeatedTest(16)
  fun scoreTest() {
    checkProperty(0.0, Random(seed).nextDouble(-10.0, 10.0), Title::score)
  }

  @RepeatedTest(32)
  fun firstAppearanceDateTest() {
    val newDate = LocalDate.ofEpochDay(Random.nextLong(-36_500, 36_500))
    checkProperty(startDate, newDate, Title::releaseDate)
  }

  private fun <T> checkProperty(
    initialValue: T,
    finalValue: T,
    prop: KMutableProperty1<Title, T>
  ) {
    assertEquals(initialValue, prop.get(title), "Test failed with seed: $seed")
    prop.set(title, finalValue)
    assertEquals(finalValue, prop.get(title), "Test failed with seed: $seed")
  }

  @Test
  fun manageChildrenTest() {
    manageRelativesTest(Title::children, Title::addChild, Title::removeChild, Title::parents)
  }

  @Test
  fun manageParentsTest() {
    manageRelativesTest(Title::parents, Title::addParent, Title::removeParent, Title::children)
  }

  private fun manageRelativesTest(
    relation: KProperty1<Title, List<Title>>,
    adder: KFunction2<Title, Title, Unit>,
    remover: KFunction2<Title, Title, Unit>,
    inverseRelation: KProperty1<Title, List<Title>>
  ) {
    // The title starts with no relatives
    assertTrue(relation.get(title).isEmpty())
    // Checks that relatives can be added correctly
    for (i in 1 until Random.nextInt(1, 10)) {
      val relative = Title("$i", "$i", startDate)
      adder.invoke(title, relative)
      assertEquals(i, relation.get(title).size)
      checkRelationship(relation, relative, inverseRelation)
    }
    // Checks that relatives can be removed correctly
    var expectedSize = relation.get(title).size
    for (relative in relation.get(title)) {
      remover.invoke(title, relative)
      assertEquals(--expectedSize, relation.get(title).size)
      checkRelationship(relation, relative, inverseRelation, related = false)
    }
    // The title should end with no relatives
    assertTrue(relation.get(title).isEmpty())
  }

  private fun checkRelationship(
    relation: KProperty1<Title, List<Title>>,
    relative: Title,
    inverseRelation: KProperty1<Title, List<Title>>,
    related: Boolean = true
  ) {
    assertEquals(related, relative in relation.get(title))
    assertEquals(related, title in inverseRelation.get(relative))
  }
}