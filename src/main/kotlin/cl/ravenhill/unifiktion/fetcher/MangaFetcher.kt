/**
 * "unifiktion" (c) by Ignacio Slater M.
 * "unifiktion" is licensed under a
 * Creative Commons Attribution 4.0 International License.
 * You should have received a copy of the license along with this
 * work. If not, see <http://creativecommons.org/licenses/by/4.0/>.
 */
package cl.ravenhill.unifiktion.fetcher

import it.skrape.core.fetcher.HttpFetcher
import it.skrape.core.htmlDocument
import it.skrape.extractIt
import it.skrape.selects.html5.h1
import it.skrape.skrape
import java.io.BufferedInputStream
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream
import java.net.URL

/**
 * @author [Ignacio Slater Muñoz](mailto:islaterm@gmail.com)
 */
open class MangaFetcher(val baseURL: String, val pattern: (String) -> Regex) {
  open class MangaPage(open var title: String = "", open var allLinks: List<String> = emptyList(), val chapterIdIndex: Int) {
    lateinit var baseURL: String

    data class MangaChapter(var pages: List<String> = emptyList())

    fun download() {
      var i = 0
      allLinks.parallelStream().forEach { link ->
        val chapter = link.split("/")[chapterIdIndex]
        File("$title/$chapter").mkdirs()
        skrape(HttpFetcher) {
          request {
            url = this@MangaPage.baseURL
          }
          extractIt<MangaChapter> {
            htmlDocument {
              findAll { eachSrc }.filter { ".*(.png|.jpg)$".toRegex().matches(it) }
                .forEach {
                  val fileName = it.split("/").last()
                  val url = URL("${this@MangaPage.baseURL}$it")
                  val inputStream = BufferedInputStream(url.openStream())
                  val output = BufferedOutputStream(FileOutputStream("$title/$chapter/$fileName"))
                  do {
                    val bytes = inputStream.read()
                    output.write(bytes)
                  } while (bytes != -1)
                }
            }
          }
          println("${++i}/${allLinks.size}")
        }
      }
    }
  }

  inline fun <reified PageSource: MangaPage> fetch(id: String): PageSource = skrape(HttpFetcher) {
    request {
      url = "$baseURL/$id"
    }
    extractIt {
      htmlDocument {
        it.title = h1 { findFirst { text } }
        it.allLinks =
          findAll { eachHref }.filter(pattern(id)::matches)
        it.baseURL = baseURL
      }
    }
  }
}