/**
 * "amdb" (c) by Ignacio Slater M.
 * "amdb" is licensed under a
 * Creative Commons Attribution 4.0 International License.
 * You should have received a copy of the license along with this
 * work. If not, see <http://creativecommons.org/licenses/by/4.0/>.
 */
package cl.ravenhill.unifiktion.sparql

/**
 * Type for triples of the form `subject` `predicate` `object` where a subject can be shared by
 * multiple predicates
 */
typealias SubPredObjMapping = Pair<String, Map<String, String>>

/**
 * Representation of a SELECT SPARQL operation.
 *
 * @author [Ignacio Slater Muñoz](mailto:islaterm@gmail.com)
 */
open class SelectOperation(private vararg val fields: String) {

  init {
    if (fields.isEmpty()) {
      throw InvalidQueryException("No fields where supplied to the SELECT.")
    }
    fields.forEach {
      if (!"^[a-zA-Z0-9_]+\$".toRegex().matches(it)) {
        throw InvalidQueryException("'$it' is not a valid field name.")
      }
    }
  }

  /**
   * A SPARQL's ``WHERE`` operation to be executed after this one.
   *
   * @param subjectMappings
   *    an array of subjects mapped to a set of predicate-to-object pairs.
   * @return WhereOperation
   *    the resulting operation.
   */
  infix fun where(subjectMappings: Array<SubPredObjMapping>): WhereOperation {
    return WhereOperation(this, *subjectMappings)
  }

  override fun toString(): String {
    var query = "SELECT"
    fields.forEach { query += " ?$it" }
    return if ("SELECT(\\s+\\?[a-zA-Z0-9_]+)+\\s*$".toRegex()
        .matches(query)
    ) query else throw InvalidQueryException("$query is not a valid query.")
  }
}

open class WhereOperation(
  select: SelectOperation,
  private vararg val statements: SubPredObjMapping
) {
  override fun equals(other: Any?): Boolean {
    return other is WhereOperation && statements.contentEquals(other.statements)
  }

  override fun hashCode(): Int {
    return statements.contentHashCode()
  }
}

class InvalidQueryException(msg: String) : Exception(msg)
