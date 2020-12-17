/**
 * "unifiktion" (c) by Ignacio Slater M.
 * "unifiktion" is licensed under a
 * Creative Commons Attribution 4.0 International License.
 * You should have received a copy of the license along with this
 * work. If not, see <http://creativecommons.org/licenses/by/4.0/>.
 */
package cl.ravenhill.unifiktion.model

import org.apache.jena.datatypes.xsd.XSDDatatype
import org.apache.jena.rdf.model.Property
import org.apache.jena.rdf.model.ResourceFactory
import org.apache.jena.riot.Lang
import org.apache.jena.riot.RDFDataMgr
import org.apache.jena.vocabulary.OWL
import org.apache.jena.vocabulary.RDF
import org.apache.jena.vocabulary.RDFS
import java.io.File

private const val wikidata = "http://www.wikidata.org/entity/"
private const val unifiktion = "http://unifiktion.ravenhill.cl/"
private const val categories = "${unifiktion}categories/#"
private const val attributes = "${unifiktion}attributes/#"
private const val creativeWorks = "${unifiktion}cworks/"

private object Category {
  val videoGame: Property = ResourceFactory.createProperty("${categories}VideoGame")
  val creativeWork = ResourceFactory.createProperty("${categories}CreativeWork")
}

private object Attribute {
  val release: Property = ResourceFactory.createProperty("${attributes}release")
}

object DBModel {
  private const val storage = "data/creative_works.ttl"
  private val model = RDFDataMgr.loadModel(storage)


  fun addCreativeWork(work: CreativeWork) {
    val resource = model.createResource("$unifiktion${work.uri}")
      .addProperty(RDF.type, Category.creativeWork)
      .addProperty(OWL.sameAs, "$wikidata${work.wikidata}")
      .addLiteral(Attribute.release, model.createTypedLiteral(work.release, XSDDatatype.XSDdate))
    work.names.forEach { (lang, name) ->
      resource.addLiteral(
        RDFS.label,
        model.createLiteral(name, lang.id)
      )
    }
  }

  fun print() {
    RDFDataMgr.write(System.out, model, Lang.TURTLE)
  }

  fun save() {
    RDFDataMgr.write(File(storage).outputStream(), model, Lang.TURTLE)
  }
}