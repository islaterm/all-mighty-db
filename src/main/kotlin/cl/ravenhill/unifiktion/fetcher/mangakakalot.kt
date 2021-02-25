/**
 * "unifiktion" (c) by Ignacio Slater M.
 * "unifiktion" is licensed under a
 * Creative Commons Attribution 4.0 International License.
 * You should have received a copy of the license along with this
 * work. If not, see <http://creativecommons.org/licenses/by/4.0/>.
 */
package cl.ravenhill.unifiktion.fetcher

class MangakakalotFetcher : MangaFetcher(
  "https://manganelo.com/manga",
  { id -> "https://manganelo.com/chapter/$id/chapter_.*$".toRegex() }) {

  data class MangakakalotPage(
    override var title: String = "",
    override var allLinks: List<String> = emptyList()
  ) : MangaPage(chapterIdIndex = 5)

  companion object {
    private val fetcherInstance = MangakakalotFetcher()

    fun fetch(id: String) = fetcherInstance.fetch<MangakakalotPage>(id)
  }
}

fun main() {
  MangakakalotFetcher.fetch("read_boku_no_hero_academia_manga").download()
}