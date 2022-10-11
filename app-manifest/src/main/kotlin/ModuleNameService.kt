import groovy.json.JsonSlurper
import java.net.URL

private const val RANDOM_WORD_API = "https://random-word-api.herokuapp.com/word"

internal class ModuleNameService(private val json: JsonSlurper = JsonSlurper()) : () -> String {
    override fun invoke(): String = URL(RANDOM_WORD_API)
        .run { json.parse(openStream()) as List<*> }
        .let { "${it[0]}" }

    companion object {
        val Default = ModuleNameService()
    }
}
