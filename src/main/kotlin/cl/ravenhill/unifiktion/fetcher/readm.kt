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

class ReadMFetcher {

  data class ReadMPage(var title: String = "", var allLinks: List<String> = emptyList()) {
    data class ReadMChapter(var pages: List<String> = emptyList())

    fun download() {
      var i = 0
      allLinks.parallelStream().forEach { link ->
        val chapter = link.split("/")[3]
        File("$title/$chapter").mkdirs()
        skrape(HttpFetcher) {
          request {
            url = "$baseUrl$link"
          }
          extractIt<ReadMChapter> {
            htmlDocument {
              findAll { eachSrc }.filter { ".*(.png|.jpg)$".toRegex().matches(it) }
                .forEach {
                  val fileName = it.split("/").last()
                  val url = URL("$baseUrl$it")
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

  companion object {
    const val baseUrl = "https://www.readm.org"

    fun fetch(id: Int): ReadMPage = skrape(HttpFetcher) {
      request {
        url = "https://www.readm.org/manga/$id"
      }
      extractIt {
        htmlDocument {
          it.title = h1 { findFirst { text } }
          it.allLinks =
            findAll { eachHref }.filter {
              "^/manga/$id/\\d+/all-pages$".toRegex().matches(it)
            }
        }
      }
    }
  }
}

fun main() {
  ReadMFetcher.fetch(4101).download()
}