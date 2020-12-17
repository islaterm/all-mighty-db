/**
 * "unifiktion" (c) by Ignacio Slater M.
 * "unifiktion" is licensed under a
 * Creative Commons Attribution 4.0 International License.
 * You should have received a copy of the license along with this
 * work. If not, see <http://creativecommons.org/licenses/by/4.0/>.
 */
package cl.ravenhill.kenia

interface ISparqlOperation

/**
 * Representation of a SELECT SPARQL operation.
 *
 * @author [Ignacio Slater Muñoz](mailto:islaterm@gmail.com)
 */
open class SelectOperation(
  private val queryObj: SparqlQuery,
  private vararg val fields: String
) : ISparqlOperation {

  init {
    if (fields.isEmpty()) {
      throw InvalidQueryException("No fields where supplied to the SELECT.")
    }
    fields.forEach {
      if (!"^\\?[a-zA-Z0-9_]+\$".toRegex().matches(it)) {
        throw InvalidQueryException("'$it' is not a valid field name.")
      }
    }
  }

  /**
   * A _SPARQL_'s ``WHERE`` operation to be executed after this one.
   *
   * @param subjectMappings
   *    an array of subjects mapped to a set of predicate-to-object pairs.
   * @return WhereOperation
   *    the resulting operation.
   */
  infix fun where(subjectMappings: Array<SubPredObjMapping>): WhereOperation {
    queryObj.query = WhereOperation(this, *subjectMappings)
    return queryObj.query as WhereOperation
  }

  override fun toString(): String {
    var query = "SELECT"
    fields.forEach { query += " $it" }
    return if ("SELECT(\\s+\\?[a-zA-Z0-9_]+)+\\s*$".toRegex()
        .matches(query)
    ) query else throw InvalidQueryException("$query is not a valid query.")
  }
}

open class WhereOperation(
  private val select: SelectOperation,
  private vararg val subjectMappings: SubPredObjMapping
) : ISparqlOperation {

  private var filterQuery = ""

  infix fun filter(query: String) : WhereOperation {
    filterQuery = "FILTER ($query)"
    return this
  }

  infix fun regex(regex: String): WhereOperation {
    return filter("regex($regex)")
  }

  override fun toString(): String {
    var query = "WHERE {"
    subjectMappings.forEach { (subject, mappings) ->
      query += subject
      query += mappings.map { " ${it.key} ${it.value}" }.joinToString(";")
      query += "."
    }
    query += filterQuery
    query += "}"
    return "$select $query"
  }

  override fun equals(other: Any?): Boolean {
    return other is WhereOperation && subjectMappings.contentEquals(other.subjectMappings)
  }

  override fun hashCode(): Int {
    return subjectMappings.contentHashCode()
  }
}

object NullOperation : ISparqlOperation {
  override fun toString(): String {
    throw InvalidQueryException("Uninitialized query")
  }
}