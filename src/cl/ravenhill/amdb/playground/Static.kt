package cl.ravenhill.amdb.playground

import io.ktor.application.*
import io.ktor.http.content.*
import io.ktor.routing.*

@Suppress("unused")
fun Application.static() {
  routing {
    static("/static") {
      resources("static")
    }
  }
}