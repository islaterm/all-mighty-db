package cl.ravenhill.unifiktion.model

import cl.ravenhill.unifiktion.model.cworks.Manga
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.io.File

/**
 * @author [Ignacio Slater Muñoz](mailto:ignacio.slater@ug.uchile.cl)
 */
internal class DBModelTest {
  private val modelPath = "src/test/resources/test.ttl"
  private val baseModel = "src/test/resources/base.ttl"
  private val nausicaa = Manga(
    mapOf(
      Language.ENGLISH to "Nausicaä of the Valley of the Wind",
      Language.JAPANESE to "風の谷のナウシカ"
    ), "1982-10-09"
  )

  @BeforeEach
  fun setUp() {
    File(modelPath).writeText(File(baseModel).readText())
    DBModel.setModel(modelPath)
  }

  @Test
  fun `manga is added correctly`() {
    DBModel.addManga(nausicaa)
    val manga = DBModel.getManga(nausicaa.uri)
    assertEquals(nausicaa, manga)
  }

  @AfterEach
  fun tearDown() {
    DBModel.setModel(baseModel)
    File(modelPath).delete()
  }
}