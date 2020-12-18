package cl.ravenhill.unifiktion.model

enum class Language(val id: String) {
  ENGLISH("en"),
  SPANISH("es"),
  JAPANESE("jp");

  companion object {
    fun getFromId(lang: String) = when (lang) {
      "en" -> ENGLISH
      "sp" -> SPANISH
      "jp" -> JAPANESE
      else -> throw UnknownLanguageException("There's no language associated with the id $lang")
    }
  }

  class UnknownLanguageException(msg: String) : Exception(msg)
}

enum class Source(val url: String) {
  WIKIDATA("http://www.wikidata.org/entity/")
}