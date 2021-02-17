/**
 * "unifiktion" (c) by Ignacio Slater M.
 * "unifiktion" is licensed under a
 * Creative Commons Attribution 4.0 International License.
 * You should have received a copy of the license along with this
 * work. If not, see <http://creativecommons.org/licenses/by/4.0/>.
 */
package cl.ravenhill.unifiktion.model.cworks

import cl.ravenhill.unifiktion.model.Language
import it.skrape.core.fetcher.HttpFetcher
import it.skrape.core.htmlDocument
import it.skrape.extractIt
import it.skrape.skrape


/**
 * @author [Ignacio Slater Muñoz](mailto:islaterm@gmail.com)
 */
class Manga(names: Map<Language, String>, release: String) :
  AbstractWork(names, release, CWorkType.MANGA) {

  override fun equals(other: Any?) = other is Manga && other.uri == this.uri
  override fun hashCode() = uri.hashCode()
  override fun toString() = "Manga (\n" +
      "\t${names[Language.ENGLISH]},\n" +
      "\t$release,\n" +
      ")"
}

data class MyDataClass(
  var allLinks: List<String> = emptyList()
)

fun main() {
    val extracted = skrape(HttpFetcher) {
      request {
        url = "https://www.readm.org/manga/4101"
      }

      extractIt<MyDataClass> {
        htmlDocument {
          it.allLinks = findAll { eachHref }.filter { "^/manga/4101/\\d+/all-pages$".toRegex().matches(it) }
        }
      }
    }
    print(extracted)
    // will print:
    // MyDataClass(httpStatusCode=200, httpStatusMessage=OK, paragraph=i'm a paragraph, allParagraphs=[i'm a paragraph, i'm a second paragraph], allLinks=[http://some.url, http://some-other.url])
  }