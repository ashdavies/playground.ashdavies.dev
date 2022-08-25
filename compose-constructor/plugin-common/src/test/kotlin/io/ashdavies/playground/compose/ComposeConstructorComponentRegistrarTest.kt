package io.ashdavies.playground.compose

import com.tschuchort.compiletesting.KotlinCompilation
import com.tschuchort.compiletesting.SourceFile
import org.junit.Test
import kotlin.test.assertEquals

internal class ComposeConstructorComponentRegistrarTest {

    @Test
    fun `should return successful exit code`() {
        val compilationResult = compile("""
            data class ComposeConstructor @io.ashdavies.playground.compose.Remember constructor()
        """.trimIndent()
        )

        assertEquals(KotlinCompilation.ExitCode.OK, compilationResult.exitCode)
    }
}

private fun compile(contents: String): KotlinCompilation.Result = KotlinCompilation().apply {
    compilerPlugins = listOf(ComposeConstructorComponentRegistrar())
    sources = listOf(SourceFile.kotlin("main.kt", contents))
    inheritClassPath = true
    useIR = true
}.compile()
