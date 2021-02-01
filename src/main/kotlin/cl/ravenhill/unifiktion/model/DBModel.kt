/**
 * "unifiktion" (c) by Ignacio Slater M.
 * "unifiktion" is licensed under a
 * Creative Commons Attribution 4.0 International License.
 * You should have received a copy of the license along with this
 * work. If not, see <http://creativecommons.org/licenses/by/4.0/>.
 */
package cl.ravenhill.unifiktion.model

import cl.ravenhill.kenia.SparqlQuery
import cl.ravenhill.unifiktion.model.cworks.Manga
import org.apache.jena.datatypes.xsd.XSDDatatype
import org.apache.jena.rdf.model.*
import org.apache.jena.riot.Lang
import org.apache.jena.riot.RDFDataMgr
import org.apache.jena.vocabulary.OWL
import org.apache.jena.vocabulary.RDF
import org.apache.jena.vocabulary.RDFS
import java.io.File

private const val unifiktion = "http://unifiktion.ravenhill.cl/"
private const val categories = "${unifiktion}categories/#"
private const val attributes = "${unifiktion}attributes/#"
private const val creativeWorks = "${unifiktion}cworks/"

private object Category {
  val videoGame: Property = ResourceFactory.createProperty("${categories}VideoGame")
  val creativeWork: Property = ResourceFactory.createProperty("${categories}CreativeWork")
  val manga: Property = ResourceFactory.createProperty("${categories}Manga")
}

private object Attribute {
  val release: Property = ResourceFactory.createProperty("${attributes}release")
  val score: Property = ResourceFactory.createProperty("${attributes}score")
}

object DBModel {
  private var storage = "data/creative_works.ttl"
  private var model = RDFDataMgr.loadModel(storage)

  internal fun setModel(path: String) {
    storage = path
    model = RDFDataMgr.loadModel(storage)
  }

  fun addManga(manga: Manga) {
    val resource = model.createResource("$unifiktion${manga.uri}")
      .addProperty(RDF.type, Category.manga)
      .addLiteral(Attribute.release, model.createTypedLiteral(manga.release, XSDDatatype.XSDdate))
      .addLiteral(Attribute.score, model.createTypedLiteral(manga.score))
    manga.sameAs.forEach { (source, id) -> resource.addProperty(OWL.sameAs, "${source.url}$id") }
    manga.names.forEach { (lang, name) ->
      resource.addLiteral(
        RDFS.label,
        model.createLiteral(name, lang.id)
      )
    }
  }

  fun save() {
    RDFDataMgr.write(File(storage).outputStream(), model, Lang.TURTLE)
  }

  fun getManga(uri: String): Manga {
    val mangaResource = model.getResource("$unifiktion$uri")
    val names = mutableMapOf<Language, String>()
    mangaResource.listProperties(RDFS.label).forEach { label ->
      names[Language.getFromId(label.language)] = label.string
    }
    return Manga(names, mangaResource.getProperty(Attribute.release).string)
  }
}