/**
 * "amdb" (c) by Ignacio Slater M.
 * "amdb" is licensed under a
 * Creative Commons Attribution 4.0 International License.
 * You should have received a copy of the license along with this
 * work. If not, see <http://creativecommons.org/licenses/by/4.0/>.
 */
package cl.ravenhill.amdb

import org.apache.jena.query.QueryExecution
import org.apache.jena.query.QueryExecutionFactory
import org.apache.jena.query.QueryFactory
import org.apache.jena.query.ResultSet
import org.apache.jena.rdf.model.Model
import org.apache.jena.riot.RDFDataMgr

/**
 * @author <a href=mailto:ignacio.slater@ug.uchile.cl>Ignacio Slater Muñoz</a>
 */

fun main() {
  val model = RDFDataMgr.loadModel("ex.ttl")

  SparqlQuery(model).use {
    it.prefixes = mapOf("owl" to "http://www.w3.org/2002/07/owl#")
    it select
        listOf("x") where
        listOf(Triple("x", "owl:sameAs", "<https://ddlc.moe>"))
    val result = it.runQuery()
    result?.forEach { querySolution ->
      println("$querySolution")
    }
  }
}

class SparqlQuery(private val model: Model) : AutoCloseable {
  private var queryExecution: QueryExecution? = null
  var prefixes: Map<String, String> = emptyMap()
  private var query = ""

  infix fun select(fields: List<String>): SparqlQuery {
    query += "SELECT "
    fields.forEach { query += "?${it} " }
    return this
  }

  infix fun where(triples: List<Triple<String, String, String>>): SparqlQuery {
    query += "WHERE {\n"
    triples.forEach { query += "?${it.first} ${it.second} ${it.third}\n" }
    query += "}"
    return this
  }

  fun runQuery(): ResultSet? {
    var prefixedQuery = ""
    prefixes.forEach { (prefix, value) -> prefixedQuery += "prefix $prefix: <$value>\n" }
    prefixedQuery += query
    queryExecution = QueryExecutionFactory.create(QueryFactory.create(prefixedQuery), model)
    return queryExecution?.execSelect()
  }

  override fun close() {
    queryExecution?.close()
  }
}

class RDFModel(private val jenaModel: Model) {
  private var query = ""

  infix fun select(fields: List<String>): RDFModel {
    query += "SELECT "
    fields.forEach { query += " ?${it}" }
    return this
  }

  infix fun where(fields: List<Triple<String, String, String>>): RDFModel {
    TODO("Not yet implemented")
  }
}