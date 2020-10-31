package cl.ravenhill.amdb.playground

import io.ktor.application.*
import io.ktor.features.*
import io.ktor.gson.*
import io.ktor.response.*
import io.ktor.routing.*

@Suppress("unused")
fun Application.features() {
  install(ContentNegotiation) {
    gson {
      setPrettyPrinting()
      serializeNulls()
    }
  }
  routing {
    get("/customer") {
      val model = Customer(1, "Mary Jane", "mary@jane.com")
      call.respond(model)
    }
  }
}

data class Customer(val id: Int, val name: String, val email: String)