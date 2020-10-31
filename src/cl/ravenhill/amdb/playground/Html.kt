package cl.ravenhill.amdb.playground

import io.ktor.application.*
import io.ktor.html.*
import io.ktor.routing.*
import kotlinx.html.body
import kotlinx.html.h1
import kotlinx.html.li
import kotlinx.html.ul

@Suppress("unused")
fun Application.html() {
  routing {
    get("/html-dsl") {
      call.respondHtml {
        body {
          h1 { +"HTML" }
          ul {
            for (n in 0 until 10) {
              li { +"$n" }
            }
          }
        }
      }
    }
  }
}