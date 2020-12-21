/**
 * "unifiktion" (c) by Ignacio Slater M.
 * "unifiktion" is licensed under a
 * Creative Commons Attribution 4.0 International License.
 * You should have received a copy of the license along with this
 * work. If not, see <http://creativecommons.org/licenses/by/4.0/>.
 */
package cl.ravenhill.unifiktion.model

import cl.ravenhill.unifiktion.model.cworks.ICreativeWork
import cl.ravenhill.unifiktion.model.cworks.Manga
import cl.ravenhill.unifiktion.model.cworks.hentai.ArtistCG
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
  val artistCG: Property = ResourceFactory.createProperty("${categories}ArtistCG")
  val videoGame: Property = ResourceFactory.createProperty("${categories}VideoGame")
  val creativeWork: Property = ResourceFactory.createProperty("${categories}CreativeWork")
  val manga: Property = ResourceFactory.createProperty("${categories}Manga")
}

private object Attribute {
  val release: Property = ResourceFactory.createProperty("${attributes}release")
  val score: Property = ResourceFactory.createProperty("${attributes}score")
}

object DBModel {
  private const val basePath = "data/creative_works"
  private var ttlStorage = "$basePath.ttl"
  private var xmlStorage = "$basePath.xml"
  private var model = RDFDataMgr.loadModel(ttlStorage)

  internal fun setModel(path: String) {
    ttlStorage = "$path.ttl"
    xmlStorage = "$path.xml"
    model = RDFDataMgr.loadModel(ttlStorage)
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

  fun addArtistCG(gallery: ArtistCG) {
    addResource(gallery, Category.artistCG)
  }

  private fun addResource(work: ICreativeWork, category: Property) {
    val resource = model.createResource("$unifiktion${work.uri}")
      .addProperty(RDF.type, category)
      .addLiteral(Attribute.release, model.createTypedLiteral(work.release, XSDDatatype.XSDdate))
      .addLiteral(Attribute.score, model.createTypedLiteral(work.score))
    work.sameAs.forEach { (source, id) -> resource.addProperty(OWL.sameAs, "${source.url}$id") }
    work.names.forEach { (lang, name) ->
      resource.addLiteral(
        RDFS.label,
        model.createLiteral(name, lang.id)
      )
    }
  }

  fun save() {
    RDFDataMgr.write(File(ttlStorage).outputStream(), model, Lang.TURTLE)
    RDFDataMgr.write(File(xmlStorage).outputStream(), model, Lang.RDFXML)
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