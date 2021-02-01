/**
 * "amdb" (c) by Ignacio Slater M.
 * "amdb" is licensed under a
 * Creative Commons Attribution 4.0 International License.
 * You should have received a copy of the license along with this
 * work. If not, see <http://creativecommons.org/licenses/by/4.0/>.
 */
package cl.ravenhill.kenia

import org.apache.jena.query.QueryExecution
import org.apache.jena.query.QueryExecutionFactory
import org.apache.jena.query.QueryFactory
import org.apache.jena.query.ResultSet
import org.apache.jena.rdf.model.Model

/**
 * Type for triples of the form `subject` `predicate` `object` where a subject can be shared by
 * multiple predicates.
 *
 * This mapping is the
 */
typealias SubPredObjMapping = Pair<String, Map<String, String>>

class InvalidQueryException(msg: String) : Exception(msg)

class SparqlQuery(private val model: Model) : AutoCloseable {
  private var queryExecution: QueryExecution? = null
  var prefixes: Map<String, String> = emptyMap()
  var query: ISparqlOperation = NullOperation

  /**
   * Creates a new ``SELECT`` operation and returns the generated query object.
   */
  infix fun select(fields: Array<String>) : SelectOperation {
    query = SelectOperation(this, *fields)
    return query as SelectOperation
  }

//  infix fun where(triples: List<Triple<String, String, String>>): SparqlQuery {
////    query += "WHERE {\n"
////    triples.forEach { query += "?${it.first} ${it.second} ${it.third}\n" }
////    query += "}"
////    return this
//  }

  fun runQuery(): ResultSet? {
    var queryPrefixes = ""
    prefixes.forEach { (prefix, value) -> queryPrefixes += "prefix $prefix: <$value>\n" }
    val prefixedQuery = "$queryPrefixes$query"
    queryExecution = QueryExecutionFactory.create(QueryFactory.create(prefixedQuery), model)
    return queryExecution?.execSelect()
  }

  override fun close() {
    queryExecution?.close()
  }
}