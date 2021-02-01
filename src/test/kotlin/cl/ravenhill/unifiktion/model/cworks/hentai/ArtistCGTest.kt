package cl.ravenhill.unifiktion.model.cworks.hentai

import cl.ravenhill.unifiktion.model.AbstractWorkTest
import cl.ravenhill.unifiktion.model.Language
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

/**
 * @author [Ignacio Slater Muñoz](mailto:ignacio.slater@ug.uchile.cl)
 */
internal class ArtistCGTest : AbstractWorkTest() {
  private lateinit var artistCG: ArtistCG

  @BeforeEach
  override fun setUp() {
    super.setUp()
    artistCG = ArtistCG(defaultNames, startDate)
  }

  @Test
  fun `construction of the artist CG`() {
    checkConstructor(artistCG, ::createArtistCG, defaultNames, startDate)
  }

  private fun createArtistCG(names: Map<Language, String>, release: String) =
    ArtistCG(names, release)
}