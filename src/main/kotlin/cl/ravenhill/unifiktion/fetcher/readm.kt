/**
 * "unifiktion" (c) by Ignacio Slater M.
 * "unifiktion" is licensed under a
 * Creative Commons Attribution 4.0 International License.
 * You should have received a copy of the license along with this
 * work. If not, see <http://creativecommons.org/licenses/by/4.0/>.
 */
package cl.ravenhill.unifiktion.fetcher

class ReadMFetcher :
  MangaFetcher("https://www.readm.org", { id -> "^/manga/$id/\\d+/all-pages$".toRegex() }) {

  data class ReadMPage(
    override var title: String = "",
    override var allLinks: List<String> = emptyList()
  ) : MangaPage(chapterIdIndex = 3)

  companion object {
    private val fetcherInstance = ReadMFetcher()

    fun fetch(id: Int) = fetcherInstance.fetch<ReadMPage>("$id")
  }
}

fun main() {
  ReadMFetcher.fetch(4101).download()
}