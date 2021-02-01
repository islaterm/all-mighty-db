package cl.ravenhill.unifiktion.model

import org.json.JSONArray
import org.json.JSONObject

val jikanAPI = "https://api.jikan.moe/v3"

/**
 * "unifiktion" (c) by Ignacio Slater M.
 * "unifiktion" is licensed under a
 * Creative Commons Attribution 4.0 International License.
 * You should have received a copy of the license along with this
 * work. If not, see <http://creativecommons.org/licenses/by/4.0/>.
 */
/**
 * @author [Ignacio Slater Muñoz](mailto:islaterm@gmail.com)
 */
class Producer private constructor(val name: String, val url: String, val anime: String) {
  companion object {
    fun from(url: String): List<Producer> {
      val request = khttp.get("$jikanAPI$url")
      val producers = mutableListOf<Producer>()
      val anime = request.jsonObject["title"] as String
      for (producer in request.jsonObject["producers"] as JSONArray) {
        val prodJson = producer as JSONObject
        producers.add(Producer(prodJson["name"] as String, prodJson["url"] as String, anime))
      }
      return producers
    }
  }

  fun asTsv() = "$anime\t$name\t$url"

  override fun toString() = "Producer($name, $url)"
}

fun main() {
  for (producer in Producer.from("/anime/40591/")) {
    println(producer.asTsv())
  }
}