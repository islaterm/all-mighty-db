package cl.ravenhill.amdb.entities

import cl.ravenhill.unifiktion.tables.TitlesTable
import cl.ravenhill.unifiktion.entities.Title
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.RepeatedTest
import org.junit.jupiter.api.Test
import java.time.LocalDate
import kotlin.random.Random
import kotlin.reflect.KFunction2
import kotlin.reflect.KMutableProperty1
import kotlin.reflect.KProperty1

internal class TitleTest : AbstractEntityTest() {
  private lateinit var titles: TitlesTable
  private lateinit var startDate: LocalDate
  private val testUri = "http://example.org"
  private val testName = "Example"
  private lateinit var title: Title
  private var score = 0.0f

  @BeforeEach
  override fun setUp() {
    super.setUp()
    titles = TitlesTable(test = true)
    startDate = LocalDate.ofEpochDay(rng.nextLong(-36_500, 36_500))
    score = rng.nextFloat()
    transaction {
      title = Title.new {
        uri = testUri
        name = testName
        releaseDate = startDate
        score = 0.0F
      }
    }
  }

  @Test
  fun testConstructor() {
    transaction {
      val expectedTitle = Title.new {
        uri = testUri
        name = testName
        releaseDate = startDate
        score = 0.0F
      }
      assertEquals(expectedTitle, title)
      assertEquals(expectedTitle.hashCode(), title.hashCode())
      assertNotEquals(title, Title.new {
        uri = "test:different_uri"
        name = testName
        releaseDate = startDate
        score = 0.0F
      })
      assertNotEquals(title, Any())
    }
  }

  @Test
  fun testName() {
    checkProperty(testName, "New name", Title::name)
  }

  @RepeatedTest(16)
  fun scoreTest() {
    checkProperty(0.0f, Random(seed).nextDouble(-10.0, 10.0).toFloat(), Title::score)
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
    transaction {
      assertEquals(initialValue, prop.get(title), "Test failed with seed: $seed")
      prop.set(title, finalValue)
      assertEquals(finalValue, prop.get(title), "Test failed with seed: $seed")
    }
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
      transaction {
        val relative = Title.new {
          uri = "$i"
          name = "$i"
          releaseDate = startDate
          score = 0.0F
        }
        adder.invoke(title, relative)
        assertEquals(i, relation.get(title).size)
        checkRelationship(relation, relative, inverseRelation)
      }
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