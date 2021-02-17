/**
 * "unifiktion" (c) by Ignacio Slater M.
 * "unifiktion" is licensed under a
 * Creative Commons Attribution 4.0 International License.
 * You should have received a copy of the license along with this
 * work. If not, see <http://creativecommons.org/licenses/by/4.0/>.
 */
package cl.ravenhill.unifiktion.model.foaf

import it.skrape.core.fetcher.HttpFetcher
import it.skrape.core.htmlDocument
import it.skrape.extractIt
import it.skrape.skrape
import java.util.*

/**
 * @author [Ignacio Slater Muñoz](mailto:islaterm@gmail.com)
 */
open class Person(val name: String) {
  val uri: String
    get() = "unifiktion://${hashCode()}"

  companion object {
    fun from(url: String): List<Person> {
      return emptyList()
    }
  }
}

class Author private constructor(name: String) : Person(name) {

  companion object {
    data class ParsedAuthor(
      var name: String = "",
      var url: String = "",
      var work: String = "",
    )

    fun from(url: String): Author {
      if ("^http(s)?://(www)?myanimelist.net/manga/\\d+/.+$".toRegex()
          .matches(url)
      ) {
        TODO("Change to use MAL's API")
//        val data = skrape(HttpFetcher) {
//          request {
//            this.url = url
//          }
//          extractIt<ParsedAuthor> {
//            htmlDocument {
//              println(findAll { eachLink }.filter { (_, link) -> "^http(s)?://(www.)?myanimelist.net/people/\\d+/.+$".toRegex()
//                .matches(link) })
//            }
//          }
//        }
      }
      throw Exception()
    }
  }

  override fun hashCode(): Int {
    return Objects.hash(name)
  }
}

data class Dummy(val links: List<String> = emptyList())

fun main() {
  val data = skrape(HttpFetcher) {
    request { this.url = "https://w11.mangafreak.net/Manga/Kinnikuman" }
    extractIt<Dummy> { htmlDocument { println(findAll { eachLink }.filter { (_, link) -> "^http://images.mangafreak.net:8080/downloads/.+$".toRegex()
                .matches(link) })} }
  }
}